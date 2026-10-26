package interpreter;
import interpreter.exceptions.ADTException;
import interpreter.model.adt.IList;
import interpreter.model.adt.MyList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ListTest {
    @Test
    void testAdd(){
        IList<Integer> list = new MyList<>();
        assertTrue(list.isEmpty(), "The list should be empty");
        list.add(1);
        list.add(2);
        assertFalse(list.isEmpty() , "The list is not empty");
        assertEquals(2 , list.size());
    }

    @Test
    void testGet() throws ADTException {
        IList<String> list = new MyList<>();
        list.add("apple");
        list.add("orange");
        assertEquals("orange" , list.get(1));
        assertEquals("apple" , list.get(0));
        assertNotEquals("abb" , list.get(0));
    }

    @Test
    void testGetOutOfBounds(){
        IList<Integer> list = new MyList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        assertThrows(ADTException.class, () ->{list.get(3);});
        assertThrows(ADTException.class, () ->{list.get(-1);});
        assertThrows(ADTException.class, () ->{list.get(10);});
    }
}
