package com.example.demo.api;

import com.example.demo.entity.SpaService;
import com.example.demo.entity.request.SpaServiceRequest;
import com.example.demo.service.BeautyCareService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name="api")
@RequestMapping("api/service")

public class SpaServiceApi {

    @Autowired
    BeautyCareService beautyCareService;

    @PostMapping
    public ResponseEntity createSpaService(@RequestBody SpaServiceRequest spaServiceRequest) {
       SpaService spaService = beautyCareService.createCareService(spaServiceRequest);
        return ResponseEntity.ok(spaService);
    }

    @GetMapping
    public ResponseEntity getSpaService() {
        List<SpaService> spaServices = beautyCareService.getALLService();
        return ResponseEntity.ok(spaServices);
    }

    @PutMapping("{id}")
    public ResponseEntity updateSpaService(@PathVariable long id, @RequestBody SpaServiceRequest spaServiceRequest) {
        SpaService spaService = beautyCareService.updateSpaService(id,spaServiceRequest);
        return ResponseEntity.ok(spaService);
    }

    @GetMapping("{id}")
    public ResponseEntity getSpaServiceById(@PathVariable long id) {
        SpaService spaService = beautyCareService.getSpaServiceById(id);
        return ResponseEntity.ok(spaService);

    }
    @DeleteMapping("{id}")
    public ResponseEntity DeleteServiceById(@PathVariable long id) {
        SpaService spaService = beautyCareService.DeleteServiceById(id);
        return ResponseEntity.ok(spaService);
    }

}
