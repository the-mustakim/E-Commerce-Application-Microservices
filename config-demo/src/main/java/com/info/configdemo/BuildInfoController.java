package com.info.configdemo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BuildInfoController {

    @Value("${build.id:default}")
    private String buildId;

    @Value("${build.version}")
    private String buildVersion;

    @Value("${build.name}")
    private String buildName;

    @Value("${build.type}")
    private String buildType;

    @GetMapping("/build-info")
    public String getBuildInfo(){
        return "Build Id: " + buildId + ", Version: " + buildVersion + ", Name: " + buildName + ", Type: " + buildType;
    }
}
