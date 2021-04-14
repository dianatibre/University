package ro.ubb.socket.server.tcp;

import ro.ubb.socket.common.service.CommonServiceException;
import ro.ubb.socket.common.service.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.UnaryOperator;

public class TcpServer {
    private ExecutorService executorService;
    private int port;
    private Map<String, UnaryOperator<Message>> methodHandlers;

    public TcpServer(ExecutorService executorService, int port) {
        this.executorService = executorService;
        this.port = port;
        this.methodHandlers = new HashMap<>();

    }

    public void addHandler(String methodName, UnaryOperator<Message> handler) {
        methodHandlers.put(methodName, handler);
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            System.out.println("server started; waiting for clients...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("client connected");
                executorService.submit(new ClientHandler(clientSocket));
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new CommonServiceException("could not start server", e);
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                ObjectInputStream is = new ObjectInputStream(clientSocket.getInputStream());
                Message request = new Message();
                request.readFrom(is);
                System.out.println("server - received request: " + request);
                Message response = methodHandlers.get(request.getHeader()).apply(request);
                //System.out.println("server - computed response: " + response);
                ObjectOutputStream os = new ObjectOutputStream(clientSocket.getOutputStream());
                response.writeTo(os);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new CommonServiceException("server - data " +
                        "exchange" +
                        " " +
                        "error", e);
            } finally {
                if (clientSocket != null) {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}
