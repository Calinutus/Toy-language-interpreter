package interpreter;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.adt.MyHeap;
import interpreter.model.exp.*;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.IValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpTest {

    private IHeap heap;
    private IDictionary<String, IValue> symTable;

    @BeforeEach
    void setUp() {
        heap = new MyHeap();
        symTable = new MyDictionary<>();
    }

    @Test
    void testValueExp() throws MyException {
        IExp exp = new ValueExp(new IntValue(10));
        IValue val = exp.eval(symTable, heap);
        assertEquals(new IntValue(10), val);
    }

    @Test
    void testVarExp() throws MyException {
        IExp exp = new VarExp("v");

        symTable.put("v", new IntValue(20));
        IValue val1 = exp.eval(symTable, heap);
        assertEquals(new IntValue(20), val1);

        IValue val2 = exp.eval(symTable, heap);
        assertEquals(new IntValue(20), val2);

        assertEquals(val1 , val2);
    }

    @Test
    void testArithExp() throws MyException {
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

        IValue val1 = exp1.eval(symTable, heap);
        assertEquals(new IntValue(20), val1);

        IValue val2 = exp2.eval(symTable, heap);
        assertEquals(new IntValue(1) , val2);

        IValue val3 = exp3.eval(symTable, heap);
        assertEquals(new IntValue(8) , val3);
    }

    @Test
    void testArithDivisionByZero() {
        IExp exp = new ArithExp(ArithOp.DIV, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(0)));
        assertThrows(ExpressionException.class, () -> exp.eval(symTable, heap));
    }

    @Test
    void testLogicExp() throws MyException {
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
        IValue val1 = exp1.eval(symTable, heap);
        assertEquals(new BoolValue(false), val1);

        IValue val2 = exp2.eval(symTable, heap);
        assertEquals(new BoolValue(true) , val2);
    }

    @Test
    void testArithNonIntegerOperand(){
        IExp exp = new ArithExp(ArithOp.ADD , new ValueExp(new IntValue(2)) , new ValueExp(new BoolValue(true)));
        assertThrows(MyException.class , () -> {exp.eval(symTable, heap);});
    }

    @Test
    void testLogicNonBooleanOperand(){
        IExp exp = new LogicExp(LogicOp.AND , new ValueExp(new IntValue(1)) , new ValueExp(new IntValue(2)));
        assertThrows(MyException.class, () -> {exp.eval(symTable, heap);});
    }

    @Test
    void testUndefinedVariableExp(){
        IExp exp = new VarExp("undefinedVar");
        assertThrows(MyException.class, () -> exp.eval(symTable, heap));
    }

    @Test
    void testRelationalExpressions() throws MyException {
        assertEquals(new BoolValue(true), new RelationalExp(Relop.LT, new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5))).eval(symTable, heap));
        assertEquals(new BoolValue(false), new RelationalExp(Relop.LT, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(3))).eval(symTable, heap));
        assertEquals(new BoolValue(true), new RelationalExp(Relop.LE, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(5))).eval(symTable, heap));
        assertEquals(new BoolValue(true), new RelationalExp(Relop.LE, new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5))).eval(symTable, heap));
        assertEquals(new BoolValue(true), new RelationalExp(Relop.EQ, new ValueExp(new IntValue(4)), new ValueExp(new IntValue(4))).eval(symTable, heap));
        assertEquals(new BoolValue(true), new RelationalExp(Relop.NE, new ValueExp(new IntValue(4)), new ValueExp(new IntValue(5))).eval(symTable, heap));
        assertEquals(new BoolValue(true), new RelationalExp(Relop.GT, new ValueExp(new IntValue(7)), new ValueExp(new IntValue(2))).eval(symTable, heap));
        assertEquals(new BoolValue(true), new RelationalExp(Relop.GE, new ValueExp(new IntValue(7)), new ValueExp(new IntValue(7))).eval(symTable, heap));
    }

    @Test
    void testRelationalNonIntegerOperandThrows() {
        IExp exp = new RelationalExp(Relop.LT, new ValueExp(new IntValue(5)), new ValueExp(new BoolValue(true)));
        assertThrows(ExpressionException.class, () -> exp.eval(symTable, heap));
    }
}