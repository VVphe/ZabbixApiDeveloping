# 周报
## prometheus调研
- 存储机制
    原始抓取数据格式如下：  
    `Metric name{Lables} Timestamp Value`  
    例如  
    `http_requests_total{status="200",method="GET"} @1434317560938 94355`  
    时间序列的存储被设计为key-value格式,即MetricName+Lables+Timestamp=key,Value=value;所有数值都是64bit,即64bit timestamp+64bit value  
    进一步将Metric name与Lables合并为如下格式：  
    `{__name__="Metric name",Lables}`  
    例如  
    `{__name__=http_requests_total,status="200",method="GET"} @1434317560938 94355`  
    以上就是数据在prometheus内部的表现形式
- 安装部署
    1. 镜像拉取`docker pull prom/prometheus:latest`
    2. 配置文件(设定抓取目标、时间间隔等)
        ```
        global:
            scrape_interval:     15s # By default, scrape targets every 15 seconds.
            external_labels:
            monitor: 'codelab-monitor'

        scrape_configs:

        - job_name: 'prometheus'
            scrape_interval: 5s

            static_configs:
        - targets: ['localhost:9090']

        - job_name: 'linux'
            scrape_interval: 5s

            static_configs:
        - targets: ['202.120.167.198:9100']
            labels:
            instance: linux
        ```
    3. Dockerfile
        ```
        FROM prom/prometheus
        ADD prometheus.yaml /etc/prometheus
        ```
    4. build&&run
        ```
        docker build -t my-prometheus
        docker run -p 9090:9090 my-prometheus
        ```
    5. 被监控端安装exporter
        ```
        docker pull prom/node-exporter
        docker run -d -p 9100:9100 prom/node-exporter
        ```
    6. prometheus端9090端口查看抓取到的数据
    ![prometheus](https://raw.githubusercontent.com/VVphe/ZabbixApiDeveloping/master/Documents/pictures/prometheus.png)
        
## zabbix api二次开发
目前已实现如下接口:apiVersion()查看zabbix api版本、login()用户登录、getHostItems()获取指定名称host的监控项,并逐项进行了测试;通过getHostItems()获取的items信息保存在服务器influxdb中,存储情况如下：
![influxdb](https://github.com/VVphe/ZabbixApiDeveloping/blob/master/Documents/pictures/influxdb.png)

## 项目链接
[ZabbixApi Developing](https://github.com/VVphe/ZabbixApiDeveloping)