package com.mcredit.model.dto.warehouse;

import java.util.List;

public class CavetInfor {

    private Long id;
    private String brand;
    private String modelCode;
    private String color;
    private String engine;
    private String chassis;
    private String nPlate;
    private String cavetNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getChassis() {
        return chassis;
    }

    public void setChassis(String chassis) {
        this.chassis = chassis;
    }

    public String getnPlate() {
        return nPlate;
    }

    public void setnPlate(String nPlate) {
        this.nPlate = nPlate;
    }

    public String getCavetNumber() {
        return cavetNumber;
    }

    public void setCavetNumber(String cavetNumber) {
        this.cavetNumber = cavetNumber;
    }

}
