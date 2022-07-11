package Model;

import Client.*;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Channel implements Serializable {
    String name;
    ArrayList<Client> bannedUsers = new ArrayList<Client>();

    public Channel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Client> getBannedUsers() {
        return bannedUsers;
    }
}