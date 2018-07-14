package com.bazingaturk.data.entity.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class RewardLog {
    @Id
    @GenericGenerator(name="system-uuid",strategy="uuid")
    @GeneratedValue(generator = "system-uuid")
    @Column(length = 32)
    private String id;
    private String date;
    private double money;

}
