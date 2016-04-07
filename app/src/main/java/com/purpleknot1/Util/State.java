package com.purpleknot1.Util;

import com.purpleknot1.DTO.StateDTO;

import lombok.Getter;
import lombok.Setter;

public class State {

    private static StateDTO mInstance = null;

    @Getter
    @Setter
    private StateDTO selectedDateDTO;

    private State() {
        selectedDateDTO = new StateDTO();
    }

    public static StateDTO getInstance() {
        if (mInstance == null) {
            mInstance = new StateDTO();
        }
        return mInstance;
    }


}