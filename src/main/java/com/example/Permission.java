package com.example;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
