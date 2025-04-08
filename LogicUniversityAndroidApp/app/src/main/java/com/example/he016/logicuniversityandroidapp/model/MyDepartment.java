package com.example.he016.logicuniversityandroidapp.model;

public class MyDepartment {
    public MyDepartment(String departmentId, String departmentName, String departmentRepName, String collectionPointDescription, String departmentPhone) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.departmentRepName = departmentRepName;
        this.collectionPointDescription = collectionPointDescription;
        this.departmentPhone = departmentPhone;
    }

    public String departmentId;
    public String departmentName;
    public String departmentRepName;
    public String collectionPointDescription;
    public String departmentPhone;

    @Override
    public String toString() {
        return departmentName;
    }
}
