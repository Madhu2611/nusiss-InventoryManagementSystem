package com.example.he016.logicuniversityandroidapp.model;

import android.util.Log;

import com.example.he016.logicuniversityandroidapp.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdjustmentVoucher extends HashMap<String, String> {

    static String baseURL2;


    static {
        baseURL2 = String.format("%s/api/store", JSONParser.RoutePrefix);
    }

    public AdjustmentVoucher(String voucherId, String category, String description, String remark, String quantity, String date, String itemNo) {
        put("voucherId", voucherId);
        put("description", description);
        put("quantity", quantity);
        put("category", category);
        put("date", date);
        put("remark", remark);
        put("itemNo", itemNo);
    }

    public AdjustmentVoucher(String itemNo, String description, String totalamount, String unitPrice) {
        put("itemNo", null);
        put("description", description);
        put("totalamount", totalamount);
        put("unitPrice", null);
    }

    // Method
    public static boolean saveAdjustmentVoucher(AdjustmentVoucher adv) {
        boolean res = false;
        JSONObject jemp = new JSONObject();

        try {
            jemp.put("description", adv.get("description"));
            jemp.put("quantity", adv.get("quantity"));
            jemp.put("category", adv.get("category"));
            jemp.put("remark", adv.get("remark"));
            jemp.put("itemNo", adv.get("itemNo"));
            String strResult = JSONParser.postStream(baseURL2 + "/CreateAdjustmentVoucher", true, jemp.toString());
            strResult = strResult.replaceAll("(\\r\\n|\\n|\\r)", "");
            res = Boolean.parseBoolean(strResult);

        } catch (Exception e) {
            res = false;
        }
        return res;

    }

    public static List<AdjustmentVoucher> FindGeneralAdjustmentVoucher(int year, int month) {

        List<AdjustmentVoucher> list = new ArrayList<AdjustmentVoucher>();

        try {
            JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/FindGeneralAdjustmentVoucher/%s/%s", baseURL2, year, month));

            if (a != null) {
                for (int i = 0; i < a.length(); i++) {
                    JSONObject b = a.getJSONObject(i);
                    list.add(new AdjustmentVoucher(
                            b.getString("itemNo"),
                            b.getString("description"),
                            b.getString("totalamount"),
                            b.getString("unitPrice")
                    ));
                }
            }
        } catch (Exception e) {
            Log.e("ListbyMonth", "JSONArray error");
        }
        return list;
    }
}
