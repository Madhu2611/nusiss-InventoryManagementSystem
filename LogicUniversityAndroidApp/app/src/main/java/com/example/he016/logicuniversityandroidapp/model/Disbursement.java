package com.example.he016.logicuniversityandroidapp.model;

import java.util.HashMap;

public class Disbursement extends HashMap<String, String> {

    public Disbursement(String departmentId,
                        String itemNo,
                        String description,
                        String unitMeasure,
                        String qtyRequired,
                        String qtyActual,
                        String qtyDamaged,
                        String qtyMissing) {

        put("departmentId", departmentId);
        put("itemNo", itemNo);
        put("description", description);
        put("unitMeasure", unitMeasure);
        put("qtyRequired", qtyRequired);
        put("qtyActual", qtyActual);
        put("qtyDamaged", qtyDamaged);
        put("qtyMissing", qtyMissing);
    }
}