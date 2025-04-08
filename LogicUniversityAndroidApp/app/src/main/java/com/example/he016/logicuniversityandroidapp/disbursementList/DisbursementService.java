package com.example.he016.logicuniversityandroidapp.disbursementList;

import com.example.he016.logicuniversityandroidapp.JSONParser;
import com.example.he016.logicuniversityandroidapp.model.Disbursement;
import com.example.he016.logicuniversityandroidapp.model.MyDepartment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisbursementService {

    public static List<MyDepartment> GetDepartments() {
        String strUrl = JSONParser.RoutePrefix + "/api/store/GetDepartments";
        JSONArray ja = JSONParser.getJSONArrayFromUrl(strUrl);

        List<MyDepartment> deps = new ArrayList<>();

        for (int i = 0; i < ja.length(); i++) {
            try {
                JSONObject jo = ja.getJSONObject(i);
                MyDepartment depTemp = new MyDepartment(
                        jo.getString("departmentId"),
                        jo.getString("departmentName"),
                        jo.getString("departmentRepName"),
                        jo.getString("collectionPointDescription"),
                        jo.getString("departmentPhone")
                );

                deps.add(depTemp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deps;
    }

    public static Disbursement makeDisbursementfromJSONObject(JSONObject jo) {
        Disbursement disbursement = null;
        if (jo != null) {
            try {
                disbursement = new Disbursement(
                        jo.getString("departmentId"),
                        jo.getString("itemNo"),
                        jo.getString("description"),
                        jo.getString("unitMeasure"),
                        jo.getString("qtyRequired"),
                        jo.getString("qtyActual"),
                        jo.getString("qtyDamaged"),
                        jo.getString("qtyMissing"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return disbursement;
    }

    public static JSONObject makeJsonObjectfromDisbursement(Disbursement disbursement) {
        JSONObject jo = new JSONObject();
        if (disbursement != null) {
            try {
                jo.put("departmentId", disbursement.get("departmentId"));
                jo.put("itemNo", disbursement.get("itemNo"));
                jo.put("description", disbursement.get("description"));
                jo.put("unitMeasure", disbursement.get("unitMeasure"));
                jo.put("qtyRequired", disbursement.get("qtyRequired"));
                jo.put("qtyActual", disbursement.get("qtyActual"));
                jo.put("qtyDamaged", disbursement.get("qtyDamaged"));
                jo.put("qtyMissing", disbursement.get("qtyMissing"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jo;
    }

    public static List<Disbursement> ViewDisbursementList(String deptId) {
        String strUrl = JSONParser.RoutePrefix + "/api/store/GetDisbursement/" + deptId;
        JSONArray ja = JSONParser.getJSONArrayFromUrl(strUrl);

        List<Disbursement> disbursementList = new ArrayList<>();
        if (ja != null) {
            try {
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    Disbursement d = makeDisbursementfromJSONObject(jo);
                    disbursementList.add(d);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return disbursementList;
    }

    public static boolean AcceptDisbursementList(List<Disbursement> list) {
        JSONArray updateDisbursementList = createJsonArrayFromList(list);

        if (updateDisbursementList != null) {
            String strURL = JSONParser.RoutePrefix + "/api/store/ConfirmDisbursement";
            String result = JSONParser.postStream(strURL, true, updateDisbursementList.toString());
            result = result.replaceAll("(\\r\\n|\\n|\\r)", "");
            Boolean bResult = Boolean.parseBoolean(result);
            return bResult;
        } else {
            return false;
        }
    }

    public static JSONArray createJsonArrayFromList(List<Disbursement> list) {
        JSONArray jArray = new JSONArray();
        try {
            for (Disbursement d : list) {
                JSONObject jo = new JSONObject();
                jo.put("departmentId", d.get("departmentId"));
                jo.put("itemNo", d.get("itemNo"));
                jo.put("description", d.get("description"));
                jo.put("unitMeasure", d.get("unitMeasure"));
                jo.put("qtyRequired", d.get("qtyRequired"));
                jo.put("qtyActual", d.get("qtyActual"));
                jo.put("qtyDamaged", d.get("qtyDamaged"));
                jo.put("qtyMissing", d.get("qtyMissing"));
                jArray.put(jo);
            }
        } catch (JSONException jse) {
            jse.printStackTrace();
        }
        return jArray;
    }

}
