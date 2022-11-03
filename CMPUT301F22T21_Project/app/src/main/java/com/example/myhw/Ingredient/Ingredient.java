package com.example.myhw.Ingredient;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Ingredient implements Serializable {
    public int count;
    @DocumentId
    public String id;
    public String description;
    public String unit;
    public String category;
    public String location;
    public String time;

    public Map<String, Object> toMap() {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("description", description);
        stringObjectHashMap.put("count", count);
        stringObjectHashMap.put("unit", unit);
        stringObjectHashMap.put("category", category);
        stringObjectHashMap.put("location", location);
        stringObjectHashMap.put("time", time);
        return stringObjectHashMap;
    }
}
