package ro.ubb.socket.common.service;

import java.io.*;

public class Message {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String OK = "ok";
    public static final String ERROR = "error";

    private String header;
    private Object body;

    public Message() {
    }

    public Message(String header, Object body) {
        this.header = header;
        this.body = body;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Message{" +
                "header='" + header + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

    public void readFrom(ObjectInputStream is) throws IOException {
        try {
            header = (String) is.readObject();
            body = is.readObject();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void writeTo(ObjectOutputStream os) throws IOException {
        os.writeObject(header);
        os.writeObject(body);
        os.flush();
    }
}
