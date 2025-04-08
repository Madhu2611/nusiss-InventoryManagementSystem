package com.example.he016.logicuniversityandroidapp.model;

import android.util.Log;

import com.example.he016.logicuniversityandroidapp.JSONParser;

import org.json.JSONObject;

import java.util.HashMap;

public class Department extends HashMap<String, String> {

    //static String host = "172.23.200.41";
    static String baseURL;

    static {
        baseURL = String.format("%s/api/department", JSONParser.RoutePrefix);
    }

    public Department(String departmentId, String departmentName, String collectionPointId, String staffIdDH, String staffIdDR,
                      String staffIdContact, String departmentFax, String departmentPhone) {
        put("departmentId", departmentId);
        put("departmentName", departmentName);
        put("collectionPointId", collectionPointId);
        put("staffIdDH", staffIdDH);
        put("staffIdDR", staffIdDR);
        put("staffIdContact", staffIdContact);
        put("departmentFax", departmentFax);
        put("departmentPhone", departmentPhone);
    }

    public Department(String departmentId, String departmentName, String collectionPointId, String collectionPointDescription,
                      String staffIdDH, String DHName, String staffIdDR, String DRName, String staffIdContact,
                      String ContactName, String departmentFax, String departmentPhone) {
        put("departmentId", departmentId);
        put("departmentName", departmentName);
        put("collectionPointId", collectionPointId);
        put("collectionPointDescription", collectionPointDescription);
        put("staffIdDH", staffIdDH);
        put("DHName", DHName);
        put("staffIdDR", staffIdDR);
        put("DRName", DRName);
        put("staffIdContact", staffIdContact);
        put("ContactName", ContactName);
        put("departmentFax", departmentFax);
        put("departmentPhone", departmentPhone);
    }

    public static void saveCollectionPoint(String collectionPoint) {
        JSONObject a = new JSONObject();
        try {
            a.put("collectionPointId", collectionPoint);
        } catch (Exception e) {
            Log.e("Save Collection Point", "JSONArray error");
        }
        JSONParser.postStream(baseURL + "/saveCollectionPoint", true, a.toString());
    }

    public static void saveDepartmentRep(String staffId) {
        JSONObject a = new JSONObject();
        try {
            a.put("staffId", staffId);
        } catch (Exception e) {
            Log.e("Save DR", "JSONArray error");
        }
        JSONParser.postStream(baseURL + "/saveDepartmentRep", true, a.toString());
    }

    public static Department showDepartmentDetails() {
        try {
            JSONObject a = JSONParser.getJSONFromUrl(baseURL + "/departmentDetails");
            Department dep = new Department(
                    a.getString("departmentId"),
                    a.getString("departmentName"),
                    a.getString("collectionPointId"),
                    a.getString("collectionPointDescription"),
                    a.getString("staffIdDH"),
                    a.getString("DHName"),
                    a.getString("staffIdDR"),
                    a.getString("DRName"),
                    a.getString("staffIdContact"),
                    a.getString("ContactName"),
                    a.getString("departmentFax"),
                    a.getString("departmentPhone"));
            return dep;
        } catch (Exception e) {
            Log.e("Department Details", "JSONArray error");
        }
        return null;
    }

}
