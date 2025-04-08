package com.example.he016.logicuniversityandroidapp.model;

import android.util.Log;

import com.example.he016.logicuniversityandroidapp.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PurchaseOrderDetail extends HashMap<String, String> {

    static String baseURL = String.format("%s/api/store", JSONParser.RoutePrefix);

    public PurchaseOrderDetail(String poId, String itemNo, String description, String unitMeasure, String quantity) {
        put("poId", poId);
        put("itemNo", itemNo);
        put("description", description);
        put("unitMeasure", unitMeasure);
        put("quantity", quantity);
    }

    public PurchaseOrderDetail(String itemNo, String description, String unitMeasure, String quantity) {
        put("itemNo", itemNo);
        put("description", description);
        put("unitMeasure", unitMeasure);
        put("quantity", quantity);
    }


    //get a list of items in the PO id
    public static List<PurchaseOrderDetail> ListItemsByPOID(int poId) {
        List<PurchaseOrderDetail> list = new ArrayList<PurchaseOrderDetail>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/GetPoDetailList/%s", baseURL, poId)); //using api /PurchaseOrderDetail
        try {
            for (int i = 0; i < a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                if (poId == b.getInt("poId")) {
                    PurchaseOrderDetail podTemp = new PurchaseOrderDetail(b.getString("poId"),
                            b.getString("itemNo"),
                            b.getString("description"),
                            b.getString("unitMeasure"),
                            b.getString("quantity"));
                    list.add(podTemp);
                }
            }
        } catch (Exception e) {
            Log.e("PurchaseOrderDetailList", "JSONArray error");
        }
        return list;
    }


    public static PurchaseOrderDetail ReadItemNo(int poId, String itemNo) {
        try {

            JSONObject a = JSONParser.getJSONFromUrl(String.format("%s/%s", baseURL + "/GetPoDetail", poId + "/" + itemNo));
            PurchaseOrderDetail b = new PurchaseOrderDetail(a.getString("poId"),
                    a.getString("itemNo"),
                    a.getString("description"),
                    a.getString("unitMeasure"),
                    a.getString("quantity"));
            return b;
        } catch (Exception e) {
            Log.e("Item", "JSONArray error");
        }
        return null;
    }


    public static void saveItem(int poId, String itemNo, int quantity, String status) {
        JSONObject jemp = new JSONObject();

        try {
            jemp.put("itemNo", itemNo);
            jemp.put("poId", poId);
            jemp.put("quantity", quantity);
            jemp.put("status", status);

        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONParser.postStream(baseURL + "/UpdatePORequestDetail", true, jemp.toString());
    }


    //to update the status of PO which is initially "sent" to "closed"
    public static void savePOList(List<PurchaseOrderDetail> blist, String orderDate, String deliveryDate, int poId) {
        JSONArray ja = new JSONArray();

        try {
            for (int i = 0; i < blist.size(); i++) {
                JSONObject jemp = new JSONObject();
                jemp.put("status", "Closed");
                jemp.put("deliveryDate", deliveryDate);
                jemp.put("orderDate", orderDate);
                jemp.put("poId", poId);
                jemp.put("itemNo", blist.get(i).get("itemNo"));
                jemp.put("quantity", blist.get(i).get("quantity"));

                ja.put(i, jemp);
            }
            JSONParser.postStream(baseURL + "/UpdatePORequestDetails", true, ja.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
