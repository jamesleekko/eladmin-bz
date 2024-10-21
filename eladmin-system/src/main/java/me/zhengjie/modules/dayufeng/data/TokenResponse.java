package me.zhengjie.modules.dayufeng.data;

import lombok.Data;

@Data
public class TokenResponse {
    private int code;
    private String msg;
    private TokenData data;
}
