package Server;

import Model.*;
import Client.*;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

/**
 * Created by Kooshan on 2022.
 */
public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private Client client;

    public ClientHandler(Socket clientSocket) throws IOException, ClassNotFoundException {
        System.out.println("Client connected");
        this.clientSocket = clientSocket;
        try {
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to handle the client's request.
     */
    @Override
    public void run() {
        while (true) {
            try {
                Message choice = (Message) inputStream.readObject();
                if (Objects.equals(choice.getType(), "exitRequest")) {
                    exit();
                    return;
                }
                client = (Client) inputStream.readObject();
                if (!Objects.equals(client.getUsername(), "exitProcessRequest")) {
                    if (Objects.equals(choice.getType(), "signInRequest")) {
                        if (signIn(client)) {
                            break;
                        }
                    } else if (Objects.equals(choice.getType(), "signUpRequest")) {
                        if (signUp(client)) {
                            Data.addUser(client);
                            break;
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        for (Client user : Data.getAllUsers()) {
            if (Objects.equals(client.getUsername(), user.getUsername())) {
                client = user;
            }
        }
        client.setCurrentState("Online");
        System.out.println(client.getUsername() + " is connected");
        Server.getClients().add(this);
        //check if this clientHandler is not already in the list of clients
        while (!clientSocket.isClosed()) {
            try {
                Object inp = inputStream.readObject();
                if (inp instanceof Message) {
                    Message message = (Message) inp;
                    System.out.println(message.getSender() + " " + message.getBody() + " " + message.getType() + " " + message.getReceiver());
                    if (!Objects.equals(message.getType(), "historyRequest") && !Objects.equals(message.getType(), "friendRequestHistory") && !Objects.equals(message.getType(), "friendRequestResponse")) {
                        if (Objects.equals(message.getType(), "private") || Objects.equals(message.getType(), "friendRequest")) {
                            boolean foundUser = false;
                            for (Client clientCheck : Data.getAllUsers()) {
                                if (clientCheck.getUsername().equals(message.getReceiver())) {
                                    if (!clientCheck.isBlocked(message.getSender())) {
                                        foundUser = true;
                                    }
                                }
                            }
                            if (foundUser) {
                                Data.addMessage(message);
                            }
                        } else {
                            Data.addMessage(message);
                        }
                    }
                    if (message.getType().equals("historyRequest")) {
                        historyRequest(message);
                    } else if (message.getType().equals("showGroupMembersRequest")) {
                        showGroupMembers(message);
                    } else if (message.getType().equals("channelChat")) {
                        channelChat(message);
                    } else if (message.getType().equals("friendRequest")) {
                        friendRequest(message);
                    } else if (message.getType().equals("showAbilitiesRequest")) {
                        showAbilities(message);
                    } else if (message.getType().equals("showGivableAbilitiesRequest")) {
                        showGivableAbilities(message);
                    } else if (message.getType().equals("friendRequestHistory")) {
                        friendRequestHistory(message);
                    } else if (message.getType().equals("private")) {
                        privateChat(message);
                    } else if (message.getType().equals("signInGroupRequest")) {
                        signInGroup(message);
                    } else if (message.getType().equals("changePasswordRequest")) {
                        changePassword(message);
                    } else if (message.getType().equals("createGroupRequest")) {
                        createGroup(message);
                    } else if (message.getType().equals("blockRequest")) {
                        block(message);
                    } else if (message.getType().equals("showGroupsRequest")) {
                        showGroups(message);
                    } else if (message.getType().equals("friendRequestResponse")) {
                        friendRequestResponse(message);
                    } else if (message.getType().equals("requestFriendList")) {
                        friendList(message);
                    } else if (message.getType().equals("createChannelRequest")) {
                        createChannel(message);
                    } else if (message.getType().equals("deleteChannelRequest")) {
                        deleteChannel(message);
                    } else if (message.getType().equals("deleteGroupRequest")) {
                        deleteGroup(message);
                    } else if (message.getType().equals("createRoleRequest")) {
                        createRole(message);
                    } else if (message.getType().equals("addAbilityToRoleRequest")) {
                        addAbilityToRole(message);
                    } else if (message.getType().equals("addUserToGroupRequest")) {
                        addUserToGroup(message);
                    } else if (message.getType().equals("changeStateRequest")) {
                        changeState(message);
                    } else if (message.getType().equals("showChannelsRequest")) {
                        showChannels(message);
                    } else if (message.getType().equals("signInChannelRequest")) {
                        signInChannel(message);
                    } else if (message.getType().equals("showRolesRequest")) {
                        showRoles(message);
                    } else if (message.getType().equals("defineRoleRequest")) {
                        defineRole(message);
                    } else if (message.getType().equals("logOutRequest")) {
                        break;
                    } else if (message.getType().equals("renameGroupRequest")) {
                        renameGroup(message);
                    } else if (message.getType().equals("removeUserRequest")) {
                        removeUser(message);
                    } else if (message.getType().equals("changeProfileRequest")) {
                        changeProfilePicture(clientSocket);
                    } else if (message.getType().equals("banUserRequest")) {
                        banUser(message);
                    } else if (message.getType().equals("showBannableUsersRequest")) {
                        showBannableUsers(message);
                    } else if (message.getType().equals("showBannableChannelsRequest")) {
                        showBannableChannels(message);
                    }
                    else {
                        for (ClientHandler clientHandler : Server.getClients()) {
                            clientHandler.getOutputStream().writeObject(message);
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                try {
                    client.setCurrentState("Offline");
                    inputStream.close();
                    outputStream.close();
                    clientSocket.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
//        try {
        client.setCurrentState("Offline");
        Server.getClients().remove(this);
        //clientSocket.close();
        System.out.println(client.getUsername() + " is disconnected");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }


    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    /**
     * update the state of the client
     *
     * @param state update the state and notify every friend
     */
    public void updateState(String state) throws IOException {
        client.setState(state);
        for (ClientHandler clientHandler : Server.getClients()) {
            if (clientHandler.getClient() == client) {
                continue;
            }
            clientHandler.getOutputStream().writeObject(client);
        }
    }

    /**
     * sign in the client
     *
     * @param client the client to sign in
     * @return true if the client is signed in, false otherwise
     * @throws IOException
     */
    public boolean signIn(Client client) throws IOException {
        for (ClientHandler clientHandler : Server.getClients()) {
            if (Objects.equals(clientHandler.getClient().getUsername(), client.getUsername()) && clientHandler != this) {
                try {
                    outputStream.writeObject(new Message(null, "You are already connected with this account", null, "warning"));
                    outputStream.writeObject(new Message(null, "false", null, "signInResponse"));
                    return false;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessError e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        for (Client clientCheck : Data.getAllUsers()) {
            if (Objects.equals(clientCheck.getUsername(), client.getUsername()) && Objects.equals(clientCheck.getPassword(), client.getPassword())) {
                outputStream.writeObject(new Message(null, "true", null, "signInResponse"));
                outputStream.writeObject(clientCheck);
                return true;
            }
        }
        return false;
    }

    /**
     * sign up the client
     *
     * @param client the client to sign up
     * @return true if the client is signed up, false otherwise
     * @throws IOException
     */
    public boolean signUp(Client client) throws IOException {
        for (Client check : Data.getAllUsers()) {
            if (Objects.equals(check.getUsername(), client.getUsername())) {
                try {
                    outputStream.writeObject(new Message(null, "This User Name Already Exists", null, "warning"));
                    outputStream.writeObject(new Message(null, "false", null, "signUpResponse"));
                    return false;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        outputStream.writeObject(new Message(null, "true", null, "signUpResponse"));
        return true;
    }

    /**
     * Exits Server And Wants To Close The Client
     */
    public void exit() {
        try {
            outputStream.writeObject(new Message(null, "Bye Bye!", null, "terminate"));
            outputStream.flush();
            clientSocket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * send a message to a friend as private Message
     *
     * @param message the message to send
     * @throws IOException
     */
    public void historyRequest(Message message) throws IOException {
        for (Message checkMessage : Data.getAllMessages()) {
            if (((checkMessage.getSender().equals(message.getSender()) && checkMessage.getReceiver().equals(message.getReceiver())) ||
                    (checkMessage.getReceiver().equals(message.getSender()) && checkMessage.getSender().equals(message.getReceiver()))) && Objects.equals(checkMessage.getType(), "private")) {
                for (ClientHandler clientHandler : Server.getClients()) {
                    System.out.println(clientHandler.getClient().getUsername());
                    if (clientHandler.getClient().getUsername().equals(message.getReceiver())) {
                        clientHandler.getOutputStream().writeObject(new Message(checkMessage.getSender(), checkMessage.getBody(), checkMessage.getReceiver(), "historyResponse"));
                    }
                }
            }
        }
    }

    /**
     * send a message to a friend as groupChat
     *
     * @param message
     * @throws IOException
     */
    public void channelChat(Message message) throws IOException {
        for (Group group : Data.returnGroups()) {
            if (group.getGroupName().equals(message.getReceiver().split("-")[0])) {
                for (Channel channel : group.getChannels()) {
                    if (channel.getName().equals(message.getReceiver().split("-")[1])) {
                        //send notification to the taged user starting with @user

                        if (message.getBody().contains("@")) {
                            //get the words containing @
                            String[] userNames = message.getBody().split(" ");
                            for (String userNameTaged : userNames) {
                                if (userNameTaged.startsWith("@")) {
                                    //get the rest of user name except @
                                    String userName = userNameTaged.substring(1);
                                    tagUser(userName, message);
                                }
                            }
                        }
                        for (ClientHandler clientHandlerCheck : Server.getClients()) {
                            for (Client client : group.getMembers().keySet()) {
                                if (client.getUsername().equals(clientHandlerCheck.getClient().getUsername())) {
                                    if (!Objects.equals(client.getUsername(), message.getSender())) {
                                        clientHandlerCheck.getOutputStream().writeObject(message);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * send a friend request to a friend
     *
     * @param message the message to send
     * @throws IOException
     */
    public void friendRequest(Message message) throws IOException {
        boolean foundUser = false;
        for (Client clientCheck : Data.getAllUsers()) {
            if (clientCheck.getUsername().equals(message.getReceiver())) {
                if (!clientCheck.isBlocked(message.getSender())) {
                    foundUser = true;
                }
            }
        }
        if (!foundUser) {
            for (ClientHandler clientHandlerFound : Server.getClients()) {
                if (clientHandlerFound.getClient().getUsername().equals(message.getSender())) {
                    clientHandlerFound.getOutputStream().writeObject(new Message("Server", "User " + message.getReceiver() + " is not Available.", message.getSender(), "warning"));
                }
            }
        }
    }

    /**
     * show Abilities of the client in the server
     *
     * @param message the sender and the group that they want to see the abilities of
     * @throws IOException
     */
    public void showAbilities(Message message) throws IOException {
        for (Group group : Data.returnGroups()) {
            //the first half is group name and the second half is the channel name
            if (group.getGroupName().equals(message.getBody())) {
                for (Client client : group.getMembers().keySet()) {
                    if (client.getUsername().equals(message.getSender())) {
                        for (Role role : group.getMembers().get(client)) {
                            outputStream.writeObject(new Message("Server", "Your Role: " + role.getName(), message.getSender(), "showAbilitiesResponse"));
                        }
                        for (Ability ability : Ability.values()) {
                            for (Role role : group.getMembers().get(client)) {
                                if (role.getAbilities().contains(ability)) {
                                    outputStream.writeObject(new Message("Server", "Your Ability: " + ability.toString(), message.getSender(), "showAbilitiesResponse"));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * the abilities that you can give to other clients in the server
     *
     * @param message the sender of the request
     * @throws IOException
     * @respond the abilities that the client can give to other clients
     */
    public void showGivableAbilities(Message message) throws IOException {
        for (ClientHandler clientHandler : Server.getClients()) {
            if (clientHandler.getClient().getUsername().equals(client.getUsername())) {
                for (Ability ability : Ability.values()) {
                    if (!Objects.equals(ability.toString(), "CreateRole") && !Objects.equals(ability.toString(), "RemoveRole") && !Objects.equals(ability.toString(), "DeleteServer") && !Objects.equals(ability.toString(), "addUser") && !Objects.equals(ability.toString(), "DefineRole")) {
                        clientHandler.getOutputStream().writeObject(new Message("Server", "You Can Give: " + ability.toString(), message.getSender(), "showGivableAbilitiesResponse"));
                    }
                }

            }
        }
    }

    /**
     * sign In to a Group of choice
     *
     * @param message the group you want to sign in to
     * @throws IOException
     * @respond a message to client whether they were successful or not
     */
    public void signInGroup(Message message) throws IOException {
        //check if the client is in the group or not
        boolean signInFlag = false;
        for (Group group : Data.returnGroups()) {
            if (group.getGroupName().equals(message.getBody())) {
                for (Client client : group.getMembers().keySet()) {
                    if (client.getUsername().equals(message.getSender())) {
                        outputStream.writeObject(new Message(null, "True", message.getSender(), "signInGroupResponse"));
                        showGroupMembers(new Message(null, null, message.getBody(), "showGroupMembers"));
                        signInFlag = true;
                    }
                }
                if (!signInFlag) {
                    outputStream.writeObject(new Message(null, "False", message.getSender(), "signInGroupResponse"));
                }
            }

        }
    }

    /**
     * sign in to a channel of choice
     *
     * @param message the channel you want to sign in and what group it belongs to
     * @throws IOException
     * @respond a message to client whether they were successful or not
     */
    public void signInChannel(Message message) throws IOException {
        //check if the client is in the group or not
        boolean signInFlag = false;
        boolean isBlocked = false;
        boolean isInGroup = false;
        for (Group group : Data.returnGroups()) {
            if (group.getGroupName().equals(message.getReceiver())) {
                for (Channel channel : group.getChannels()) {
                    if (channel.getName().equals(message.getBody())) {
                        for (Client client : group.getMembers().keySet()) {
                            if (client.getUsername().equals(message.getSender())) {
                                isInGroup = true;
                                break;
                            }
                        }
                        for (Client client : channel.getBannedUsers()) {
                            if (client.getUsername().equals(message.getSender())) {
                                isBlocked = true;
                                break;
                            }
                        }
                        if (!isBlocked && isInGroup) {
                            outputStream.writeObject(new Message(null, "True", message.getSender(), "signInChannelResponse"));
                            signInFlag = true;
                            if (checkAbility(new Message(message.getSender(), "ChatHistory", message.getReceiver(), "check"))) {
                                showChannelHistory(new Message(message.getSender(), message.getBody(), message.getReceiver(), "showChannelHistory"));
                            }
                        }
                    }
                }
            }
        }
        if (!signInFlag) {
            outputStream.writeObject(new Message(null, "False", message.getSender(), "signInChannelResponse"));
        }
    }

    /**
     * show history of given channel
     *
     * @param message the channel you want to see the history of
     * @throws IOException
     */
    private void showChannelHistory(Message message) throws IOException {
        System.out.println(message.getSender() + message.getBody() + message.getReceiver());
        for (Group group : Data.returnGroups()) {
            if (group.getGroupName().equals(message.getReceiver())) {
                for (Channel channel : group.getChannels()) {
                    if (channel.getName().equals(message.getBody())) {
                        for (Message messageHistory : Data.getAllMessages()) {
                            try {
                                if (messageHistory.getReceiver().split("-")[0].equals(message.getReceiver()) && messageHistory.getReceiver().split("-")[1].equals(message.getBody())) {
                                    outputStream.writeObject(new Message(messageHistory.getSender(), messageHistory.getBody(), messageHistory.getReceiver(), "channelChat"));
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * add an ability to a role
     *
     * @param message the role you want to add an ability to and the ability you want to add
     * @throws IOException
     */
    public void addAbilityToRole(Message message) throws IOException {
        for (Group group : Data.returnGroups()) {
            if (group.getGroupName().equals(message.getReceiver())) {
                for (Client clientCheck : group.getMembers().keySet()) {
                    if (clientCheck.getUsername().equals(message.getSender())) {
                        for (Role role : group.getMembers().get(clientCheck)) {
                            if (role.getAbilities().contains(Ability.CreateRole)) {
                                if (group.addAbilityToRole(message.getBody().split("-")[0], message.getBody().split("-")[1])) {
                                    outputStream.writeObject(new Message(message.getReceiver(), "Added To Role", message.getSender(), "inform"));
                                } else {
                                    outputStream.writeObject(new Message(message.getReceiver(), "Check Again", message.getSender(), "inform"));
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * add a user to the group
     *
     * @param message the group you want to add the user to and the user you want to add
     * @throws IOException
     * @respond a message to client whether they were successful or not and the client exists or not
     */
    public void addUserToGroup(Message message) throws IOException {
        boolean foundUser = false;
        for (Group group : Data.returnGroups()) {
            if (group.getGroupName().equals(message.getBody())) {
                for (Client clientCheck : group.getMembers().keySet()) {
                    if (clientCheck.getUsername().equals(message.getSender())) {
                        System.out.println("Adding user to group");
                        for (Client addedUser : Data.getAllUsers()) {
                            if (addedUser.getUsername().equals(message.getReceiver())) {
                                foundUser = true;
                                System.out.println(addedUser.getUsername() + " " + message.getReceiver());
                                if (group.addMember(addedUser)) {
                                    System.out.println("User added to group");
                                    for (ClientHandler clientHandler : Server.getClients()) {
                                        if (clientHandler.getClient().getUsername().equals(message.getSender())) {
                                            clientHandler.getOutputStream().writeObject(new Message(null, "User Added to Group Successfully", null, "inform"));
                                        }
                                    }
                                    System.out.println(message.getSender() + "-Lobby");
                                    Message welcomeMessage = new Message("Server", addedUser.getUsername() + " has been added to " + group.getGroupName() + ".", group.getGroupName() + "-Lobby", "channelChat");
                                    Data.addMessage(welcomeMessage);
                                    channelChat(welcomeMessage);
                                } else {
                                    for (ClientHandler clientHandler : Server.getClients()) {
                                        if (clientHandler.getClient().getUsername().equals(message.getSender())) {
                                            System.out.println("User already in group");
                                            clientHandler.getOutputStream().writeObject(new Message(null, "This User Already Exists", null, "inform"));
                                        }
                                    }
                                }
                            }
                        }
                        if (!foundUser) {
                            for (ClientHandler clientHandlerFound : Server.getClients()) {
                                if (clientHandlerFound.getClient().getUsername().equals(message.getSender())) {
                                    clientHandlerFound.getOutputStream().writeObject(new Message("Server", "User " + message.getReceiver() + " is not Available.", message.getSender(), "warning"));
                                }
                            }
                        }
                    }
                }
                break;
            }
        }
    }

    /**
     * create a new role
     *
     * @param message name of new role and the group that this role belongs to
     */
    public void createRole(Message message) {
        for (Group group : Data.returnGroups()) {
            if (group.getGroupName().equals(message.getReceiver())) {
                System.out.println(group.getGroupName());
                for (Client clientCheck : group.getMembers().keySet()) {
                    if (clientCheck.getUsername().equals(message.getSender())) {
                        for (Role role : group.getMembers().get(clientCheck)) {
                            if (role.getAbilities().contains(Ability.CreateRole)) {
                                group.createRole(message.getBody());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * create a new Channel in the group
     *
     * @param message the name of the channel and the group that this channel belongs to
     * @throws IOException
     */
    public void createChannel(Message message) throws IOException {
        if (checkAbility(new Message(message.getSender(), "CreateChannel", message.getReceiver(), "check"))) {
            for (Group group : Data.returnGroups()) {
                if (group.getGroupName().equals(message.getReceiver())) {
                    for (Client clientCheck : group.getMembers().keySet()) {
                        for (Role role : group.getMembers().get(clientCheck)) {
                            if (role.getAbilities().contains(Ability.CreateChannel)) {
                                group.createChannel(message.getBody().split("-")[0], message.getBody().split("-")[1]);
                                outputStream.writeObject(new Message("Server", "Channel Created", message.getSender(), "inform"));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * delete a Channel in the group
     *
     * @param message the name of the channel and the group that this channel belongs to
     * @throws IOException
     */
    public void deleteChannel(Message message) throws IOException {
        if (checkAbility(new Message(message.getSender(), "DeleteChannel", message.getReceiver(), "check"))) {
            for (Group group : Data.returnGroups()) {
                if (group.getGroupName().equals(message.getReceiver())) {
                    for (Client clientCheck : group.getMembers().keySet()) {
                        for (Role role : group.getMembers().get(clientCheck)) {
                            if (role.getAbilities().contains(Ability.CreateChannel)) {
                                if (!Objects.equals(message.getBody(), "Lobby")) {
                                    group.deleteChannel(message.getBody());
                                    outputStream.writeObject(new Message("Server", "Channel Deleted", message.getSender(), "inform"));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Delete a group
     *
     * @param message name of group and who wants to delete it
     */
    public void deleteGroup(Message message) throws IOException {
        if (checkAbility(new Message(message.getSender(), "DeleteGroup", message.getReceiver(), "check"))) {
            for (Group group : Data.returnGroups()) {
                if (group.getGroupName().equals(message.getReceiver())) {
                    if (checkAbility(new Message(message.getSender(), "DeleteGroup", message.getReceiver(), "check"))) {
                        Data.removeGroup(message.getReceiver());
                        outputStream.writeObject(new Message("Server", "Group Deleted", message.getSender(), "inform"));
                        return;
                    }
                }
            }
        }
    }

    /**
     * change state of a user
     *
     * @param message the state
     */
    public void changeState(Message message) {
        for (Client clientCheck : Data.getAllUsers()) {
            if (clientCheck.getUsername().equals(message.getSender())) {
                clientCheck.setState(message.getBody());
            }
        }
    }

    /**
     * show all friend Requests
     *
     * @param message the user that wants to see the friend requests
     * @throws IOException
     */
    public void friendRequestHistory(Message message) throws IOException {
        for (Message checkMessage : Data.getAllMessages()) {
            if (checkMessage.getReceiver().equals(message.getSender()) && Objects.equals(checkMessage.getType(), "friendRequest")) {
                outputStream.writeObject(new Message(checkMessage.getSender(), null, checkMessage.getReceiver(), "friendRequestHistoryResponse"));
            }
        }

    }

    /**
     * see friend list
     *
     * @param message the username of the user
     * @throws IOException
     */
    public void friendList(Message message) throws IOException {
        for (ClientHandler clientHandler : Server.getClients()) {
            if (clientHandler.getClient().getUsername().equals(message.getSender())) {
                for (Client friend : clientHandler.getClient().getFriends()) {
                    System.out.println(friend.getUsername() + " " + friend.getState());
                    //if the client is online then we show the chosen state otherwise we show offline
                    if (friend.getCurrentState().equals("Online")) {
                        outputStream.writeObject(new Message(friend.getUsername(), friend.getState(), message.getSender(), "friendListRespone"));
                    } else {
                        outputStream.writeObject(new Message(friend.getUsername(), "Offline", message.getSender(), "friendListRespone"));
                    }
                }
            }
        }
    }

    /**
     * friend Request response
     *
     * @param message the response
     * @throws IOException
     */
    public void friendRequestResponse(Message message) throws IOException {
        //approve the friend request or decline it
        for (ClientHandler clientHandler : Server.getClients()) {
            if (clientHandler.getClient().getUsername().equals(message.getSender())) {
                clientHandler.getOutputStream().writeObject(new Message(message.getReceiver(), message.getBody(), message.getSender(), "friendRequestResponse"));
            }
            if (clientHandler.getClient().getUsername().equals(message.getReceiver())) {
                clientHandler.getOutputStream().writeObject(message);
            }
        }
        if (message.getBody().equals("yes")) {
            Client sender = null;
            Client receiver = null;
            for (ClientHandler clientHandler : Server.getClients()) {
                if (clientHandler.getClient().getUsername().equals(message.getSender())) {
                    sender = clientHandler.getClient();
                }
                if (clientHandler.getClient().getUsername().equals(message.getReceiver())) {
                    receiver = clientHandler.getClient();
                }
            }
            assert sender != null;
            sender.addFriend(receiver);
            assert receiver != null;
            receiver.addFriend(sender);
        }
        Data.removeFriendRequest(message);
        Data.removeMessage(message);
//approve the friend request or decline it
        for (ClientHandler clientHandler : Server.getClients()) {
            if (clientHandler.getClient().getUsername().equals(message.getSender())) {
                clientHandler.getOutputStream().writeObject(new Message(message.getReceiver(), message.getBody(), message.getSender(), "friendRequestResponse"));
            }
            if (clientHandler.getClient().getUsername().equals(message.getReceiver())) {
                clientHandler.getOutputStream().writeObject(message);
            }
        }
        if (message.getBody().equals("yes")) {
            Client sender = null;
            Client receiver = null;
            for (ClientHandler clientHandler : Server.getClients()) {
                if (clientHandler.getClient().getUsername().equals(message.getSender())) {
                    sender = clientHandler.getClient();
                }
                if (clientHandler.getClient().getUsername().equals(message.getReceiver())) {
                    receiver = clientHandler.getClient();
                }
            }
            assert sender != null;
            sender.addFriend(receiver);
            assert receiver != null;
            receiver.addFriend(sender);
        }
        Data.removeFriendRequest(message);
        Data.removeMessage(message);

    }

    /**
     * show groups that the user is in
     *
     * @param message the username of the user
     * @throws IOException
     */
    public void showGroups(Message message) throws IOException {
        for (ClientHandler clientHandler : Server.getClients()) {
            if (clientHandler.getClient().getUsername().equals(message.getSender())) {
                for (Group group : Data.getAllGroups()) {
                    for (Client clientCheck : group.getMembers().keySet()) {
                        if (clientCheck.getUsername().equals(message.getSender())) {
                            clientHandler.getOutputStream().writeObject(new Message("Server", group.getGroupName(), message.getSender(), "showGroupsResponse"));
                        }
                    }
                }
            }
        }
    }

    /**
     * block a user
     *
     * @param message the message containing the user to block and the user to block
     */
    public void block(Message message) {
        //block user
        for (ClientHandler clientHandler : Server.getClients()) {
            if (clientHandler.getClient().getUsername().equals(message.getSender())) {
                for (ClientHandler block : Server.getClients()) {
                    if (block.getClient().getUsername().equals(message.getReceiver())) {
                        clientHandler.getClient().blockUser(block.getClient());
                    }
                }
            }
        }
    }

    /**
     * create new group
     *
     * @param message message with group name
     */
    public void createGroup(Message message) throws IOException {
        for (ClientHandler clientHandler : Server.getClients()) {
            if (Objects.equals(message.getSender(), clientHandler.getClient().getUsername())) {
                for (Group group : Data.getAllGroups()) {
                    if (group.getGroupName().equals(message.getBody())) {
                        clientHandler.getOutputStream().writeObject(new Message("Server", "Group already exists", message.getSender(), "warning"));
                        return;
                    }
                }
                Data.createGroup(clientHandler.getClient(), message.getBody());
                clientHandler.getOutputStream().writeObject(new Message("Server", "Group created", message.getSender(), "warning"));
            }
        }
    }

    /**
     * change password of client
     *
     * @param message message with new password
     */
    public void changePassword(Message message) {
        //change password of user
        for (ClientHandler clientHandler : Server.getClients()) {
            if (clientHandler.getClient().getUsername().equals(message.getSender())) {
                clientHandler.getClient().setPassword(message.getBody());
            }
        }
    }

    /**
     * private chat
     *
     * @param message the message you want to send to the client in private message
     * @throws IOException
     */
    public void privateChat(Message message) throws IOException {
        boolean foundUser = false;
        for (ClientHandler clientHandler : Server.getClients()) {
            if (clientHandler.getClient().getUsername().equals(message.getReceiver())) {
                if (!clientHandler.getClient().isBlocked(message.getSender())) {
                    clientHandler.getOutputStream().writeObject(message);
                    foundUser = true;
                } else {
                    for (ClientHandler findBlocked : Server.getClients()) {
                        if (findBlocked.getClient().getUsername().equals(message.getSender())) {
                            findBlocked.getOutputStream().writeObject(new Message("Server", "You have been blocked by " + message.getReceiver() + ".", message.getSender(), "private"));
                        }
                    }
                    Data.removeMessage(message);
                }
            }
        }
        if (!foundUser) {
            for (Client clientCheck : Data.getAllUsers()) {
                if (clientCheck.getUsername().equals(message.getReceiver())) {
                    if (!clientCheck.isBlocked(message.getSender())) {
                        foundUser = true;
                    }
                }
            }
        }
        if (!foundUser) {
            for (ClientHandler clientHandlerFound : Server.getClients()) {
                if (clientHandlerFound.getClient().getUsername().equals(message.getSender())) {
                    clientHandlerFound.getOutputStream().writeObject(new Message("Server", "User " + message.getReceiver() + " is not Available.", message.getSender(), "warning"));
                }
            }
        }
    }

    /**
     * shows channels that you can access
     *
     * @param message the message that contains the username of the user that sent the request and the Group that the user is in
     * @throws IOException
     */
    public void showChannels(Message message) throws IOException {
        for (Group group : Data.getAllGroups()) {
            if (group.getGroupName().equals(message.getReceiver())) {
                for (Channel channel : group.getChannels()) {
                    boolean channelBan = false;
                    for (Client clientBan : channel.getBannedUsers()) {
                        if (clientBan.getUsername().equals(message.getSender())) {
                            channelBan = true;
                        }
                    }
                    if (!channelBan) {
                        outputStream.writeObject(new Message("Server", channel.getName(), message.getSender(), "showChannelsResponse"));
                    }
                }
            }

        }
    }

    /**
     * show groups that are currently in the server
     *
     * @param message the group you want to see users
     * @throws IOException
     */
    public void showGroupMembers(Message message) throws IOException {
        for (Group group : Data.getAllGroups()) {
            if (group.getGroupName().equals(message.getReceiver())) {
                for (Client clientInGroup : group.getMembers().keySet()) {
                    StringBuilder roles = new StringBuilder();
                    for (Role role : group.getMembers().get(clientInGroup)) {
                        roles.append(role.getName()).append(" ");
                    }
                    if (Objects.equals(clientInGroup.getCurrentState(), "Online")) {
                        outputStream.writeObject(new Message("Server", clientInGroup.getUsername() + ": " + clientInGroup.getState() + "   " + roles.toString(), message.getSender(), "warning"));
                    } else {
                        outputStream.writeObject(new Message("Server", clientInGroup.getUsername() + ": " + "Offline" + "   " + roles.toString(), message.getSender(), "warning"));
                    }
                }
            }
        }
    }

    /**
     * show roles in a server
     *
     * @param message the group you want to see roles
     * @throws IOException
     */
    public void showRoles(Message message) throws IOException {
        for (Group group : Data.getAllGroups()) {
            if (group.getGroupName().equals(message.getReceiver())) {
                for (Role role : group.getRoles()) {
                    outputStream.writeObject(new Message("Server", role.getName(), message.getSender(), "showRolesResponse"));
                }
            }
        }

    }

    /**
     * give a role to the user
     *
     * @param message the role and the person we are giving role to
     * @throws IOException
     */
    public void defineRole(Message message) throws IOException {
        for (Group group : Data.getAllGroups()) {
            if (group.getGroupName().equals(message.getReceiver())) {
                for (Role role : group.getRoles()) {
                    if (role.getName().equals(message.getBody().split("-")[1])) {
                        for (Client client : group.getMembers().keySet()) {
                            if (client.getUsername().equals(message.getBody().split("-")[0])) {
                                if (!group.getMembers().get(client).contains(role)) {
                                    group.getMembers().get(client).add(role);
                                    System.out.println(client.getUsername() + " has been given role " + role.getName());
                                    outputStream.writeObject(new Message("Server", "Role Matched to User", message.getSender(), "inform"));
                                    return;
                                } else {
                                    outputStream.writeObject(new Message("Server", "Role already assigned to user", message.getSender(), "inform"));
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
        outputStream.writeObject(new Message("Server", "Role Not Found", message.getSender(), "inform"));
    }

    /**
     * Check whether you are eligible to do an ability or not
     *
     * @param message the ability and doer of the ability
     * @return true if you are eligible to do the ability, false if not
     */
    public boolean checkAbility(Message message) {
        for (Group group : Data.getAllGroups()) {
            if (group.getGroupName().equals(message.getReceiver())) {
                for (Client clientCheck : group.getMembers().keySet()) {
                    if (clientCheck.getUsername().equals(message.getSender())) {
                        for (Role roleCheck : group.getMembers().get(clientCheck)) {
                            for (Ability abilityCheck : roleCheck.getAbilities()) {
                                if (abilityCheck.toString().equals(message.getBody())) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * rename a group
     *
     * @param message the group you want to rename and the new name
     * @throws IOException
     */
    public void renameGroup(Message message) throws IOException {
        if (checkAbility(new Message(message.getSender(), "RenameGroup", message.getBody(), "check"))) {
            for (Group group : Data.getAllGroups()) {
                if (group.getGroupName().equals(message.getBody())) {
                    group.setGroupName(message.getReceiver());
                    outputStream.writeObject(new Message("Server", "Group Renamed", message.getSender(), "inform"));
                }
            }
        } else {
            outputStream.writeObject(new Message("Server", "You do not have permission to rename this group", message.getSender(), "inform"));
        }
    }

    /**
     * delete a user from a group
     *
     * @param message the group you want to delete the user from and the user you want to delete
     * @throws IOException
     */
    public void removeUser(Message message) throws IOException {
        for (Group group : Data.getAllGroups()) {
            if (group.getGroupName().equals(message.getReceiver())) {
                //delete user from group using iterator
                Client deleteUser = null;
                for (Client client : group.getMembers().keySet()) {
                    if (client.getUsername().equals(message.getBody())) {
                        deleteUser = client;
                    }
                }
                if (deleteUser != null) {
                    outputStream.writeObject(new Message("Server", "User Removed", message.getSender(), "inform"));
                    group.getMembers().remove(deleteUser);
                } else {
                    outputStream.writeObject(new Message("Server", "User Not Found", message.getSender(), "warning"));
                }
            }
        }
    }

    /**
     * ban a user from channel
     *
     * @param message the channel and group you want to ban from and username of banned person
     * @throws IOException
     */
    public void banUser(Message message) throws IOException {
        for (Group group : Data.getAllGroups()) {
            if (group.getGroupName().equals(message.getReceiver().split("-")[0])) {
                for (Channel channel : group.getChannels()) {
                    if (channel.getName().equals(message.getReceiver().split("-")[1])) {
                        for (Client client : group.getMembers().keySet()) {
                            if (client.getUsername().equals(message.getBody())) {
                                channel.getBannedUsers().add(client);
                                outputStream.writeObject(new Message("Server", "User Banned", message.getSender(), "inform"));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * show users that can be banned from a channel
     *
     * @param message the channel you want to see banned users from
     * @throws IOException
     */
    public void showBannableUsers(Message message) throws IOException {
        for (Group group : Data.getAllGroups()) {
            if (group.getGroupName().equals(message.getReceiver())) {
                for (Channel channel : group.getChannels()) {
                    if (channel.getName().equals(message.getBody())) {
                        for (Client client : group.getMembers().keySet()) {
                            Role creator = null;
                            for (Role role : group.getRoles()) {
                                if (role.getName().equals("creator")) {

                                    creator = role;
                                    break;
                                }
                            }
                            if (!group.getMembers().get(client).contains(creator) && !channel.getBannedUsers().contains(client)) {
                                System.out.println(client.getUsername());
                                outputStream.writeObject(new Message("Server", client.getUsername(), message.getSender(), "showBannableUsersResponse"));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * send notification to banned users
     *
     * @param userName the user you want to send notification to
     * @param message  the message you want to send
     * @throws IOException
     */
    public void tagUser(String userName, Message message) throws IOException {
        for (ClientHandler clientHandler : Server.getClients()) {
            if (clientHandler.getClient().getUsername().equals(userName)) {
                clientHandler.getOutputStream().writeObject(new Message(message.getSender(), message.getBody(), message.getReceiver(), "notification"));
            }
        }
    }

    /**
     *
     * @param profileSocket
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void changeProfilePicture(Socket profileSocket) throws IOException, ClassNotFoundException {
        System.out.println("Profile Picture Change Requested");
        //OutputStream profile = new FileOutputStream(client.getUsername() + ".jpg");
//        byte[] bytes = new byte[16 * 1024];
//        int count;
//        while ((count = inputStream.read(bytes)) > 0) {
//            profile.write(bytes, 0, count);
//        }
//        profile.close();
    }

    /**
     * show channels that a user can be banned from
     * @param message the user you want to see banned channels from
     * @throws IOException
     */
    public void showBannableChannels(Message message) throws IOException {
        for (Group group : Data.getAllGroups()) {
            if (group.getGroupName().equals(message.getReceiver())) {
                for (Channel channel : group.getChannels()) {
                    for (Client clientCheck : group.getMembers().keySet()) {
                        if (clientCheck.getUsername().equals(message.getBody())) {
                            if (!channel.getBannedUsers().contains(clientCheck)) {
                                outputStream.writeObject(new Message("Server", channel.getName(), message.getSender(), "showBannableChannelsResponse"));
                            }
                        }
                    }
                }
            }
        }
    }
}
