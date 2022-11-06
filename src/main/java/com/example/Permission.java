package com.example;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Permission {

    @Id
    private Long id;
    private String name;
    private String description;
    private String uri;
    private String method;

}
