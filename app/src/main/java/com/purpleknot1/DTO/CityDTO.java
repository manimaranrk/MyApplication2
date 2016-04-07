package com.purpleknot1.DTO;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CityDTO {

    String city;

    List<String> cityList = new ArrayList<String>();

    int pos;

}
