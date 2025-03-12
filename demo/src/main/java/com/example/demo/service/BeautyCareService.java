package com.example.demo.service;

import com.example.demo.entity.SpaService;
import com.example.demo.entity.request.SpaServiceRequest;
import com.example.demo.exception.exceptions.NotFoundException;
import com.example.demo.repository.ServiceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class BeautyCareService {

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    ModelMapper modelMapper;

    public SpaService createCareService(SpaServiceRequest spaServiceRequest) {
        SpaService spaService = modelMapper.map(spaServiceRequest, SpaService.class);
        return serviceRepository.save(spaService);
    }

    public List<SpaService> getALLService() {
        return serviceRepository.findAll();
    }

    public SpaService getSpaServiceById(long id) {
        return serviceRepository.findById(id).orElseThrow(() -> new NotFoundException("spa not found"));
    }

    public SpaService updateSpaService(long id, SpaServiceRequest spaServiceRequest) {
        SpaService spaService = getSpaServiceById(id);
        spaService.setName(spaServiceRequest.getName());
        spaService.setDescription(spaServiceRequest.getDescription());
        spaService.setPrice(spaServiceRequest.getPrice());
        spaService.setDuration(spaServiceRequest.getDuration());
        return serviceRepository.save(spaService);

    }

    public SpaService DeleteServiceById(long id) {
        SpaService spaService = getSpaServiceById(id);
        spaService.setAvaliable(false);
        return serviceRepository.save(spaService);
    }
}
