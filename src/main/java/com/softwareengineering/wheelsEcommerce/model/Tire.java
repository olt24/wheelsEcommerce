package com.softwareengineering.wheelsEcommerce.model;

import jakarta.persistence.*;

@Entity
public class Tire extends Product {

    private String size; // Example: 225/45R17
    private String type; // Example: All-season

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}