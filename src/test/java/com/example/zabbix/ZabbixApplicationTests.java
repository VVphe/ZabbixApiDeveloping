package com.example.zabbix;

import com.example.zabbix.controller.ZabbixApi;
import com.example.zabbix.dao.InfluxDBConnect;
import com.example.zabbix.service.ZabbixService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZabbixApplicationTests {

	private ZabbixApi zabbixApi;
	@Autowired
	private ZabbixService zabbixService;

	@Before
	public void before(){
		String url="http://202.120.167.198:8088/api_jsonrpc.php";
		zabbixApi=new ZabbixApi(url);
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void zabbixVersionTest(){
		String version=zabbixApi.apiVersion();
		System.out.println(version);
	}

	@Test
	public void loginTest(){
		boolean auth=zabbixApi.login("Admin","zabbix");
		System.out.println(auth);
	}

	@Test
	public void getHostItemsTest(){
		String host="rjxy-dqf-web.tongji.edu.cn";
		zabbixApi.login("Admin","zabbix");
		List<List<String>> hostItems=zabbixApi.getHostItems(host);
		zabbixService.connect();
		zabbixService.insertEntity(hostItems);
		System.out.println(hostItems.size());
	}
}
