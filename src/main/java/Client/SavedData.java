package Client;

public class SavedData {
    private static ClientApp clientApp;
    private static Client client;

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
}
