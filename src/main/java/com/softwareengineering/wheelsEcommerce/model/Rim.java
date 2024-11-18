package com.softwareengineering.wheelsEcommerce.model;

import jakarta.persistence.*;

@Entity
public class Rim extends Product {

    private String diameter; // Example: 17", 18"
    private String material; // Example: Alloy, Steel

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDiameter() {
        return diameter;
    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;
    }
}