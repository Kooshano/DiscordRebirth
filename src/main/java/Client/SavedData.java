package Client;

import java.util.ArrayList;

public class SavedData {
    private static ClientApp clientApp;
    private static Client client;
    private static ArrayList<String> friends = new ArrayList<>();
    private static String friendRequestResponse;
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
}
