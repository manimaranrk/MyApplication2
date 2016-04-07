package com.purpleknot1.DTO;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class StateDTO {

    String state;

    List<String> stateList = new ArrayList<String>();

}
