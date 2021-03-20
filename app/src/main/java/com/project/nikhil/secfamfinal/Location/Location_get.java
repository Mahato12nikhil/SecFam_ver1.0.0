package com.project.nikhil.secfamfinal.Location;

public class Location_get {
  Double latitude,longitude;

  public Location_get(Double latitude, Double longitude) {
    this.latitude = latitude;
    this.latitude = longitude;
  }
  public Location_get(){}

  public Double getlatitude() {
    return latitude;
  }

  public void setlatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getlongitude() {
    return longitude;
  }

  public void setlongitude(Double longitude) {
    this.longitude =longitude;
  }
}
