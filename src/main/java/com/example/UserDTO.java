package com.example;

import lombok.Data;

@Data
public class UserDTO {

    private int id;

    private String email;

    private String password;

    private String username;

    private String homepage;

    private int enabled;

    private String newPass;

    private String confirmPass;

}
