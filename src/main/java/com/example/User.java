package com.example;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class User {

    @Id
    private Integer id;

    private String username;

    private String email;

    private String name;

    private String password;

    private Integer enabled;

}
