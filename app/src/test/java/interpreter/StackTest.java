package interpreter;

import interpreter.exceptions.ADTException;
import interpreter.model.adt.IStack;
import interpreter.model.adt.MyStack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StackTest {
    @Test
    void testPush(){
        IStack<Integer> stack = new MyStack<>();
        assertTrue(stack.isEmpty(), "The stack should be empty");
        stack.push(10);
        stack.push(20);
        stack.push(30);
        assertFalse(stack.isEmpty() , "The stack is not empty");
    }

    @Test
    void testPop() throws ADTException {
        IStack<Integer> stack = new MyStack<>();
        stack.push(10);
        assertEquals(10 , stack.pop() , "The value should be the 10");
    }

    @Test
    void testPopOnEmptyStack(){
        IStack<Integer> stack = new MyStack<>();
        assertThrows(ADTException.class, () -> {stack.pop();});
    }

    @Test
    void testLifo() throws ADTException {
        IStack<Integer> stack = new MyStack<>();
        stack.push(10);
        stack.push(20);
        stack.push(30);
        assertEquals(30 , stack.pop());
        assertEquals(20 , stack.pop());
        assertEquals(10 , stack.pop());
    }

}
