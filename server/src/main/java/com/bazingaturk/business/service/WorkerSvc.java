package com.bazingaturk.business.service;

import com.bazingaturk.business.vo.taskVO.AssignmentVO;
import com.bazingaturk.business.vo.userVO.UserInfoVO;
import com.bazingaturk.business.vo.userVO.WorkerVO;
import com.bazingaturk.data.entity.tasks.Assignment;
import com.bazingaturk.data.entity.user.UserInfo;
import com.bazingaturk.util.enums.ResultMessageEnum;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Service
public interface WorkerSvc {

    /**
     * 获取指定工人正在进行的任务
     * @param userId
     * @return
     */
    List<AssignmentVO> getOngoingAssignments(String userId);

    /**
     * 获取指定工人已完成的任务
     * @param userId
     * @return
     */
    List<AssignmentVO> getFinishedAssignments(String userId);


    int getWorkerRank(String userId);

    List<String> getWorkerRewardLog(String userId);

    List<UserInfoVO> getWorkerInfoList();

    WorkerVO getWorker(String userId) throws ParseException;

    double getTodayReward(String workerId);


}
