package com.example.zabbix.controller;

import com.example.zabbix.entity.Request;
import com.example.zabbix.entity.RequestBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class ZabbixApi implements ZabbixApiInterface{
    private String url;
    private String auth;

    public ZabbixApi(String url){
        this.url=url;
    }

    @Override
    public String apiVersion() {
        Request request=RequestBuilder.newBuilder().method("apiinfo.version").build();
        JsonObject response=this.call(request);
        return response.get("result").getAsString();
    }

    @Override
    public boolean login(String user, String password) {
        this.auth=null;
        Request request= RequestBuilder.newBuilder().paramEntry("user",user).paramEntry("password",password).method("user.login").build();
        JsonObject response=this.call(request);
        String auth=response.get("result").getAsString();
        if(auth!=null&&!auth.isEmpty()){
            this.auth=auth;
            return true;
        }
        return false;
    }

    @Override
    public JsonObject call(Request request){
        if(request.getAuth()==null){
            request.setAuth(this.auth);
        }

        HttpHeaders httpHeaders=new HttpHeaders();
        Gson gson=new Gson();
        RestTemplate restTemplate=new RestTemplate();

        MediaType type=MediaType.parseMediaType("application/json;charset=UTF-8");
        httpHeaders.setContentType(type);
        String requestJson=gson.toJson(request);
        HttpEntity<String> entity=new HttpEntity<String>(requestJson,httpHeaders);
        String result=restTemplate.postForObject(url,entity,String.class);

        JsonObject jsonObject=new JsonParser().parse(result).getAsJsonObject();
        return jsonObject;
    }

    @Override
    public List<List<String>> getHostItems(String host) {
        Request request=RequestBuilder.newBuilder().paramEntry("host",host).method("item.get").build();
        JsonObject response=this.call(request);
        JsonArray result=response.get("result").getAsJsonArray();

        Iterator iterator=result.iterator();
        List<List<String>> items=new ArrayList<List<String>>();
        while(iterator.hasNext()){
            JsonObject jsonElement=(JsonObject)iterator.next();
            List<String> itemProperty=new ArrayList<String>();
            String itemKey=jsonElement.get("key_").getAsString();
            String itemValue=jsonElement.get("lastvalue").getAsString();
            itemProperty.add(itemKey);
            itemProperty.add(itemValue);

            items.add(itemProperty);
        }

        if(!items.isEmpty()){
            return items;
        }
        return null;
    }
}
