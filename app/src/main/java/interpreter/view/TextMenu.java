package interpreter.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextMenu {
    private Map<String, Command> commands;

    public TextMenu() {
        this.commands = new HashMap<>();
    }

    public void addCommand(Command c) {
        commands.put(c.getKey(), c);
    }

    public void show() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- TOY LANGUAGE MENU ---");
            for (Command com : commands.values()) {
                System.out.println(com.getKey() + ". " + com.getDescription());
            }
            System.out.print("Choose option: ");
            String key = scanner.nextLine().trim();

            Command com = commands.get(key);
            if (com == null) {
                System.out.println("Invalid option! Try again.");
                continue;
            }
            com.execute();
        }
    }
}