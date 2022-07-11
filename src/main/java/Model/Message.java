package Model;

import Client.Client;

import java.io.Serializable;

/**
 * This class is used for storing the message object
 * The message object can either be a request or a response
 * The message object can be a private message or a chat history
 */
public class Message implements Serializable {
    private final String sender;
    private final String body;
    private final String receiver;
    private final String type;
    /**Constructor for the message*/
    public Message(String sender, String body, String receiver, String type) {
        this.sender = sender;
        this.body = body;
        this.receiver = receiver;
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getBody() {
        return body;
    }

    public String getType() {
        return type;
    }
    
}
