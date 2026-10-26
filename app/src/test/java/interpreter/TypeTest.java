package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import interpreter.model.type.BoolType;
import interpreter.model.type.IType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
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

    @Test
    void intTypeDefaultValueIsFalse(){
        IType boolType = new BoolType();
        IValue defaultValue = boolType.defaultValue();
        assertEquals(new BoolValue(false) , defaultValue);
    }

    @Test
    void typeToStringReturnsCorrect(){
        IType intType = new IntType();
        IType boolType = new BoolType();

        assertEquals("int" , intType.toString());
        assertEquals("bool" , boolType.toString());
    }


}
