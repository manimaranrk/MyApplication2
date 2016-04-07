package com.purpleknot1.Util;

import com.purpleknot1.DTO.ImageAttachmentDto;

import lombok.Getter;
import lombok.Setter;

public class ImageAttachment {

    private static ImageAttachmentDto mInstance = null;

    @Getter
    @Setter
    private ImageAttachmentDto image;

    private ImageAttachment() {
        image = new ImageAttachmentDto();
    }

    public static ImageAttachmentDto getInstance() {
        if (mInstance == null) {
            mInstance = new ImageAttachmentDto();
        }
        return mInstance;
    }

}
