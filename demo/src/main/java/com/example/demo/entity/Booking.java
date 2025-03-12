package com.example.demo.entity;


import com.example.demo.enums.BookingEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;

    @ManyToMany(cascade = CascadeType.ALL)
            @JoinTable(name="booking_service",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
            )
    List<SpaService> services = new ArrayList<>();
    LocalDateTime  startTime;
    LocalDateTime  endTime;

    LocalDateTime  createdAt;

    @Enumerated(EnumType.STRING)
    BookingEnum status;



}
