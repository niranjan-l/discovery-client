package com.niranjan2021.discoveryclient.Controller;


import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.logging.Logger;

@RestController

@RequestMapping("/app/client/demo")
public class HomeController {

    public final  static Logger  logger = Logger.getLogger(String.valueOf(MethodHandles.lookup().lookupClass()));

    @Autowired
    DiscoveryClient discoveryClient;
    
    @Autowired
    EurekaClient eurekaClient;

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    @GetMapping
    public  String  callBusinessService(){
        List<ServiceInstance> service = discoveryClient.getInstances("service");
        String baseurl = service.get(0).getUri().toString();
         baseurl = baseurl.concat("/api/v1");
        logger.info("baseurl is "+baseurl);
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<String> responseEntity = restTemplate.exchange(baseurl, HttpMethod.GET, null, String.class);
        return responseEntity.getBody();
    }
    @GetMapping("/eureka/client")
    public  String  callBusinessServiceUsingEurekaClient(){
        InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("service", false);
        String homePageUrl = instanceInfo.getHomePageUrl();
        homePageUrl=   homePageUrl.concat("api/v1");
        logger.info(homePageUrl);
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<String> responseEntity = restTemplate.exchange(homePageUrl, HttpMethod.GET, null, String.class);
        return responseEntity.getBody();
    }


}
