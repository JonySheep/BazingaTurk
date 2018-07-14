package com.bazingaturk.business.service;

import com.bazingaturk.business.vo.taskVO.AssignmentVO;
import com.bazingaturk.util.enums.ResultMessageEnum;
import org.springframework.stereotype.Service;

@Service
public interface AssignmentSvc {

    /**
     * 工人接受任务
     * @param workerId
     * @param taskId
     * @return
     */
    public ResultMessageEnum receiveAssignment(String workerId, String taskId);

    /**
     * 获得一份作业的详情
     * @param assignmentId
     * @return
     */
    public AssignmentVO getAssignment(String assignmentId);


    /**
     * 增加作业的进度
     * @param assignmentId
     * @return
     */
    public ResultMessageEnum progressAssignment(String assignmentId);

    /**
     * 工人提交作业
     * @param assignmentId
     * @return
     */
    public ResultMessageEnum submitAssignment(String assignmentId);

    /**
     * 任务审核通过
     * @param assignmentId
     * @return
     */
    public ResultMessageEnum approveAssignment(String assignmentId);

    /**
     * 任务审核失败
     * @param assignmentId
     * @return
     */
    public ResultMessageEnum rejectAssignment(String assignmentId);

    public ResultMessageEnum beginAssignment(String assignmentId);



}
