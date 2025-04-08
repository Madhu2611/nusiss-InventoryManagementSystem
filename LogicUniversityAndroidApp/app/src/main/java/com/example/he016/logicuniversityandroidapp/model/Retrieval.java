package com.example.he016.logicuniversityandroidapp.model;

import java.util.HashMap;

public class Retrieval extends HashMap<String, String> {

    // constructor
    public Retrieval(String DueDate,
                     String itemNo,
                     String description,
                     String unitMeasure,
                     String categoryId,
                     String location,
                     String balance,
                     String quantityTotalNeed,
                     String quantityRetrieval,
                     String quantityInstoreDamage,
                     String quantityInstoreMissing) {
        put("DueDate", DueDate);
        put("itemNo", itemNo);
        put("description", description);
        put("unitMeasure", unitMeasure);
        put("categoryId", categoryId);
        put("location", location);
        put("balance", balance);
        put("quantityTotalNeed", quantityTotalNeed);
        put("quantityRetrieval", quantityRetrieval);
        put("quantityInstoreDamaged", quantityInstoreDamage);
        put("quantityInstoreMissing", quantityInstoreMissing);
    }

}
