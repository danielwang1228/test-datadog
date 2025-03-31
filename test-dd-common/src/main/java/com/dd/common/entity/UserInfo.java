package com.dd.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "user_info")
@Data
public class UserInfo {
    @Id
    private Long id;
    private String name;
    private Integer age;
    private String address;
}
