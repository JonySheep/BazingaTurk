package com.bazingaturk.business.vo.taskVO;

import com.bazingaturk.data.entity.tasks.Task;
import com.bazingaturk.util.enums.TaskStatusEnum;
import com.bazingaturk.util.enums.TaskTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于在任务大厅，发起者的任务列表和后台简单显示的VO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskSimpleVO {

    //任务id
    private String taskId;
    //任务名称
    private String taskName = null;
    //该任务的任务类别列表
    private TaskTypeEnum taskType = null;
    //发起者id
    private String requesterId = null;
    //发起者名称
    private String requesterName = null;
    //任务创建时间
    private String createTime = null;
    //任务描述
    private String description = null;
    //任务状态
    private TaskStatusEnum status = null;
    //该任务要做的数量
    private int size = 0;

    //任务需要多少个分组
    private int partTimes = 0;
    //一个分组需要分多少次
    private int assignTimes = 0;
    //一份作业的奖励
    private double assignmentReward = 0.0;
    //最小单元的平均限制时间
    private double unitLimitMinutes = 0.0;
    //热度
    private double heat=0.0;

    public TaskSimpleVO(Task t) {
        this.taskId = t.getTaskId();
        this.taskName = t.getTaskName();
        this.taskType = t.getTaskType();
        this.requesterId = t.getRequesterId();
        //requesterName 等待被赋值
        this.createTime = t.getCreateTime();
        this.description = t.getDescription();
        this.status = t.getStatus();
        this.size = t.getSize();
        this.partTimes = t.getPartTimes();
        this.assignTimes = t.getPartitions().get(0).getAssignTimes();
        this.assignmentReward = t.getAssignmentReward();
        this.unitLimitMinutes = t.getUnitLimitMinutes();
        this.heat=0.0;
    }
}
