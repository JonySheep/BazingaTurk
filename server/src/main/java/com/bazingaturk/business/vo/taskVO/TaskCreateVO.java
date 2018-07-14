package com.bazingaturk.business.vo.taskVO;

import com.bazingaturk.business.vo.annoVO.AttributeVO;
import com.bazingaturk.data.entity.tasks.Task;
import com.bazingaturk.util.enums.TaskStatusEnum;
import com.bazingaturk.util.enums.TaskTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于前端创建任务时和后端的交互
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreateVO {

    //任务名称
    private String taskName;
    //该任务的任务类别列表
    private TaskTypeEnum taskType;
    //发起者id
    private String requesterId;
    //任务描述
    private String description;
    //该任务的要分类的属性列表
    private List<AttributeVO> attributes;

    //任务需要多少个分组
    private int partTimes;
    //一个分组需要的作业次数
    private int assignTimes;
    //一份作业的奖励
    private double assignmentReward;
    //最小单元的平均限制时间
    private double unitLimitMinutes;
}
