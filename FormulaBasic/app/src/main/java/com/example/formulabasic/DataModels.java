package com.example.formulabasic;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

class Circuit {
    public String circuitId;
    public String circuitName;
    public String url;
    public Location Location;
}

class Location {
    public String locality;
    public String country;
    public String lat;
    @SerializedName("long")
    public double lon;

}

class CircuitTable {
    public ArrayList<Circuit> Circuits;
}

class MRData {
    public CircuitTable CircuitTable;
}

class ErgastResponse {
    public MRData MRData;
}

class Driver {
    public String driverId;
    public String url;
    public String givenName;
    public String familyName;
    public String dateOfBirth;
    public String nationality;
}

class DriverTable {
    public ArrayList<Driver> Drivers;
}

class MRDataDriver {
    public String xmlns;
    public String url;
    public String limit;
    public String offset;
    public String total;
    public DriverTable DriverTable;
}
class ErgastDriverResponse {
    public MRDataDriver MRData;
}