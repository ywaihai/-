package com.waihai.usercenter.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -2063588657041818450L;
    private String userAccount;
    private  String userPassword;
}
