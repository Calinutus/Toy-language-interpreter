package interpreter.view;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.repository.IRepository;
import interpreter.repository.Repository;

public class RunExample extends Command{
    private Controller controller;

    public RunExample(String key , String description , IStmt stmt){
        super(key , description);
        PrgState state = PrgState.of(stmt);
        IRepository repo =new Repository(state);
        this.controller = new Controller(repo);
        this.controller.setDisplayFlag(true);
    }

    @Override
    public void execute(){
        try{
            controller.allStep();
        }catch (MyException e){
            System.out.println("[EXEC ERROR]" + e.getMessage());
        }catch (Exception e){
            System.out.println("[UNKNOWN ERROR]" + e.getMessage());
        }
    }
}
