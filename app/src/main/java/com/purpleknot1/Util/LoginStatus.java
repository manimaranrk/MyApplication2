package com.purpleknot1.Util;

import com.purpleknot1.DTO.LoginStatusDTO;
import com.purpleknot1.DTO.LoginStatusDTO;

import lombok.Getter;
import lombok.Setter;

public class LoginStatus {

    private static LoginStatusDTO mInstance = null;

    @Getter
    @Setter
    private LoginStatusDTO loginStatusDTO;

    private LoginStatus() {
        loginStatusDTO = new LoginStatusDTO();
    }

    public static LoginStatusDTO getInstance() {
        if (mInstance == null) {
            mInstance = new LoginStatusDTO();
        }
        return mInstance;
    }


}