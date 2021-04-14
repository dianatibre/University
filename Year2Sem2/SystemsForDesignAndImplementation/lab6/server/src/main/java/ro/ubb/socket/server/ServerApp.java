package ro.ubb.socket.server;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ServerApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("ro.ubb.socket.server.config");
        System.out.println("server starting...");
    }
}
