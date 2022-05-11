package com.utopiapp.demo.dto;

import java.util.Map;

public class JwtDto {

    private String token;
    private Map<String, Object> client;

    public JwtDto(String token, Map<String, Object> client) {
        this.token = token;
        this.client = client;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map<String, Object> getClient() {
        return client;
    }

    public void setClient(Map<String, Object> client) {
        this.client = client;
    }
}
