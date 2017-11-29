package com.example.zabbix.dao;

import java.io.Serializable;
import java.util.Map;
import org.influxdb.*;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

@Repository
public class InfluxDBConnect implements InfluxDBRepository {

    private InfluxDB influxDB;

    public InfluxDBConnect() {
    }

    public InfluxDB getInfluxDB() {
        return influxDB;
    }

    public void setInfluxDB(InfluxDB influxDB) {
        this.influxDB = influxDB;
    }

    public InfluxDB influxBuilder(String username, String password, String database, String url){
        if(influxDB==null){
            influxDB=InfluxDBFactory.connect(url,username,password);
            if(!influxDB.databaseExists(database)) {
                influxDB.createDatabase(database);
            }
        }
        return influxDB;
    }

    @Override
    public void insert(String database,String measurement, Map<String, String> tags, Map<String, Object> fields) {
        Point.Builder builder=Point.measurement(measurement);
        builder.tag(tags);
        builder.fields(fields);
        influxDB.write(database,"",builder.build());
    }
}
