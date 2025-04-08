package com.example.he016.logicuniversityandroidapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.he016.logicuniversityandroidapp.model.RequestDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CartSession {
    SharedPreferences cart;

    public CartSession(Context cntx) {
        cart = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setRequestDetail(String itemNo, int quantityNeed) {
        cart.edit().putString("itemNo", itemNo).commit();
        cart.edit().putInt("quantityNeed", quantityNeed).commit();
    }

    public RequestDetail getRequestDetail() {
        String itemNo = cart.getString("itemNo","");
        Integer need = cart.getInt("quantityNeed",0);
        String quantityNeed = Integer.toString(need);
        RequestDetail rd = new RequestDetail(null, null, itemNo, quantityNeed,
                null, null, null, null, null);
        return rd;
    }

    public <RequestDetail> void saveCartArrayList (ArrayList<RequestDetail> cartrds, String key) {
        Gson gson = new Gson();
        String json = gson.toJson(cartrds);
        cart.edit().putString(key, json);
        cart.edit().commit();
    }


    public ArrayList<RequestDetail> getCartArrayList(String key){

        Gson gson = new Gson();
        String json = cart.getString(key, null);
        Type type = new TypeToken<ArrayList<RequestDetail>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void deleteCartRequestDetail (String itemNo){
        cart.edit().remove(itemNo);
    }
}

