/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lavidatec.template.controller;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Suhmian
 */
public class test {
    
    public static void main(String[] args) throws Exception {
        JSONArray jsonArr = new JSONArray();
        JSONObject obj = new JSONObject();
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("publicKey", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAdJunmw5NVQ4vcGK7krQ/kmVUEqT1SzZEDqqsR7cmeC+vKt4OMAervDTcv9ac9I6U60XaUB9VT5AkfocHUOvNIxFoI6mhCI6u7T7GWW8zxuEF4siwLbyrP+tVHQ/n4sSvGjrhrkEguFD3k6wePk8ONzvwQ2gGDDRnf4wrbX7oCwIDAQAB");
        obj.put("map", jsonObj);
        jsonArr.put(obj);
        System.out.println(jsonArr);
        JSONObject j = new JSONObject(jsonArr.getJSONObject(0).get("map").toString());
        System.out.println(j.get("publicKey"));
    }
}
