package com.purpleknot1.Util;

import com.purpleknot1.DTO.SelectedDateDTO;

import lombok.Getter;
import lombok.Setter;

public class SelectedDate {

    private static SelectedDateDTO mInstance = null;

    @Getter
    @Setter
    private SelectedDateDTO selectedDateDTO;

    private SelectedDate() {
        selectedDateDTO = new SelectedDateDTO();
    }

    public static SelectedDateDTO getInstance() {
        if (mInstance == null) {
            mInstance = new SelectedDateDTO();
        }
        return mInstance;
    }

}