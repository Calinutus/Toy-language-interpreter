package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import interpreter.model.type.BoolType;
import interpreter.model.type.IType;
import interpreter.model.type.IntType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

class TypeTest {

    @Test
    void intTypeDefaultValueIsZero(){
        IType intType = new IntType();
        IValue defaultValue = intType.defaultValue();
        assertEquals(new IntValue(0) , defaultValue);
    }

}
