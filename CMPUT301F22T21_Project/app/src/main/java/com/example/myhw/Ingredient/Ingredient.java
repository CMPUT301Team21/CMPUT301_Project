package com.example.myhw.Ingredient;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Ingredient implements Serializable {
    public int count;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return description.equals(that.description);
    }

}
