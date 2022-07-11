package Client;

import java.io.Serializable;
import java.util.ArrayList;

public class Client implements Serializable {
    private String email;
    private String password;
    private String username;
    private String phone;
    private String state = "Online";
    private String currentState = "Online";
    private ArrayList<Client> friends = new ArrayList<Client>();
    private ArrayList<Client> blocked = new ArrayList<Client>();

    /**the phone number should contain 8 digits starting with 09
     @param phone the number to check
     @return true if the phone number is valid
     */
    public static boolean checkPhoneNumber(String phone) {
        if(phone==null){
            return true;
        }
        if (phone.length() != 11) {
            return false;
        }
        if (!phone.startsWith("09")) {
            return false;
        }
        return true;
    }
    /**the username should contain at least 6 characters
     * @param username the username to check
     * @return true if the username is valid, false otherwise
     */
    public static boolean checkUsername(String username) {
        if (username.length() < 6) {
            return false;
        }
        return true;
    }
    /**the password should contain at least 8 characters and contain uppercase letters lowercase letters and numbers
     * @param  password given password
     * @return true if the password is valid
     */
    boolean checkPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        if (!password.matches(".*[0-9].*")) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param email the email to check
     * @return true if the email is valid
     */
    public static boolean checkMail (String email) {
        if (email.length() < 5) {
            return false;
        }
        if (!email.contains("@")) {
            return false;
        }
        return true;
    }

    public Client(String email, String password, String username, String phone) {
        if(!checkMail(email)){
            throw new IllegalArgumentException("Invalid email");
        }
        if(!checkPassword(password)){
            throw new IllegalArgumentException("Invalid password");
        }
        if(!checkPhoneNumber(phone)){
            throw new IllegalArgumentException("Invalid phone number");
        }
        if(!checkUsername(username)){
            throw new IllegalArgumentException("Invalid username");
        }
        this.email = email;
        this.password = password;
        this.username = username;
        this.phone = phone;
    }
    /** to String method
     * @return the client's information in string format
     * */
    public String toString() {
        return "Client{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void addFriend(Client client){
        friends.add(client);
    }

    public void block(Client client){
        blocked.add(client);
    }

    public boolean isBlocked(String id){
        for(Client client:blocked){
            if(client.getUsername().equals(id)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Client> getFriends() {
        return friends;
    }

    public String getState() {
        return state;
    }

    public void blockUser(Client blocked) {
        this.blocked.add(blocked);
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getCurrentState() {
        return currentState;
    }
}
