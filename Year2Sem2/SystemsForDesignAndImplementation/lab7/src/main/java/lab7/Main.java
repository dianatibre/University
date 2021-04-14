package lab7;

import lab7.ui.Console;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext("lab7.config");
        Console console=context.getBean(Console.class);
        console.runConsole();
    }
}
