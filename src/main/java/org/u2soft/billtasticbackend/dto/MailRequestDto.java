package org.u2soft.billtasticbackend.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MailRequestDto {

    @Pattern(
            regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$",
            message = "Mail adresi hatalı"
    )
    private String email;

    private String password;
    private String repeatPassword;
    private String date;
    private String time;
}
