package com.example.WebApplication.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Data
public class SelectionRequest {

    private String type;
    private int option;
}
