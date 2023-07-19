package com.example.wastemanagementvrushabh;

public class Model {
    private String imageUri;
    String name, phone, latitude, longitude, uid,status,complaintId;
    boolean note;

    public Model() {

    }

//    public Model(String name, String phone, String location) {
//        this.name = name;
//        this.phone = phone;
//        this.location = location;
//    }
//
//    public Model(String imageUri) {
//        this.imageUri = imageUri;
//    }


    public Model(String imageUri, String name, String phone, String latitude, String longitude, String uid,String status,String complaintId, boolean note) {
        this.imageUri = imageUri;
        this.name = name;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.uid = uid;
        this.status = status;
        this.complaintId = complaintId;
        this.note = note;
    }

    public boolean isNote() {
        return note;
    }

    public void setNote(boolean note) {
        this.note = note;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
