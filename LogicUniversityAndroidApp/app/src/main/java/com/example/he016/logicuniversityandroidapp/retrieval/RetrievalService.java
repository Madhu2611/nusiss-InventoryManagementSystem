package com.example.he016.logicuniversityandroidapp.retrieval;

import com.example.he016.logicuniversityandroidapp.JSONParser;
import com.example.he016.logicuniversityandroidapp.model.Retrieval;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RetrievalService {


    public static List<Retrieval> ViewRetrievalGoods(String DueDate) {
        if (DueDate == null)
            return null;

        String[] parts = DueDate.split("T");
        String strUrl = JSONParser.RoutePrefix + "/api/store/ViewRetrievalGoods/" + parts[0];
        JSONArray ja = JSONParser.getJSONArrayFromUrl(strUrl);

        List<Retrieval> retrievalList = new ArrayList<>();
        if (ja != null) {

            try {
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    Retrieval retrieval = makeRetrievalByJsonObject(jo);
                    retrievalList.add(retrieval);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return retrievalList;
    }

    public static Retrieval ViewRetrievalGood(String itemNo, String DueDate) {
        Retrieval retrieval = null;
        try {
            String[] parts = DueDate.split("T");

            String strUrl = JSONParser.RoutePrefix + "/api/store/ViewRetrievalGood/" + itemNo + "/" + parts[0];
            JSONObject jo = JSONParser.getJSONFromUrl(strUrl);

            if (jo != null) {
                retrieval = makeRetrievalByJsonObject(jo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retrieval;
    }

    public static Boolean AllocateGoods(String DueDate, String itemNo, String quantityRetrieval, String quantityInstoreDamaged, String quantityInstoreMissing) {
        Retrieval tempRetrieval = new Retrieval(
                DueDate,
                itemNo,
                "",
                "",
                "",
                "",
                "",
                "",
                quantityRetrieval,
                quantityInstoreDamaged,
                quantityInstoreMissing
        );
        JSONObject jo = RetrievalService.makeJsonObjectByRetrieval(tempRetrieval);

        // post stream here...
        String strURL = JSONParser.RoutePrefix + "/api/store/AllocateGoods";
        String result = JSONParser.postStream(strURL, true, jo.toString());

        result = result.replaceAll("(\\r\\n|\\n|\\r)", "");
        Boolean bResult = Boolean.parseBoolean(result);
        return bResult;
    }

    public static Retrieval makeRetrievalByJsonObject(JSONObject jo) {
        Retrieval retrieval = null;
        if (jo != null) {
            try {
                retrieval = new Retrieval(
                        jo.getString("DueDate"),
                        jo.getString("itemNo"),
                        jo.getString("description"),
                        jo.getString("unitMeasure"),
                        jo.getString("categoryId"),
                        jo.getString("location"),
                        jo.getString("balance"),
                        jo.getString("quantityTotalNeed"),
                        jo.getString("quantityRetrieval"),
                        jo.getString("quantityInstoreDamaged"),
                        jo.getString("quantityInstoreMissing"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return retrieval;
    }

    public static JSONObject makeJsonObjectByRetrieval(Retrieval retrieval) {
        JSONObject jo = new JSONObject();
        if (retrieval != null) {
            try {
                jo.put("DueDate", retrieval.get("DueDate"));
                jo.put("itemNo", retrieval.get("itemNo"));
                jo.put("description", retrieval.get("description"));
                jo.put("unitMeasure", retrieval.get("unitMeasure"));
                jo.put("categoryId", retrieval.get("categoryId"));
                jo.put("location", retrieval.get("location"));
                jo.put("balance", retrieval.get("balance"));
                jo.put("quantityTotalNeed", retrieval.get("quantityTotalNeed"));
                jo.put("quantityRetrieval", retrieval.get("quantityRetrieval"));
                jo.put("quantityInstoreDamaged", retrieval.get("quantityInstoreDamaged"));
                jo.put("quantityInstoreMissing", retrieval.get("quantityInstoreMissing"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jo;
    }

}
