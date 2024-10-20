package com.example.customers_app_with_thymleaf.model;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @Builder
public class Product {
    private String id;
    private String name;
    private double price;
    private int quantity;
}
