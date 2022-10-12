package com.example.demo.domain;

import lombok.Data;

@Data
public class Search {

    private String searchText;
    private Integer parent;
    private Integer child;
    private Integer grandChild;
    private String brand;
    
}
