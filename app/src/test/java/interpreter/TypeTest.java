package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import interpreter.model.type.*;
import interpreter.model.value.*;
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
    void stringTypeDefaultValueIsEmpty(){
        IType stringType = new StringType();
        IValue defaultValue = stringType.defaultValue();
        assertEquals(new StringValue("") , defaultValue);
    }

    @Test
    void typeToStringReturnsCorrect(){
        IType intType = new IntType();
        IType boolType = new BoolType();
        IType stringType = new StringType();

        assertEquals("int" , intType.toString());
        assertEquals("bool" , boolType.toString());
        assertEquals("string" , stringType.toString());
    }

    @Test
    void typeRefTypeDefaultValue(){
        IType intType = new IntType();
        RefType refType = new RefType(intType);

        IValue defaultValue = refType.defaultValue();

        assertEquals(new RefValue(0 , intType) , defaultValue);
        assertEquals(refType , defaultValue.getType());
    }

    @Test
    void nestedRefTypesEquality(){
        RefType simpleRef = new RefType(new IntType());
        RefType ref1 = new RefType(new RefType(new IntType()));
        RefType ref2 = new RefType(new RefType(new IntType()));

        assertEquals(ref1, ref2);
        assertNotEquals(ref1 , simpleRef);
    }
}
