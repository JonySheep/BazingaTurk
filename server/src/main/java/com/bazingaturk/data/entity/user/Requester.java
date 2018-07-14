package com.bazingaturk.data.entity.user;

import com.bazingaturk.data.entity.tasks.Assignment;
import com.bazingaturk.data.entity.tasks.Task;
import com.bazingaturk.util.enums.ResultMessageEnum;
import com.bazingaturk.util.enums.TaskStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Requester{
    @Id
    @GenericGenerator(name="system-uuid",strategy="uuid")
    @GeneratedValue(generator = "system-uuid")
    @Column(length = 32)
    private String id;
    private String userId;

    private double throughputRate;

    public Requester(String userId){
        this.userId=userId;
        this.throughputRate=0.0;
    }

}
