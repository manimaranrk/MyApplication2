package com.purpleknot1.Util;

import com.purpleknot1.DTO.CreateVenueDTO;

import lombok.Getter;
import lombok.Setter;


public class CreateVenue {

    private static CreateVenueDTO mInstance = null;

    @Getter
    @Setter
    private CreateVenueDTO createVenueDTO;

    private CreateVenue() {
        createVenueDTO = new CreateVenueDTO();
    }

    public static CreateVenueDTO getInstance() {
        if (mInstance == null) {
            mInstance = new CreateVenueDTO();
        }
        return mInstance;
    }
}
