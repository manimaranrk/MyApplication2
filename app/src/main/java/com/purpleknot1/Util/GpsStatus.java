package com.purpleknot1.Util;


import com.purpleknot1.DTO.GpsStatusDTO;

import lombok.Getter;
import lombok.Setter;


public class GpsStatus {

    private static GpsStatusDTO mInstance = null;

    @Getter
    @Setter
    private GpsStatusDTO gpsStatusDTO;

    private GpsStatus() {
        gpsStatusDTO = new GpsStatusDTO();
    }

    public static GpsStatusDTO getInstance() {
        if (mInstance == null) {
            mInstance = new GpsStatusDTO();
        }
        return mInstance;
    }


}
