package com.twiagle.dispike.common.vo;

import com.twiagle.dispike.common.validator.IsPhoneNumber;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class LoginVo {
    @NotNull
    @IsPhoneNumber
    private String phoneNumber;

    @NotNull
    @Length(min = 32, max = 32)
    private String password;
}
