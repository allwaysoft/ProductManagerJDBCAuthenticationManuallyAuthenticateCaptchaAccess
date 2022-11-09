package com.example;

import lombok.Data;

@Data
public class UserDTO {

    private int id;

    private String email;
    @ValidPassword
    private String password;

    private String username;

    private String name;

    private String homepage;

    private int enabled;

    private String newPass;

    private String confirmPass;

}
