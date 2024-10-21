package me.zhengjie.modules.dayufeng.data;

import lombok.Data;

@Data
public class TokenData {
    private String access_token;
    private String token_type;
    private Long expires_in;
}
