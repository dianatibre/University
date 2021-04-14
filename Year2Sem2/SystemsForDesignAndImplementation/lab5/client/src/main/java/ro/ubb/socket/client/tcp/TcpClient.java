package ro.ubb.socket.client.tcp;

import ro.ubb.socket.common.service.CommonServiceException;
import ro.ubb.socket.common.service.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TcpClient {
    private String host;
    private int port;

    public TcpClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Message sendAndReceive(Message request) {
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());

            System.out.println(request.getBody().toString() + request.getHeader());
            request.writeTo(os);
            System.out.println("client - sent request: " + request);

            ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
            Message response = new Message();
            response.readFrom(is);
            System.out.println("client - received response: " + response);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommonServiceException("client - exception connecting to" +
                    " server", e);
        }
    }
}
