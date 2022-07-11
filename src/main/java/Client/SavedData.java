package Client;

public class SavedData {
    private ClientApp clientApp;
    private Client client;

    public SavedData(ClientApp clientApp) {
        this.clientApp = clientApp;
        this.client = clientApp.getClient();
    }
}
