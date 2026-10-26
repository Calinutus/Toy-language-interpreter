package interpreter;

import interpreter.exceptions.ExpressionException;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.IDictionary;
import interpreter.model.exp.*;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.IValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpTest {

    @Test
    void testValueExp() throws ExpressionException {
        IExp exp = new ValueExp(new IntValue(10));
        IDictionary<String, IValue> symTable = new MyDictionary<>();
        IValue val = exp.eval(symTable);
        assertEquals(new IntValue(10), val);
    }

    @Test
    void testVarExp() throws ExpressionException {
        IExp exp = new VarExp("v");
        IDictionary<String, IValue> symTable = new MyDictionary<>();

        symTable.put("v", new IntValue(20));
        IValue val1 = exp.eval(symTable);
        assertEquals(new IntValue(20), val1);

        IValue val2 = exp.eval(symTable);
        assertEquals(new IntValue(20), val2);

        assertEquals(val1 , val2);
    }

    @Test
    void testArithExp() throws ExpressionException {
        // (2 + 3) * 4
        IExp exp1 = new ArithExp(
                ArithOp.MUL,
                new ArithExp(ArithOp.ADD, new ValueExp(new IntValue(2)), new ValueExp(new IntValue(3))),
                new ValueExp(new IntValue(4))
        );
        // (10 - 8)/ 2
        IExp exp2 = new ArithExp(
                ArithOp.DIV, new ArithExp(ArithOp.SUB , new ValueExp(new IntValue(10)) , new ValueExp(new IntValue(8))),
                new ValueExp(new IntValue(2))
        );
        // ((3 + 4) * 5 - 11) / 3
        IExp exp3 = new ArithExp(
                ArithOp.DIV,
                new ArithExp(
                        ArithOp.SUB,
                        new ArithExp(
                                ArithOp.MUL,
                                new ArithExp(
                                        ArithOp.ADD,
                                        new ValueExp(new IntValue(3)),
                                        new ValueExp(new IntValue(4))
                                ),
                                new ValueExp(new IntValue(5))
                        ),
                        new ValueExp(new IntValue(11))
                ),
                new ValueExp(new IntValue(3))
        );

        IDictionary<String, IValue> symTable = new MyDictionary<>();
        IValue val1 = exp1.eval(symTable);
        assertEquals(new IntValue(20), val1);

        IValue val2 = exp2.eval(symTable);
        assertEquals(new IntValue(1) , val2);

        IValue val3 = exp3.eval(symTable);
        assertEquals(new IntValue(8) , val3);
    }

    @Test
    void testArithDivisionByZero() {
        IExp exp = new ArithExp(ArithOp.DIV, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(0)));
        IDictionary<String, IValue> symTable = new MyDictionary<>();
        assertThrows(ExpressionException.class, () -> exp.eval(symTable));
    }

    @Test
    void testLogicExp() throws ExpressionException {
        // t and f
        IExp exp1 = new LogicExp(
                LogicOp.AND,
                new ValueExp(new BoolValue(true)),
                new ValueExp(new BoolValue(false))
        );

        // t or (t and f)
        IExp exp2 = new LogicExp(
                LogicOp.OR,
                new ValueExp(new BoolValue(true)),
                new LogicExp(
                        LogicOp.AND,
                        new ValueExp(new BoolValue(false)),
                        new ValueExp(new BoolValue(true))
                )
        );
        IDictionary<String, IValue> symTable = new MyDictionary<>();
        IValue val1 = exp1.eval(symTable);
        assertEquals(new BoolValue(false), val1);

        IValue val2 = exp2.eval(symTable);
        assertEquals(new BoolValue(true) , val2);
    }
}