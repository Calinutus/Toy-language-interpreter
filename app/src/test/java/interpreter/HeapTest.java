package interpreter;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IHeap;
import interpreter.model.adt.MyHeap;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HeapTest {
    private IHeap heap;

    @BeforeEach
    void setUp() {
        heap = new MyHeap();
    }

    @Test
    void testAllocateReturnsSequentialAddresses() throws MyException {
        IValue value1 = new IntValue(1);
        IValue value2 = new IntValue(2);

        int addr1 = heap.allocate(value1);
        int addr2 = heap.allocate(value2);

        assertEquals(1,addr1);
        assertEquals(2,addr2);

        assertEquals(value1 , heap.get(addr1));
        assertEquals(value2 , heap.get(addr2));
    }

    @Test
    void testGetMissingAddressThrowsException(){
        assertThrows(ADTException.class, () ->heap.get(99));
    }

    @Test
    void testUpdateExistingAddress() throws ADTException {
        int addr = heap.allocate(new IntValue(20));
        heap.update(addr , new IntValue(50));
        assertEquals(new IntValue(50), heap.get(addr));
    }

    @Test
    void testUpdateOnMissingAddress(){
        assertThrows(ADTException.class, () ->heap.update(99 , new IntValue(20)));
    }

    @Test
    void testIsDefined(){
        int addr = heap.allocate(new IntValue(10));
        assertTrue(heap.isDefined(addr));
        assertFalse(heap.isDefined(99));
    }

    @Test
    void testGetAndSetContent() throws ADTException {
        heap.allocate(new IntValue(100));
        var content = heap.getContent();

        assertEquals(1 , content.size());
        assertEquals(new IntValue(100) , content.get(1));

        var newContent = Map.of(5 , (IValue) new IntValue(999));
        heap.setContent(newContent);

        assertTrue(heap.isDefined(5));
        assertEquals(new IntValue(999) , heap.get(5));
    }
}
