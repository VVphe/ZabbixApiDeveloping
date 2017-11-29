package com.example.zabbix.service;

import com.example.zabbix.dao.InfluxDBConnect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ZabbixService {

    @Autowired
    private InfluxDBConnect influxDBConnect;

    @Value("${influxdb.user}")
    private String user;
    @Value("${influxdb.password}")
    private String password;
    @Value("${influxdb.url}")
    private String url;
    @Value("${influxdb.database}")
    private String database;
    @Value("${influxdb.measurement}")
    private String measurement;

    public void connect(){
        //influxDBConnect.influxBuilder("zabbix","zabbix","zabbix","http://202.120.167.198:8086");
        influxDBConnect.influxBuilder(user,password,database,url);
    }

    public void insertEntity(List<List<String>> entity){
        for(List<String> Entity:entity){
            Map<String,String> tags=new HashMap<>();
            Map<String,Object> fields=new HashMap<>();
            tags.put("host","202.120.167.198");
            fields.put(Entity.get(0),Entity.get(1));
            //influxDBConnect.insert("zabbix","zabbix",tags,fields);
            influxDBConnect.insert(database,measurement,tags,fields);
        }
    }
}
