package com.servustech.carehome.info;


import java.util.List;

public class CountyDTO {
    private String county;
    private List<CityDTO> cities;

    // Getters and Setters

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public List<CityDTO> getCities() {
        return cities;
    }

    public void setCities(List<CityDTO> cities) {
        this.cities = cities;
    }

    // Inner class for CityDTO
    public static class CityDTO {
        private String name;
        private int count;

        // Getters and Setters

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
