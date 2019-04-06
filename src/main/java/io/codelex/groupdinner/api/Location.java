package io.codelex.groupdinner.api;

public class Location {

    private String addressLine;
    private Integer streetNumber;

    public Location(String addressLine, Integer streetNumber) {
        this.addressLine = addressLine;
        this.streetNumber = streetNumber;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public Integer getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
    }
}
