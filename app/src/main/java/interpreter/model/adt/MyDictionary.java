package interpreter.model.adt;

import interpreter.exceptions.ADTException;

import java.util.HashMap;
import java.util.Map;

public class MyDictionary<K , V> implements IDictionary<K , V>{
    private Map<K , V> map= new HashMap<>();
    @Override
    public boolean isDefined(K key){
        return map.containsKey(key);
    }

    @Override
    public void put(K key ,V value){
        map.put(key , value);
    }

    @Override
    public void update(K key,V value) throws ADTException{
        if(!(map.containsKey(key))) throw new ADTException("Key" + key + "is not defined");
        map.put(key , value);
    }

    @Override
    public V lookup(K key) throws ADTException{
        V value = map.get(key);
        if(value == null)throw new ADTException("Key" + key + "is not defined");
        return value;
    }

    @Override
    public Map<K , V> getContent(){
        return map;
    }

    @Override
    public String toString(){
        return map.toString();
    }
}
