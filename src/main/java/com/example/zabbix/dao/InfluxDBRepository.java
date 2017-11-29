package com.example.zabbix.dao;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface InfluxDBRepository {
    public void insert(String database,String measurement, Map<String,String> tags,Map<String,Object> fields);
}
