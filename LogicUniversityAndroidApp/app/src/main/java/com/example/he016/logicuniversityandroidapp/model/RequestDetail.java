package com.example.he016.logicuniversityandroidapp.model;

import android.util.Log;
import android.widget.DatePicker;

import com.example.he016.logicuniversityandroidapp.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class RequestDetail extends HashMap<String, String> {

    //static String host = "172.23.200.41"; //local host server address
    static String baseURL;

    static {
        baseURL = String.format("%s/api/department", JSONParser.RoutePrefix);
    }

    public RequestDetail(String index, String requestId, String itemNo, String description, String category,
                         String stdPrice, String quantityNeed, String status, String uom) {
        put("index", index);
        put("requestId", requestId);
        put("itemNo", itemNo);
        put("description", description);
        put("category", category);
        put("stdPrice", stdPrice);
        put("quantityNeed", quantityNeed);
        put("status", status);
        put("unitMeasure", uom);
    }

    public RequestDetail(String index, String requestId, String itemNo, String description, String category,
                         String stdPrice, String quantityNeed, String status, String uom, String totalPrice) {
        put("index", index);
        put("requestId", requestId);
        put("itemNo", itemNo);
        put("description", description);
        put("category", category);
        put("stdPrice", stdPrice);
        put("quantityNeed", quantityNeed);
        put("status", status);
        put("unitMeasure", uom);
        put("totalPrice", totalPrice);
    }

    public RequestDetail(String index, String requestId, String itemNo, String description, String quantityNeed, String status) {
        put("index", index);
        put("requestId", requestId);
        put("itemNo", itemNo);
        put("description", description);
        put("quantityNeed", quantityNeed);
        put("status", status);

    }

    public static List<RequestDetail> RetrieveRequestDetails(int requestId) {
        List<RequestDetail> list = new ArrayList<RequestDetail>();
        try {
            JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/ReadRequestDetailByReqId/%s", baseURL, requestId));

            for (int i = 0; i < a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                if (requestId == b.getInt("requestId")) {
                    //all the parameters obtained from JSON are from the webAPI
                    list.add(new RequestDetail(String.format("%d", b.getInt("index_detail")),
                            String.format("%d", b.getInt("requestId")),
                            b.getString("itemNo"), b.getString("description"),
                            b.getString("category"), String.format("%,.2f", b.getDouble("stdPrice")),
                            String.format("%d", b.getInt("quantityNeed")),
                            b.getString("status_requestDetail"), b.getString("unitMeasure")));
                }

            }
        } catch (Exception e) {
            Log.e("RequestDetail", "JSONArray error");
        }
        return (list);
    }

    public static void insertRequestDetail(int requestId, String itemNo, int quantityNeed) {
        JSONObject jemp = new JSONObject();
        try {
            jemp.put("requestId", requestId);
            jemp.put("itemNo", itemNo);
            jemp.put("quantityNeed", quantityNeed);

        } catch (Exception e) {
            Log.e("Insert Request Details", "JSONArray error");
        }
        JSONParser.postStream(baseURL + "/insertRequestDetail", true, jemp.toString());
    }

    public static void saveEditRequestDetail(int requestId, String itemNo, int quantityNeed) {
        JSONObject jemp = new JSONObject();
        try {
            jemp.put("requestId", requestId);
            jemp.put("itemNo", itemNo);
            jemp.put("quantityNeed", quantityNeed);

        } catch (Exception e) {
            Log.e("Edit Request Details", "JSONArray error");
        }
        JSONArray jArray = new JSONArray();
        jArray.put(jemp);
        JSONParser.postStream(baseURL + "/updateRequestDetails", true, jArray.toString());
    }

    public static void deleteEditRequestDetail(int requestId, String itemNo) {
        JSONObject jemp = new JSONObject();
        try {
            jemp.put("requestId", requestId);
            jemp.put("itemNo", itemNo);

        } catch (Exception e) {
            Log.e("Delete Request Details", "JSONArray error");
        }
        JSONParser.postStream(baseURL + "/deleteRequestDetail", true, jemp.toString());
    }

    //approve/reject
    public static List<RequestDetail> ListRequestDetailsById(String requestId) {
        List<RequestDetail> list = new ArrayList<RequestDetail>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(baseURL + "/ReadRequestDetailByReqId/" + requestId);
        try {
            for (int i = 0; i < a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                String totalPrice = String.format("%.2f", (b.getInt("quantityNeed") * b.getDouble("stdPrice")));
                list.add(new RequestDetail(b.getString("index_detail"),
                        b.getString("requestId"),
                        b.getString("itemNo"),
                        b.getString("description"),
                        b.getString("category"),
                        b.getString("stdPrice"),
                        b.getString("quantityNeed"),
                        b.getString("status_request"),
                        b.getString("unitMeasure"), totalPrice));

            }
        } catch (Exception e) {
            Log.e("Request Details List", "JSONArray error");
        }
        return list;
    }

    // Raise request
    public static void raiseNewRequest(String itemNo, int quantityNeed) {
        JSONObject jObject = new JSONObject();
        try {
            jObject.put("itemNo", itemNo);
            jObject.put("quantityNeed", quantityNeed);
            jObject.put("approvedDate", DateFormat.getDateInstance().format(new Date()));
            jObject.put("status_request", "submitted");
            jObject.put("status_requestDetail", "unfulfilled");
            jObject.put("quantityReceived", 0);
            jObject.put("quantitytPacked", 0);
        } catch (Exception e) {
            Log.e("Raise reqeust", "JSONArray error");
        }
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jObject);
        JSONParser.postStream(baseURL + "/raiseNewRequest", true, jsonArray.toString());
    }
}