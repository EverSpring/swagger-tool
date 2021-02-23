package com.everspring.annotatioin;


public enum SpringControllerAnnotation {

    CONTROLLER("Controller", "org.springframework.stereotype.Controller"),
    REST_CONTROLLER("RestController", "org.springframework.web.bind.annotation.RestController"),
    FEIGN_CLIENT("FeignInterface", "org.springframework.cloud.openfeign.FeignClient");

    SpringControllerAnnotation(String shortName, String qualifiedName) {
        this.shortName = shortName;
        this.qualifiedName = qualifiedName;
    }

    private String shortName;
    private String qualifiedName;

    public String getQualifiedName() {
        return qualifiedName;
    }

    public String getShortName() {
        return shortName;
    }

}