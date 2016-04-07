package com.purpleknot1.DTO;

/**
 * Created by Sridhar on 08-Sep-15.
 */
public class StatusDto {

    private String statusName;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public StatusDto(){

    }

    public StatusDto(String statusName){
        this.statusName = statusName;

    }

}
