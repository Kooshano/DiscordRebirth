package Client;

import Model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientMessageReceiver implements Runnable {
    private boolean flag = true;
    private boolean status;
    private boolean sign = false;
    private boolean ability = true;
    private boolean channelChat = false;
    private boolean members = false;
    private ObjectInputStream in;
    private String currentChat;
    private Client client;
    public ClientMessageReceiver(ObjectInputStream in, String currentChat) {
        this.in = in;
        this.currentChat = currentChat;
    }
    @Override
    public void run() {
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                Object inp = in.readObject();
                if(inp instanceof Message) {
                    Message message = (Message) inp;
                    if (message.getType().equals("private")) {
                        //if we are in the same chat
                        if (message.getSender().equals(currentChat)) {
                            System.out.println(message.getSender() + ": " + message.getBody());
                        }
                    } else if (message.getType().equals("friendRequestHistoryResponse")) {
                        System.out.println(message.getSender());

                    } else if (message.getType().equals("friendRequestResponse")) {
                        if (message.getBody().equals("yes")) {
                            System.out.println("You are now friends with " + message.getSender());
                            //Message decision = new Message(message.getSender(), answer, message.getSender(), "Approve");
                        } else if (message.getBody().equals("no")) {
                            System.out.println(message.getSender() + " have rejected your friend request");
                            //Message decision = new Message(message.getSender(), answer, message.getSender(), "Decline");
                        }
                    } else if (message.getType().equals("friendListResponse")) {
                        SavedData.addToFriends(message.getSender() + " " + message.getBody());
                    } else if (message.getType().equals("historyResponse")) {
                        System.out.println(message.getSender() + ": " + message.getBody());
                    } else if (message.getType().equals("showGroupsResponse")) {
                        System.out.println(message.getBody());
                    } else if (message.getType().equals("warning")) {
                        System.out.println(message.getBody());
                    } else if (message.getType().equals("terminate")) {
                        if (message.getBody() != null) {
                            System.out.println(message.getBody());
                        }
                        System.out.println("Connection closed");
                        //throw new exception for closing the socket
                        flag = false;
                        break;
                    } else if (message.getType().equals("signInGroupResponse")) {
                        if (message.getBody().equals("True")) {
                            status = true;
                            System.out.println("""
                                    1- Chat in Channels
                                    2- Use your abilities""");
                        }
                        if (message.getBody().equals("False")) {
                            System.out.println("Wrong input");
                        }
                    } else if (message.getType().equals("showAbilitiesResponse")) {
                        System.out.println(message.getBody());
                        status = true;
                    } else if (message.getType().equals("signUpResponse")) {
                        if (message.getBody().equals("true")) {
                            sign = true;
                        }
                    } else if (message.getType().equals("signInResponse")) {
                        if (message.getBody().equals("true")) {
                            sign = true;
                        }
                    } else if (message.getType().equals("inform")) {
                        if (message.getBody().equals("This User Already Exists")) {
                            System.out.println(message.getBody());
                        } else if (message.getBody().equals("Role Matched to User") || message.getBody().equals("Added To Role") || message.getBody().equals("User Added to Group Successfully") || message.getBody().equals("Group Renamed") || message.getBody().equals("Channel Deleted") || message.getBody().equals("Group Deleted") || message.getBody().equals("User Removed") || message.getBody().equals("User Banned")) {
                            System.out.println(message.getBody());
                            ability = false;
                        }
                    } else if (message.getType().equals("showGivableAbilitiesResponse")) {
                        System.out.println(message.getBody());
                    } else if (message.getType().equals("showChannelsResponse")) {
                        System.out.println(message.getBody());
                    } else if (message.getType().equals("signInChannelResponse")) {
                        if (message.getBody().equals("True")) {
                            System.out.println("You Entered channel");
                            channelChat = true;

                        } else if (message.getBody().equals("False")) {
                            System.out.println("Wrong Input");
                        }

                    } else if (message.getType().equals("channelChat")) {
                        if (message.getReceiver().equals(currentChat)) {
                            System.out.println(message.getSender() + ": " + message.getBody());
                        }
                    } else if (message.getType().equals("showGroupMembersResponse")) {
                        System.out.println(message.getBody());
                    } else if (message.getType().equals("showRolesResponse")) {
                        System.out.println(message.getBody());
                    } else if (message.getType().equals("showBannableUsersResponse")) {
                        System.out.println(message.getBody());
                    } else if (message.getType().equals("notification")) {
                        System.out.println(message.getSender() + " : " + message.getBody() + " " + message.getReceiver());
                    } else if (message.getType().equals("showBannableChannelsResponse")) {
                        System.out.println(message.getBody());
                    }
                } else if (inp instanceof Client) {
                    client = (Client) inp;
                }

            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        }
    }
    public boolean isFlag() {
        return flag;
    }

    public void setCurrentChat(String currentChat) {
        this.currentChat = currentChat;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isSign(){
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public boolean isAbility() {
        return ability;
    }

    public void setAbility(boolean ability) {
        this.ability = ability;
    }

    public boolean isChannelChat() {
        return channelChat;
    }

    public void setChannelChat(boolean channelChat) {
        this.channelChat = channelChat;
    }

    public Client getClient() {
        return client;
    }
}