package com.purpleknot1.Util;

import com.purpleknot1.DTO.EmployeeDetailsDto;




public class Employee {

    private static EmployeeDetailsDto mInstance = null;


  //  private EmployeeId employeeId;

    public static EmployeeDetailsDto getmInstance() {
        return mInstance;
    }

//    public static void setmInstance(EmployeeIDDto mInstance) {
//        EmployeeId.mInstance = mInstance;
//    }
//
//    public EmployeeId getEmployeeId() {
//        return employeeId;
//    }
//
//    public void setEmployeeId(EmployeeId employeeId) {
//        this.employeeId = employeeId;
//    }
//
//    private EmployeeId() {
//        employeeId = new EmployeeId();
//    }

    public static EmployeeDetailsDto getInstance() {
        if (mInstance == null) {
            mInstance = new EmployeeDetailsDto();
        }
        return mInstance;
    }
}
