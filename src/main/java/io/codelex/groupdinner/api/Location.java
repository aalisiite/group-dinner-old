package io.codelex.groupdinner.api;

public class Location {

    private String addressLine;
    private Integer streetNumber;

    public Location(String addressLine, Integer streetNumber) {
        this.addressLine = addressLine;
        this.streetNumber = streetNumber;
    }
}
