package com.purpleknot1.Util;

import com.purpleknot1.DTO.CityDTO;

import lombok.Getter;
import lombok.Setter;

public class City {

    private static CityDTO mInstance = null;

    @Getter
    @Setter
    private CityDTO cityDTO;

    private City() {
        cityDTO = new CityDTO();
    }

    public static CityDTO getInstance() {
        if (mInstance == null) {
            mInstance = new CityDTO();
        }
        return mInstance;
    }


}