package com.bazingaturk.business.vo.taskVO;

import com.bazingaturk.data.entity.tasks.Assignment;
import com.bazingaturk.data.entity.tasks.Task;
import com.bazingaturk.util.enums.AssignmentStatusEnum;
import com.bazingaturk.util.enums.TaskTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentVO {

    //任务id
    private String taskId;

    //任务名字
    private String taskName;

    //任务类型
    private TaskTypeEnum taskType;

    //任务简述
    private String description;

    //发起者id
    private String requesterId;

    //发起者名称
    private String requesterName;

    //分组id
    private String partitionId;

    //分组编号
    private int partitionNo;

    //作业id
    private String assignmentId;

    //工作者id
    private String workerId;

    //该作业要做的数量
    private Integer size;

    //该作业目前已完成的数量
    private Integer progress;

    //该作业的状态
    private AssignmentStatusEnum status;

    //最迟完成时间
    private String deadline;

    //一份作业的奖励
    private Double assignmentReward;

    private double accuracy=0.0;

    public AssignmentVO(Task t, Assignment a) {
        this.taskId = t.getTaskId();
        this.taskName = t.getTaskName();
        this.taskType = t.getTaskType();
        this.description = t.getDescription();
        this.requesterId = t.getRequesterId();
        //requesterName 等待赋值
        this.partitionId = a.getPartitionId();
        //partitionNo 等待赋值
        this.assignmentId = a.getAssignmentId();
        this.workerId = a.getWorkerId();
        this.size = a.getSize();
        this.progress = a.getProgress();
        this.status = a.getStatus();
        this.deadline = a.getDeadline();
        this.assignmentReward = a.getAssignmentReward();
        this.accuracy=a.getAccuracy();
    }
}
