package interpreter;

import interpreter.controller.Controller;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import interpreter.view.ExitCommand;
import interpreter.view.RunExample;
import interpreter.view.TextMenu;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<IStmt> programs = List.of(
                Examples.EX1,
                Examples.EX2,
                Examples.EX3,
                Examples.EX4,
                Examples.EX5
        );

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "Exit"));

        for (int i = 0; i < programs.size(); i++) {
            IStmt program = programs.get(i);
            var repo = new Repository(PrgState.of(program), "log" + (i + 1) + ".txt");
            var controller = new Controller(repo);
            controller.setDisplayFlag(true);

            menu.addCommand(new RunExample(
                    String.valueOf(i + 1),
                    program.toString(),
                    controller
            ));
        }

        menu.show();
    }
}