package com.purpleknot1.Util;

import com.purpleknot1.DTO.ImageCountDto;
import com.purpleknot1.DTO.ImageCountDto;

import lombok.Getter;
import lombok.Setter;


public class Image {

    private static ImageCountDto mInstance = null;

    @Getter
    @Setter
    private ImageCountDto surveyReportDto;

    private Image() {
        surveyReportDto = new ImageCountDto();
    }

    public static ImageCountDto getInstance() {
        if (mInstance == null) {
            mInstance = new ImageCountDto();
        }
        return mInstance;
    }

}
