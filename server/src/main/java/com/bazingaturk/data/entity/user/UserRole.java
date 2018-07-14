package com.bazingaturk.data.entity.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
public class UserRole {
    //该角色的名称
    private String role;
    public UserRole(String role){
        this.role=role;
    }

}
