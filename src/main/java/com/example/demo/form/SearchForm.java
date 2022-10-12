package com.example.demo.form;

import lombok.Data;

@Data
public class SearchForm {
    
    private String searchText;
    private Integer parent;
    private Integer child;
    private Integer grandChild;
    private String brand;

    
}
