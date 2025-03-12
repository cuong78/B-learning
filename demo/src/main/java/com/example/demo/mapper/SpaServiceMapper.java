package com.example.demo.mapper;

import com.example.demo.entity.SpaService;
import com.example.demo.entity.request.SpaServiceRequest;
import org.modelmapper.PropertyMap;

public class SpaServiceMapper extends PropertyMap<SpaServiceRequest, SpaService> {
    @Override
    protected void configure() {
        map().setId(0);
    }

}
