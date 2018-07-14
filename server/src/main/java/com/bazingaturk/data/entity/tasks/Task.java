package com.bazingaturk.data.entity.tasks;

import com.bazingaturk.business.vo.annoVO.AttributeVO;
import com.bazingaturk.business.vo.taskVO.TaskCreateVO;
import com.bazingaturk.data.entity.anno.Attribute;
import com.bazingaturk.util.enums.TaskStatusEnum;
import com.bazingaturk.util.enums.TaskTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Task {

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    @Column(length = 32)
    //任务id
    private String taskId;

    //任务名称
    private String taskName;

    //该任务的任务类别列表
    @Enumerated(EnumType.STRING)
    private TaskTypeEnum taskType;

    //发起者id
    private String requesterId;

    //任务创建时间
    private String createTime;

    //任务描述
    private String description;

    //任务状态
    @Enumerated(EnumType.STRING)
    private TaskStatusEnum status;

    //该任务要做的数量
    private Integer size;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //该任务的要分类的属性列表
    private List<Attribute> attributes;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //该任务的分组列表
    private List<Partition> partitions;

    //任务需要多少个分组
    private Integer partTimes;

    //任务的静态资源根目录
    private String path;

    //任务的浏览量
    private Long pageViews;

    //接受这个任务的人数
    private Long receiveTimes;

    //收藏这个任务的人数
    private Long staredTimes;

    //一份作业的奖励
    private Double assignmentReward = 0.0;

    //最小单元的平均限制时间
    private Double unitLimitMinutes = 0.0;

    public Task(TaskCreateVO v) {
        taskName = v.getTaskName();
        taskType = v.getTaskType();
        requesterId = v.getRequesterId();
        description = v.getDescription();
        status = TaskStatusEnum.ONGOING;

        attributes = new ArrayList<>();
        for (AttributeVO a : v.getAttributes()) {
            attributes.add(new Attribute(a));
        }

        partTimes = v.getPartTimes();

        pageViews = 0L;
        receiveTimes = 0L;
        staredTimes = 0L;

        assignmentReward = v.getAssignmentReward();
        unitLimitMinutes = v.getUnitLimitMinutes();
    }

    public List<AttributeVO> getAttributeVOs(){
        List<AttributeVO> attributeVOs = new ArrayList<>();
        for (Attribute a : attributes) {
            attributeVOs.add(new AttributeVO(a));
        }

        return attributeVOs;
    }
}
