package Client;

import Client.Client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Manager {
    ArrayList<Client> clients;
    Client currentUser;

    /**
     * sign up a new user
     *
     * @param username the username of the new user
     * @param password the password of the new user
     * @param email    the email of the new user
     * @param phone    the phone number of the new user
     * @return true if the user is successfully created, false otherwise
     */
    public String signUp(String username, String password, String email, String phone) {
        try {
            Client Client = new Client(username, password, email, phone);
            clients.add(Client);
            return "Sign up was Successful";
        } catch (IllegalArgumentException e) {
            return (e.getMessage());
        }
    }

    /**
     * sign in a user and set it as currentUser
     * @param username the username of the user
     * @param password the password of the user
     * return String that signed in successful of user not found
     */
    public String signIn(String username, String password) {
        for (Client client : clients) {
            if (client.getUsername().equals(username) && client.getPassword().equals(password)) {
                currentUser = client;
                return "Sign in was Successful";
            }
        }
        return "User not found";
    }


}
