package com.example.he016.logicuniversityandroidapp.model;

import android.util.Log;

import com.example.he016.logicuniversityandroidapp.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Request extends HashMap<String, String> {

    //static String host = "172.23.200.41"; //local host server address
    static String baseURL;

    static {
        baseURL = String.format("%s/api/department", JSONParser.RoutePrefix);
    }

    public Request(String reqid, String depid, String staffid, String approvedate, String status, String staffName, String stdPrice) {
        put("requestId", reqid);
        put("departmentId", depid);
        put("staffId", staffid);
        put("approvedDate", approvedate.substring(0, 10));
        put("status", status);
        put("staffName", staffName);
        put("stdPrice", stdPrice);
    }

    public Request(String reqid, String depid, String staffid, String approvedate, String status, String staffName) {
        put("requestId", reqid);
        put("departmentId", depid);
        put("staffId", staffid);
        put("approvedDate", approvedate.substring(0, 10));
        put("status", status);
        put("staffName", staffName);
    }


    public static List<Request> ReadRequest() {
        List<Request> list = new ArrayList<Request>();
        try {
            JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/ReadRequest", baseURL));
            String previousRequest = "0";
            for (int i = 0; i < a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                list.add(new Request(b.getString("requestId"), b.getString("departmentId"),
                        b.getString("staffId"), b.getString("approvedDate"),
                        b.getString("status_request"), b.getString("staffName")));
                previousRequest = b.getString("requestId");

            }
        } catch (Exception e) {
            Log.e("Request", "JSONArray error");
        }
        return (list);
    }

    //approve/reject
    public static List<Request> ListRequestsByDepartmentId() {
        List<Request> list = new ArrayList<Request>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(baseURL + "/ListRequestsByDepartmentId");
        try {
            for (int i = 0; i < a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                list.add(new Request(b.getString("requestId"),
                        b.getString("departmentId"),
                        b.getString("staffId"),
                        b.getString("approvedDate"),
                        b.getString("status_request"),
                        b.getString("staffName"),
                        b.getString("stdPrice")));
            }
        } catch (Exception e) {
            Log.e("Request Lists", "JSONArray error");
        }
        return list;
    }

    public static void updateRequestStatus(String requestId, String status, String remark) {
        JSONObject jemp = new JSONObject();
        try {
            jemp.put("requestId", requestId);
            jemp.put("status_request", status);
            jemp.put("remark", remark);
        } catch (Exception e) {
            Log.e("Update Status", "JSONArray error");
        }
        JSONParser.postStream(baseURL + "/updateApproveRejectStatus", true, jemp.toString());
    }
}

