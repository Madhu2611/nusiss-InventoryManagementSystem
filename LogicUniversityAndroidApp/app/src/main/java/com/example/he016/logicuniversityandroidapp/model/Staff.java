package com.example.he016.logicuniversityandroidapp.model;

import android.util.Log;

import com.example.he016.logicuniversityandroidapp.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Staff implements Serializable {

    //static String host = "172.23.200.41";
    static String baseURL;

    static {
        baseURL = String.format("%s/api/department", JSONParser.RoutePrefix);
    }

    public String staffId;
    public String staffName;
    public String departmentId;
    public String delegatedStatus;
    public String delegatedStartDate;
    public String delegatedEndDate;

    public Staff(String staffId, String staffName, String departmentId, String delegatedStatus, String delegatedStartDate, String delegatedEndDate) {
        this.staffId = staffId;
        this.staffName = staffName;
        this.departmentId = departmentId;
        this.delegatedStatus = delegatedStatus;
        this.delegatedStartDate = delegatedStartDate;
        this.delegatedEndDate = delegatedEndDate;
    }

    public static List<Staff> ListStaffByDepartment() {
        List<Staff> list = new ArrayList<Staff>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(baseURL + "/FindPossibleDRList");
        try {
            for (int i = 0; i < a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                //Staff sTemp2=new Staff("1","2",null,null,null,null);

                Staff temp = new Staff(
                        b.getString("staffId"),
                        b.getString("staffName"),
                        b.getString("departmentId"),
                        b.getString("delegatedStatus"),
                        b.getString("delegatedStartDate"),
                        b.getString("delegatedEndDate"));
                list.add(temp);
            }
        } catch (Exception e) {
            Log.e("List Staff", "JSONArray error");
        }
        return list;
    }

    @Override
    public String toString() {
        return staffName;
    }

}
