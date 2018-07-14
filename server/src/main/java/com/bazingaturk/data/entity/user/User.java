package com.bazingaturk.data.entity.user;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GenericGenerator(name="system-uuid",strategy="uuid")
    @GeneratedValue(generator = "system-uuid")
    @Column(length = 32)
    private String userId;

    private String username;

    private String password;

    private String role;

    private boolean enabled;

    public User(User user) {
        this.userId=user.getUserId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.enabled=user.isEnabled();
    }
}
