package org.u2soft.billtasticbackend.dto;

import lombok.Data;

@Data
public class MailRequestDto {
    private String email;
    private String password;
    private String repeatPassword;
    private String date;
    private String time;
}
