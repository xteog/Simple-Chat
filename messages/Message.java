package messages;

import java.io.Serializable;

public class Message implements Serializable {
    public final MessageType type;
    public final String author;
    public final String message;

    /**
     * Constructs a generic message.
     *
     * @param type    type of the message.
     * @param author  author of the message.
     * @param message The message to send.
     */
    public Message(MessageType type, String author, String message) {
        this.type = type;
        this.author = author;
        this.message = message;
    }

}
