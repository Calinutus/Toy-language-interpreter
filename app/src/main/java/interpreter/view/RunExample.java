package interpreter.view;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;

public class RunExample extends Command{
    private final Controller controller;
    private boolean executed = false;

    public RunExample(String key , String description , Controller controller){
        super(key, description);
        this.controller = controller;
    }

    @Override
    public void execute(){
        if(executed){
            System.out.println("This example has already been executed");
            return;
        }
        executed = true;
        try{
            controller.allStep();
        }catch (MyException e){
            System.out.println("[EXEC ERROR]" + e.getMessage());
        }catch (Exception e){
            System.out.println("[UNKNOWN ERROR]" + e.getMessage());
        }
    }
}
