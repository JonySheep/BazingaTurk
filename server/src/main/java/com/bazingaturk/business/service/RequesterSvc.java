package com.bazingaturk.business.service;

import com.bazingaturk.business.vo.taskVO.TaskSimpleVO;
import com.bazingaturk.business.vo.userVO.UserInfoVO;
import com.bazingaturk.data.entity.tasks.Task;
import com.bazingaturk.data.entity.user.UserInfo;
import com.bazingaturk.util.enums.ResultMessageEnum;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RequesterSvc {
    /**
     * 获取指定发布者正在进行的任务
     * @param userId
     * @return
     */
    List<TaskSimpleVO> getOngoingTasks(String userId);

    /**
     * 获取指定发布者已完成的任务
     * @param userId
     * @return
     */
    List<TaskSimpleVO> getFinishedTasks(String userId);

    /**
     * 获取发布者信息列表
     * @return
     */
    List<UserInfoVO> getRequesterInfoList();

}
