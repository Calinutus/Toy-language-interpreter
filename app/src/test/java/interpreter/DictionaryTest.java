package interpreter;

import interpreter.exceptions.ADTException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



public class DictionaryTest {
    @Test
    void testPut(){
        IDictionary<String , Integer> dict = new MyDictionary<>();
        dict.put("a" , 10);
        assertFalse(dict.isDefined("c"), "Key c is not defined yet");
        assertTrue(dict.isDefined("a") ,"Key a is defined");
    }

    @Test
    void testLookup() throws ADTException{
        IDictionary<String , String> dict = new MyDictionary<>();
        dict.put("name" , "Andrei");
        dict.put("date of birth" , "11.11.2006");
        assertEquals("Andrei", dict.lookup("name"));
        assertEquals("11.11.2006" , dict.lookup("date of birth"));
        assertThrows(ADTException.class , () -> {dict.lookup("NAME");});

    }

    @Test
    void testUpdate(){
        IDictionary<String , Integer> dict = new MyDictionary<>();
        dict.put("a" , 10);
        dict.put("b" , 20);
        dict.put("c" , 30);
        dict.update("a" , 30);
        dict.update("c" , 10);
        assertEquals(30 , dict.lookup("a"));
        assertEquals(10 , dict.lookup("c"));
    }

    @Test
    void testUpdateOnNonExistentKey(){
        IDictionary<Integer , String> dict = new MyDictionary<>();
        assertThrows(ADTException.class,() ->{dict.update(1 , "a");});
    }
}
