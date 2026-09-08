package com.example.booking.dto;

public class ResourceResponse {

    private Long id;
    private String name;
    private String type;
    private boolean available;

    public ResourceResponse() {
    }

    public ResourceResponse(
            Long id,
            String name,
            String type,
            boolean available) {

        this.id = id;
        this.name = name;
        this.type = type;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}