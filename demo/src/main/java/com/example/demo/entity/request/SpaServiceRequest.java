package com.example.demo.entity.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SpaServiceRequest {
    String name;
    String description;
    float price;
    int duration;
}
