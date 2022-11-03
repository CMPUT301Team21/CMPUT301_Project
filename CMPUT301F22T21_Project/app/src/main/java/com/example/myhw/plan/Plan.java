package com.example.myhw.plan;

import com.google.firebase.firestore.DocumentId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Plan {
    public List<AnotherIngredient> list;
    @DocumentId
    public String id;

    public Map<String, Object> toMap() {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("list", list);
        return stringObjectHashMap;
    }

    public String getListStr() {
        StringBuilder builder = new StringBuilder();
        for (AnotherIngredient anotherIngredient : list) {
            builder.append(anotherIngredient.description).append("\t\t\t\tX").append(anotherIngredient.count).append("\n");
        }
        return builder.toString();
    }
}
