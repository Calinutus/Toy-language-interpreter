package interpreter.model.exp;


import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

public record ReadHeapExp(IExp exp) implements  IExp{
    @Override
    public IValue eval(IDictionary<String, IValue> symTable, IHeap heap) throws MyException {
        IValue val = exp.eval(symTable, heap);
        if (!(val instanceof RefValue refVal)) {
            throw new MyException("ReadHeap: expression " + exp + " is not a RefValue");
        }
        int address = refVal.address();
        if(!heap.isDefined(address)) {
            throw new MyException("ReadHeap: address " + address + " is not defined in the heap");
        }
        return heap.get(address);
    }

    @Override
    public String toString(){return "rH(" + exp + ")";}
}
