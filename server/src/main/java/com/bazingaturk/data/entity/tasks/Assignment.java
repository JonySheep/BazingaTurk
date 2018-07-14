package com.bazingaturk.data.entity.tasks;

import com.bazingaturk.util.enums.AssignmentStatusEnum;
import com.bazingaturk.util.enums.TaskTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Assignment {


    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    @Column(length = 32)
    //作业id
    private String assignmentId;

    @Column(length = 32)
    private String taskId;

    @Column(length = 32)
    //分组id
    private String partitionId;

    @Column(length = 32)
    //工作者id
    private String workerId;

    //该作业要做的数量
    private Integer size;

    //该作业目前已完成的数量
    private Integer progress;

    @Enumerated(EnumType.STRING)
    //该任务的状态
    private AssignmentStatusEnum status;

    //最迟完成时间
    private String deadline;

    //一份作业的奖励
    private Double assignmentReward;

    private String startTime;

    private String submitTime;

    private double accuracy=0.0;

}
