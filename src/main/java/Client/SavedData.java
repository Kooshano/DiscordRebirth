package Client;

import Model.Message;

import java.util.ArrayList;

public class SavedData {
    private static ClientApp clientApp;
    private static Client client;
    private static ArrayList<String> friends = new ArrayList<>();
    private static String friendRequestResponse;
    private static ArrayList<String> friendRequests = new ArrayList<>();
    private static ArrayList<Message> currentChatMessages = new ArrayList<>();
    private static ArrayList<Message> notifications = new ArrayList<>();

    public SavedData(ClientApp clientApp) {
        SavedData.clientApp = clientApp;
        SavedData.client = clientApp.getClient();
    }

    public static Client getClient() {
        return client;
    }

    public static ClientApp getClientApp() {
        return clientApp;
    }

    public static void setClient(Client client) {
        SavedData.client = client;
    }

    public static void setClientApp(ClientApp clientApp) {
        SavedData.clientApp = clientApp;
    }

    public static void addToFriends(String friend) {
        friends.add(friend);
    }

    public static void clearFriends() {
        friends.clear();
    }

    public static void setFriendRequestResponse(String friendRequestResponse) {
        SavedData.friendRequestResponse = friendRequestResponse;
    }

    public static String getFriendRequestResponse() {
        return friendRequestResponse;
    }

    public static ArrayList<String> getFriends() {
        return friends;
    }

    public static void addToFriendRequest(String friendRequest) {
        friendRequests.add(friendRequest);
    }

    public static ArrayList<String> getFriendRequests() {
        return friendRequests;
    }

    public static void clearFriendsRequest() {
        friendRequests.clear();
    }

    public static void addToCurrentChatMessages(Message message) {
        currentChatMessages.add(message);
    }

    public static ArrayList<Message> getCurrentChatMessages() {
        return currentChatMessages;
    }

    public static void clearCurrentChatMessages() {
        currentChatMessages.clear();
    }

    public static void addToNotifications(Message notification) {
        notifications.add(notification);
    }

    public static ArrayList<Message> getNotifications() {
        return notifications;
    }
}
