package Client;

import Model.Message;
import Model.SendFileThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class ClientApp {
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Client client;
    private Scanner input = new Scanner(System.in);
    private ClientMessageReceiver clientMessageReceiver;
    private Thread clientReceiverThread;
    private Socket socket;

    public ClientApp() throws IOException {
        //connect to the server using socket
        socket = new Socket("localhost", 8080);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        //outputStream.writeObject(client);
        inputStream = new ObjectInputStream(socket.getInputStream());
        clientMessageReceiver = new ClientMessageReceiver(inputStream, null);
        clientReceiverThread = new Thread(clientMessageReceiver);
        clientReceiverThread.start();
    }
    public void Client() throws IOException, InterruptedException {

        while (true) {
            //print the main menu in app
            System.out.println(menu());
            String inp = input.nextLine();
            if (!(clientMessageReceiver.isFlag())) {
                break;
            }
            // search in all chats and show chats with the same sender and receiver or vice versa
            if (inp.equals("1")) {

            }
            //send a friend request to another client
            else if (inp.equals("2")) {
                System.out.println("Enter receiver name");
                String receiver = input.nextLine();
                ClientMessageSender clientMessageSender = new ClientMessageSender(outputStream, client.getUsername(), receiver, null, "friendRequest");
                Thread clientSenderThread = new Thread(clientMessageSender);
                clientSenderThread.start();
            }
            //see received friend requests and answer them
            else if (inp.equals("3")) {
                ClientMessageSender clientMessageSender = new ClientMessageSender(outputStream, client.getUsername(), null, null, "friendRequestHistory");
                Thread clientSenderThread = new Thread(clientMessageSender);
                clientSenderThread.start();
                try {
                    clientSenderThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //see a list of friends and their state
            else if (inp.equals("4")) {
                ClientMessageSender clientMessageSender = new ClientMessageSender(outputStream, client.getUsername(), null, null, "requestFriendList");
                Thread clientSenderThread = new Thread(clientMessageSender);
                clientSenderThread.start();
                try {
                    clientSenderThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //change the user state
            else if (inp.equals("5")) {
                ClientMessageSender clientMessageSender = new ClientMessageSender(outputStream, client.getUsername(), null, null, "changeStateRequest");
                Thread clientSenderThread = new Thread(clientMessageSender);
                clientSenderThread.start();
                try {
                    clientSenderThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //change picture of profile
            else if (inp.equals("6")) {
                System.out.println("Enter file's address");
                String fileAddress = input.nextLine();
                SendFileThread fileThread = new SendFileThread(socket, fileAddress);
                ClientMessageSender clientMessageSender = new ClientMessageSender(outputStream, client.getUsername(), null, null, "changeProfileRequest");
                Thread clientSenderThread = new Thread(clientMessageSender);
                clientSenderThread.start();
                try {
                    clientSenderThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fileThread.start();
                try {
                    fileThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //block another client
            else if (inp.equals("7")) {
                System.out.println("Enter receiver name");
                String receiver = input.nextLine();
                ClientMessageSender clientMessageSender = new ClientMessageSender(outputStream, client.getUsername(), receiver, null, "blockRequest");
                Thread clientSenderThread = new Thread(clientMessageSender);
                clientSenderThread.start();
            }
            //creating a new group
            else if (inp.equals("8")) {
                System.out.println("Enter Group name");
                String receiver = input.nextLine();
                ClientMessageSender clientMessageSender = new ClientMessageSender(outputStream, client.getUsername(), receiver, null, "createGroupRequest");
                Thread clientSenderThread = new Thread(clientMessageSender);
                clientSenderThread.start();
            }
            //see a list of groups and enter one of them
            else if (inp.equals("9")) {
                ClientMessageSender clientMessageSender = new ClientMessageSender(outputStream, client.getUsername(), null, null, "showGroupsRequest");
                Thread clientSenderThread = new Thread(clientMessageSender);
                clientSenderThread.start();
                try {
                    clientSenderThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                clientSenderThread.interrupt();

                while (true) {
                    boolean flag = false; //this boolean will be used to ask server to show groups name another time
                    System.out.println("Enter name of a group");
                    String groupName = input.nextLine();
                    clientMessageReceiver.setCurrentChat(groupName);
                    if (Objects.equals(groupName, "exit")) {
                        break;
                    }
                    ClientMessageSender clientMessageSender2 = new ClientMessageSender(outputStream, client.getUsername(), groupName, null, "signInGroupRequest");
                    Thread clientSenderThread2 = new Thread(clientMessageSender2);
                    clientSenderThread2.start();
                    try {
                        clientSenderThread2.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    clientSenderThread2.interrupt();

                    Thread.sleep(500);
                    boolean flag2 = false;
                    while (clientMessageReceiver.isStatus()) {
                        flag = true;
                        String whichOne = input.nextLine();
                        if (whichOne.equals("2") || whichOne.equals("exit")) {
                            clientMessageReceiver.setStatus(false);
                        }
                        if (whichOne.equals("1")) {
                            ClientMessageSender clientMessageSender3 = new ClientMessageSender(outputStream, client.getUsername(), groupName, null, "showChannelsRequest");
                            Thread clientSenderThread3 = new Thread(clientMessageSender3);
                            clientSenderThread3.start();
                            while (true) {
                                System.out.println("Enter channel's name");
                                String channelName = input.nextLine();
                                if (channelName.equals("exit")) {
                                    flag2 = true;
                                    break;
                                } else {
                                    clientMessageReceiver.setCurrentChat(groupName + "-" + channelName);
                                    ClientMessageSender clientMessageSender4 = new ClientMessageSender(outputStream, client.getUsername(), channelName, groupName, "signInChannelRequest");
                                    Thread clientSenderThread4 = new Thread(clientMessageSender4);
                                    clientSenderThread4.start();
                                    Thread.sleep(500);
                                    if (clientMessageReceiver.isChannelChat()) {
                                        ClientMessageSender clientMessageSender5 = new ClientMessageSender(outputStream, client.getUsername(), channelName, groupName, "channelChat");
                                        Thread clientSenderThread5 = new Thread(clientMessageSender5);
                                        clientSenderThread5.start();
                                        try {
                                            clientSenderThread5.join();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        clientMessageReceiver.setCurrentChat(null);
                                    }
                                }
                                ClientMessageSender clientMessageSenderChannel = new ClientMessageSender(outputStream, client.getUsername(), groupName, null, "showChannelsRequest");
                                Thread clientSenderThreadChannel = new Thread(clientMessageSenderChannel);
                                clientSenderThreadChannel.start();
                            }
                            ClientMessageSender clientMessageSenderChat = new ClientMessageSender(outputStream, client.getUsername(), groupName, null, "signInGroupRequest");
                            Thread clientSenderThreadChat = new Thread(clientMessageSenderChat);
                            clientSenderThreadChat.start();
                        } else if (whichOne.equals("2")) {
                            ClientMessageSender clientMessageSender3 = new ClientMessageSender(outputStream, client.getUsername(), groupName, null, "showAbilitiesRequest");
                            Thread clientSenderThread3 = new Thread(clientMessageSender3);
                            clientSenderThread3.start();
                            try {
                                clientSenderThread3.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            clientSenderThread3.interrupt();
                            Thread.sleep(500);
                            while (clientMessageReceiver.isAbility()) {
                                flag2 = false;
                                String chooseAbility = input.nextLine();
                                if (chooseAbility.equals("AddUser")) {
                                    System.out.println("Enter username of the user you want to add");
                                    String newUser = input.nextLine();
                                    if (newUser.equals("exit")) {
                                        flag2 = true;
                                        break;
                                    }
                                    ClientMessageSender clientMessageSender4 = new ClientMessageSender(outputStream, client.getUsername(), groupName, newUser, "addUserToGroupRequest");
                                    Thread clientSenderThread4 = new Thread(clientMessageSender4);
                                    clientSenderThread4.start();
                                    Thread.sleep(500);
                                    if (!clientMessageReceiver.isAbility()) {
                                        flag2 = true;
                                    }
                                } else if (chooseAbility.equals("CreateRole")) {
                                    System.out.println("Enter role's name");
                                    String role = input.nextLine();
                                    ClientMessageSender clientMessageSender40 = new ClientMessageSender(outputStream, client.getUsername(), role, groupName, "createRoleRequest");
                                    Thread clientSenderThread40 = new Thread(clientMessageSender40);
                                    clientSenderThread40.start();
                                    clientSenderThread40.join();
                                    ClientMessageSender clientMessageSender41 = new ClientMessageSender(outputStream, null, null, null, "showGivableAbilitiesRequest");
                                    Thread clientSenderThread41 = new Thread(clientMessageSender41);
                                    clientSenderThread41.start();
                                    clientSenderThread41.join();
                                    String ability = "";
                                    while (!ability.equals("finish")) {
                                        ability = input.nextLine();
                                        if (ability.equals("finish")) {
                                            flag2 = true;
                                            break;
                                        } else {
                                            ClientMessageSender clientMessageSender5 = new ClientMessageSender(outputStream, client.getUsername(), role + "-" + ability, groupName, "addAbilityToRoleRequest");
                                            Thread clientSenderThread5 = new Thread(clientMessageSender5);
                                            clientSenderThread5.start();
                                            clientSenderThread5.interrupt();
                                        }
                                    }

                                } else if (chooseAbility.equals("CreateChannel")) {
                                    System.out.println("Enter channel's name");
                                    String channelName = input.nextLine();
                                    System.out.println("Enter channel's type");
                                    String channelType = input.nextLine();
                                    ClientMessageSender clientMessageSender4 = new ClientMessageSender(outputStream, client.getUsername(), channelName + "-" + channelType, groupName, "createChannelRequest");
                                    Thread clientSenderThread4 = new Thread(clientMessageSender4);
                                    clientSenderThread4.start();
                                    Thread.sleep(500);
                                    if (!clientMessageReceiver.isAbility()) {
                                        flag2 = true;
                                    }
                                } else if (chooseAbility.equals("DeleteChannel")) {
                                    System.out.println("Enter channel's name");
                                    String channelName = input.nextLine();
                                    ClientMessageSender clientMessageSender4 = new ClientMessageSender(outputStream, client.getUsername(), channelName, groupName, "deleteChannelRequest");
                                    Thread clientSenderThread4 = new Thread(clientMessageSender4);
                                    clientSenderThread4.start();
                                    Thread.sleep(500);
                                    if (!clientMessageReceiver.isAbility()) {
                                        flag2 = true;
                                    }
                                } else if (chooseAbility.equals("DeleteGroup")) {
                                    ClientMessageSender clientMessageSender4 = new ClientMessageSender(outputStream, client.getUsername(), null, groupName, "deleteGroupRequest");
                                    Thread clientSenderThread4 = new Thread(clientMessageSender4);
                                    clientSenderThread4.start();
                                    clientMessageReceiver.setStatus(false);
                                    Thread.sleep(600);
                                } else if (chooseAbility.equals("RemoveUser")) {
                                    ClientMessageSender clientMessageSender4 = new ClientMessageSender(outputStream, client.getUsername(), null, groupName, "showGroupMembersRequest");
                                    Thread clientSenderThread4 = new Thread(clientMessageSender4);
                                    clientSenderThread4.start();
                                    while (true) {
                                        System.out.println("Enter a username");
                                        String username = input.nextLine();
                                        if (username.equals("exit")) {
                                            break;
                                        } else {
                                            ClientMessageSender clientMessageSender5 = new ClientMessageSender(outputStream, client.getUsername(), username, groupName, "removeUserRequest");
                                            Thread clientSenderThread5 = new Thread(clientMessageSender5);
                                            clientSenderThread5.start();
                                            Thread.sleep(500);
                                            if (!clientMessageReceiver.isAbility()) {
                                                flag2 = true;
                                                break;
                                            }
                                        }
                                    }
                                } else if (chooseAbility.equals("DefineRole")) {
                                    System.out.println("Choose a member");
                                    ClientMessageSender clientMessageSender4 = new ClientMessageSender(outputStream, client.getUsername(), null, groupName, "showGroupMembersRequest");
                                    Thread clientSenderThread4 = new Thread(clientMessageSender4);
                                    clientSenderThread4.start();
                                    try {
                                        clientSenderThread4.join();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    member:
                                    while (true) {
                                        String memberUsername = input.nextLine();
                                        if (memberUsername.equals("exit")) {
                                            flag2 = true;
                                            break;
                                        } else {
                                            ClientMessageSender clientMessageSender5 = new ClientMessageSender(outputStream, client.getUsername(), null, groupName, "showRolesRequest");
                                            Thread clientSenderThread5 = new Thread(clientMessageSender5);
                                            clientSenderThread5.start();
                                            try {
                                                clientSenderThread5.join();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        System.out.println("Choose a role");
                                        while (true) {
                                            String roleName = input.nextLine();
                                            if (roleName.equals("exit")) {
                                                flag2 = true;
                                                break member;
                                            } else {
                                                ClientMessageSender clientMessageSender6 = new ClientMessageSender(outputStream, client.getUsername(), memberUsername + "-" + roleName, groupName, "defineRoleRequest");
                                                Thread clientSenderThread6 = new Thread(clientMessageSender6);
                                                clientSenderThread6.start();
                                                try {
                                                    clientSenderThread6.join();
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            Thread.sleep(500);
                                            if (clientMessageReceiver.isAbility()) {
                                                System.out.println("Something went wrong. Try again.");
                                                break member;
                                            } else {
                                                flag2 = true;
                                                break member;
                                            }
                                        }
                                    }
                                } else if (chooseAbility.equals("RenameGroup")) {
                                    System.out.println("Enter new name");
                                    String newName = input.nextLine();
                                    if (newName.equals("exit")) {
                                    } else {
                                        ClientMessageSender clientMessageSender4 = new ClientMessageSender(outputStream, client.getUsername(), groupName, newName, "renameGroupRequest");
                                        Thread clientSenderThread4 = new Thread(clientMessageSender4);
                                        clientSenderThread4.start();
                                        groupName = newName;
                                    }
                                    Thread.sleep(500);
                                    if (!clientMessageReceiver.isAbility()) {
                                        flag2 = true;
                                    }
                                } else if (chooseAbility.equals("BanChannel")) {
                                    ClientMessageSender clientMessageSender4 = new ClientMessageSender(outputStream, client.getUsername(), groupName, null, "showChannelsRequest");
                                    Thread clientSenderThread4 = new Thread(clientMessageSender4);
                                    clientSenderThread4.start();
                                    channelBan:
                                    while (true) {
                                        System.out.println("Enter a channel name");
                                        String channelName = input.nextLine();
                                        if (channelName.equals("exit")) {
                                            break;
                                        }
                                        ClientMessageSender clientMessageSender5 = new ClientMessageSender(outputStream, client.getUsername(), channelName, groupName, "showBannableUsersRequest");
                                        Thread clientSenderThread5 = new Thread(clientMessageSender5);
                                        clientSenderThread5.start();
                                        while (true) {
                                            System.out.println("Enter a username");
                                            String username = input.nextLine();
                                            if (username.equals("finish")) {
                                                flag2 = true;
                                                break channelBan;
                                            } else {
                                                ClientMessageSender clientMessageSender6 = new ClientMessageSender(outputStream, client.getUsername(), username, groupName + "-" + channelName, "banUserRequest");
                                                Thread clientSenderThread6 = new Thread(clientMessageSender6);
                                                clientSenderThread6.start();
                                                Thread.sleep(500);
                                                if (!clientMessageReceiver.isAbility()) {
                                                    flag2 = true;
                                                }
                                            }
                                        }
                                    }
                                } else if (chooseAbility.equals("BanUser")) {
                                    ClientMessageSender clientMessageSender4 = new ClientMessageSender(outputStream, client.getUsername(), null, groupName, "showGroupMembersRequest");
                                    Thread clientSenderThread4 = new Thread(clientMessageSender4);
                                    clientSenderThread4.start();
                                    userBan:
                                    while (true) {
                                        System.out.println("Enter a user name");
                                        String username = input.nextLine();
                                        if (username.equals("exit")) {
                                            break;
                                        }
                                        ClientMessageSender clientMessageSender5 = new ClientMessageSender(outputStream, client.getUsername(), username, groupName, "showBannableChannelsRequest");
                                        Thread clientSenderThread5 = new Thread(clientMessageSender5);
                                        clientSenderThread5.start();
                                        while (true) {
                                            System.out.println("Enter a channel name");
                                            String channelName = input.nextLine();
                                            if (channelName.equals("finish")) {
                                                flag2 = true;
                                                break userBan;
                                            } else {
                                                ClientMessageSender clientMessageSender6 = new ClientMessageSender(outputStream, client.getUsername(), username, groupName + "-" + channelName, "banUserRequest");
                                                Thread clientSenderThread6 = new Thread(clientMessageSender6);
                                                clientSenderThread6.start();
                                                Thread.sleep(500);
                                                if (!clientMessageReceiver.isAbility()) {
                                                    flag2 = true;
                                                }
                                            }
                                        }
                                    }
                                } else if (chooseAbility.equals("exit")) {
                                    flag2 = true;
                                    break;
                                }
                                if (clientMessageReceiver.isAbility()) {
                                    ClientMessageSender clientMessageSenderChooseAbility = new ClientMessageSender(outputStream, client.getUsername(), groupName, null, "showAbilitiesRequest");
                                    Thread clientSenderThreadChooseAbility = new Thread(clientMessageSenderChooseAbility);
                                    clientSenderThreadChooseAbility.start();
                                    try {
                                        clientSenderThread3.join();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            clientMessageReceiver.setAbility(true);
                            if (flag2) {
                                flag2 = false;
                                ClientMessageSender clientMessageSenderAbility = new ClientMessageSender(outputStream, client.getUsername(), groupName, null, "signInGroupRequest");
                                Thread clientSenderThreadAbility = new Thread(clientMessageSenderAbility);
                                clientSenderThreadAbility.start();
                                clientSenderThreadAbility.join();
                            }
                        }
                    }
                    if (flag) {
                        System.out.println("Enter name of another group");
                        flag = false;
                        Thread clientSenderThreadGroup = new Thread(clientMessageSender);
                        clientSenderThreadGroup.start();
                    }
                }
            }
            //log out from account
        }
    }

    public static String menu() {
        return """
                1- Open a Chat
                2- Send a friend request
                3- See friend requests
                4- Show friends list
                5- Change your state
                6- Change your profile
                7- Block
                8- Create Group Request
                9- Show groups
                10- Change Password
                11- Log Out""";
    }

    public String signUp(String username, String password, String email, String phone) throws IOException, InterruptedException {
        Message message = new Message(null, null, null, "signUpRequest");
        outputStream.writeObject(message);
//            if (email.equals("exit")){
//                Client clientExit = new Client("username@mail.com","A123***321A","exitProcessRequest","09111111111");
//                outputStream.writeObject(clientExit);
//                return clientExit;
//            }
        if (phone != null && phone.length() == 0) {
            phone = null;
        }
        try {
            Client clientCheck = new Client(email, password, username, phone);
            outputStream.writeObject(clientCheck);
            Thread.sleep(500);
            if (clientMessageReceiver.isSign()) {
                client = clientCheck;
                clientMessageReceiver.setSign(false);
                ClientMessageSender clientMessageSender = new ClientMessageSender(outputStream, null, null, null, "logOutRequest");
                Thread clientSenderThread = new Thread(clientMessageSender);
                clientSenderThread.start();
                return "Sign Up Was Successful";
            }
        } catch (IllegalArgumentException e) {
            Client clientExit = new Client("username@mail.com", "A123*x*x*321A", "exitProcessRequest", "09111111111");
            outputStream.writeObject(clientExit);
            System.out.println(e.getMessage());
            return e.getMessage();
        }
        return "This Username Is Already Taken";
    }


    public String signIn(String username, String password) throws IOException, InterruptedException {
        while (true) {
            Message message = new Message(null, null, null, "signInRequest");
            outputStream.writeObject(message);
            try {
                Client clientCheck = new Client("username@mail.com", password, username, "09111111111");
                outputStream.writeObject(clientCheck);
                Thread.sleep(500);
                if (clientMessageReceiver.isSign()) {
                    client = clientMessageReceiver.getClient();
                    clientMessageReceiver.setSign(false);
                    return "Signed In Successfully";
                }
                else {
                    return "Username or password is invalid";
                }
            } catch (IllegalArgumentException e) {
                Client clientExit = new Client("username@mail.com", "A123*x*x*321A", "exitProcessRequest", "09111111111");
                outputStream.writeObject(clientExit);
                System.out.println(e.getMessage());
                return e.getMessage();
            }
        }
    }

    public void exit() throws IOException {
        Message message = new Message(null, null, null, "exitRequest");
        outputStream.writeObject(message);
    }
    public void changePassword(String newPassword){
        ClientMessageSender clientMessageSender = new ClientMessageSender(outputStream, client.getUsername(), newPassword, null, "changePasswordRequest");
        Thread clientSenderThread = new Thread(clientMessageSender);
        clientSenderThread.start();
    }

    public Client getClient() {
        return client;
    }
    public void openPrivateChat(String receiver){
        clientMessageReceiver.setCurrentChat(receiver);
    }
    public void sendPrivateChat(String receiver,String message){
        ClientMessageSender clientMessageSender = new ClientMessageSender(outputStream, receiver, client.getUsername(), message, "private");
        Thread clientSenderThread = new Thread(clientMessageSender);
        clientSenderThread.start();
        //wait for Threads to finish
        try {
            clientSenderThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //resetting current chat
    }
    public void closePrivateChat(String receiver){
        clientMessageReceiver.setCurrentChat(null);
    }
    public void logOut() throws InterruptedException {
        ClientMessageSender clientMessageSender = new ClientMessageSender(outputStream, null, null, null, "logOutRequest");
        Thread clientSenderThread = new Thread(clientMessageSender);
        clientSenderThread.start();
        Thread.sleep(500);
    }

}