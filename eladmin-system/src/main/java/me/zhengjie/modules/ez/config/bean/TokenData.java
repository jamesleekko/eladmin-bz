package me.zhengjie.modules.ez.config.bean;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenData {
    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("expireTime")
    private Long expireTime;
}