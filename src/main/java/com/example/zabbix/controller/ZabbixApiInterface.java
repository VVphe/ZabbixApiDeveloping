package com.example.zabbix.controller;

import com.example.zabbix.entity.Request;
import com.google.gson.JsonObject;

import java.util.List;

public interface ZabbixApiInterface {
    public String apiVersion();
    public boolean login(String user,String password);
    public JsonObject call(Request request);
    public List<List<String>> getHostItems(String hostId);
}
