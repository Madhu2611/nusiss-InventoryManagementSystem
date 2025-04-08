package com.example.he016.logicuniversityandroidapp.model;

import android.util.Log;

import com.example.he016.logicuniversityandroidapp.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PurchaseOrder extends HashMap<String, String> {
    static String baseURL = String.format("%s/api/store", JSONParser.RoutePrefix);

    public PurchaseOrder(String poId, String orderDate, String deliveryDate, String status) {
        put("poId", poId);
        put("orderDate", orderDate.substring(0, 10));
        put("deliveryDate", deliveryDate.substring(0, 10));
        put("status", status);
    }

    //get a list of SENT PO
    public static List<PurchaseOrder> ReadPurchaseOrder() {
        List<PurchaseOrder> list = new ArrayList<PurchaseOrder>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/GetPoList", baseURL));
        try {
            for (int i = 0; i < a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                if (b.getString("status").toUpperCase().equals("SENT")) {
                    list.add(new PurchaseOrder(b.getString("poId"),
                            b.getString("orderDate"),

                            b.getString(("deliveryDate")),
                            b.getString("status")));
                }
            }
        } catch (Exception e) {
            Log.e("PurchaseOrderList", "JSONArray error");
        }
        return list;
    }

}

