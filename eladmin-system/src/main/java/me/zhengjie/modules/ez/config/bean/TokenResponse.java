package me.zhengjie.modules.ez.config.bean;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    @JsonProperty("msg")
    private String msg;

    @JsonProperty("code")
    private String code;

    @JsonProperty("data")
    private TokenData data;
}