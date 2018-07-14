package com.bazingaturk.business.service;

import com.bazingaturk.business.vo.annoVO.AttributeVO;
import com.bazingaturk.business.vo.taskVO.PartitionVO;
import com.bazingaturk.business.vo.taskVO.TaskSimpleVO;
import com.bazingaturk.business.vo.taskVO.TaskCreateVO;
import com.bazingaturk.util.enums.ResultMessageEnum;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public interface TaskSvc {
    /**
     * 增加一个任务
     * @param newFile
     * @param parentName
     * @param taskCreateVO
     * @return
     */
    ResultMessageEnum createTask(File newFile, String parentName, TaskCreateVO taskCreateVO);

    /**
     * 获得任务的简略信息
     * @param taskId
     * @return
     */
    TaskSimpleVO getTaskBrief(String taskId);

    /**
     * 获得任务的详细信息
     * @param taskId
     * @return
     */
    List<AttributeVO> getTaskAttributes(String taskId);

    /**
     * 获得一个分组的序号
     * @param partitionId
     * @return
     */
    int getPartitionNo(String partitionId);

    /**
     * 获得一个任务的所有分组信息
     * @param taskId
     * @return
     */
    List<PartitionVO> getPartitions(String taskId);

    /**
     * 获得大厅任务列表
     * @return
     */
    List<TaskSimpleVO> getTasks();

    /**
     * 增加页面的浏览量
     * @param taskId
     * @return
     */
    ResultMessageEnum progressPageView(String taskId);

    /**
     * 整合一个分组
     *
     * @param taskId
     * @param partitionId
     * @return
     */
    ResultMessageEnum integratePartition(String taskId, String partitionId, String taskType);

    List<TaskSimpleVO> getOngoingTasks();

    List<TaskSimpleVO> getFinishedTasks();
}
