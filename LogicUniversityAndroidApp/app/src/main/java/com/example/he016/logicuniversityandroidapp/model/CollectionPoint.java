package com.example.he016.logicuniversityandroidapp.model;

import android.util.Log;


import com.example.he016.logicuniversityandroidapp.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollectionPoint extends HashMap<String, String> {

    //static String host = "172.23.200.41";
    static String baseURL;

    static {
        baseURL = String.format("%s/api/department", JSONParser.RoutePrefix);
    }

    public String collectionPointId, collectionPointDescription;

    public CollectionPoint(String collectionPointId, String collectionPointDescription) {
        this.collectionPointId = collectionPointId;
        this.collectionPointDescription = collectionPointDescription;
    }

    public static List<CollectionPoint> ListAllCollectionPoints() {
        List<CollectionPoint> list = new ArrayList<CollectionPoint>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(baseURL + "/ListAllCollectionPoints");
        try {
            for (int i = 0; i < a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                list.add(new CollectionPoint(b.getString("collectionPointId"),
                        b.getString("collectionPointDescription")));

            }
        } catch (Exception e) {
            Log.e("CollectionPointList", "JSONArray error");
        }
        return list;
    }

    @Override
    public String toString() {
        return collectionPointDescription;
    }
}
