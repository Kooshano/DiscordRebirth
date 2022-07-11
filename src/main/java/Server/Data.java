package Server;

import Client.Client;
import Model.Group;
import Model.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Data {

    private static ArrayList<Message> allMessages = returnData();
    private static ArrayList<Client> allUsers = returnUsers();
    private static ArrayList<Group> allGroups = returnGroups();
    public static ArrayList<Message> returnData(){
        try {
            FileInputStream fIn = new FileInputStream("Messages.bin");
            ObjectInputStream in = new ObjectInputStream(fIn);
            ArrayList<Message> check = new ArrayList<>();
            check = (ArrayList<Message>) in.readObject();
            if (check != null) {
                allMessages = check;
            }
            fIn.close();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(allMessages == null){
            allMessages = new ArrayList<>();
        }
        return allMessages;
    }
    public static ArrayList<Group> returnGroups(){
        try {
            FileInputStream fIn = new FileInputStream("Groups.bin");
            ObjectInputStream in = new ObjectInputStream(fIn);
            ArrayList<Group> check = new ArrayList<>();
            check = (ArrayList<Group>) in.readObject();
            if (check != null) {
                allGroups = check;
            }
            fIn.close();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(allGroups == null){
            allGroups = new ArrayList<>();
        }
        return allGroups;
    }
    public static ArrayList<Client> returnUsers(){
        try {
            FileInputStream fIn = new FileInputStream("Users.bin");
            ObjectInputStream in = new ObjectInputStream(fIn);
            ArrayList<Client> check = new ArrayList<>();
            check = (ArrayList<Client>) in.readObject();
            if (check != null) {
                allUsers = check;
            }
            fIn.close();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(allUsers==null){
            allUsers = new ArrayList<>();
        }
        return allUsers;
    }

    public static void saveData(){
        try {
            FileOutputStream fOut = new FileOutputStream("Messages.bin");
            ObjectOutputStream out = new ObjectOutputStream(fOut);
            out.writeObject(allMessages);
            fOut.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveGroups(){
        try {
            FileOutputStream fOut = new FileOutputStream("Groups.bin");
            ObjectOutputStream out = new ObjectOutputStream(fOut);
            out.writeObject(allGroups);
            fOut.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveUsers(){
        try {
            FileOutputStream fOut = new FileOutputStream("Users.bin");
            ObjectOutputStream out = new ObjectOutputStream(fOut);
            out.writeObject(allUsers);
            fOut.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Client> getAllUsers() {
        return allUsers;
    }
    public static ArrayList<Message> getAllMessages() {
        return allMessages;
    }

    /**
     * Adds a message to the list of messages
     * @param message the message to be added
     */
    public  static void addMessage(Message message) {
        allMessages.add(message);
    }

    /**
     * Adds a user to the list of users
     * @param client the user to be added
     */
    public static void addUser(Client client){
        allUsers.add(client);
    }

    public static void removeMessage(Message message){
        allMessages.remove(message);
    }

    public static void removeFriendRequest(Message message) {
        //iterate through all messages and remove the one that matches the message
        allMessages.removeIf(current -> Objects.equals(current.getSender(), message.getSender()) && Objects.equals(current.getReceiver(), message.getReceiver()) && Objects.equals(current.getType(), "friendRequest") || (Objects.equals(current.getReceiver(), message.getSender()) && Objects.equals(current.getSender(), message.getReceiver()) && Objects.equals(current.getType(), "friendRequest")));
    }

    /**
     * create a new group
     * @param creator the creator of the group
     * @param name the name of the group
     */
    public static void createGroup(Client creator,String name){
        Group group = new Group(name,creator);
        allGroups.add(group);
        group.createChannel("Lobby","text");
    }

    public static ArrayList<Group> getAllGroups() {
        return allGroups;
    }

    /**
     * remove a group
     * @param group the group to remove
     */
    public static void removeGroup(String group) {
        allGroups.removeIf(current -> current.getGroupName().equals(group));
    }
}
