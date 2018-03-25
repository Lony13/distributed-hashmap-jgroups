package zad;

import java.util.HashMap;

public class DistributedMap implements SimpleStringMap {

    private HashMap<String, String> distributedHashMap;

    public DistributedMap(){
        this.distributedHashMap = new HashMap<>();
        //pozyskanie stanu od istniejacych czlonkow
    }

    @Override
    public boolean containsKey(String key) {
        return distributedHashMap.containsKey(key);
    }

    @Override
    public String get(String key) {
        return distributedHashMap.get(key);
    }

    @Override
    public String put(String key, String value) {

        return distributedHashMap.put(key, value);
    }

    @Override
    public String remove(String key) {
        return distributedHashMap.remove(key);
    }
}
