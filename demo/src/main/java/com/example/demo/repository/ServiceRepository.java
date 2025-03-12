package com.example.demo.repository;

import com.example.demo.entity.SpaService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<SpaService,Long> {


}
