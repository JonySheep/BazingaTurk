package com.bazingaturk.business;

import com.bazingaturk.business.service.RequesterSvc;
import com.bazingaturk.business.vo.taskVO.TaskSimpleVO;
import com.bazingaturk.business.vo.userVO.UserInfoVO;
import com.bazingaturk.data.entity.tasks.Task;
import com.bazingaturk.data.entity.user.UserInfo;
import com.bazingaturk.data.repository.TaskRepository;
import com.bazingaturk.data.repository.UserInfoRepository;
import com.bazingaturk.util.enums.TaskStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequesterSvcImpl implements RequesterSvc {
    private final UserInfoRepository infoRepository;
    private final TaskRepository taskRepository;
    @Autowired
    public RequesterSvcImpl(UserInfoRepository infoRepository,TaskRepository taskRepository) {
        this.infoRepository=infoRepository;
        this.taskRepository=taskRepository;
    }

    @Override
    public List<TaskSimpleVO> getOngoingTasks(String userId){
        List<Task> tasks=taskRepository.findByRequesterId(userId);
        if(tasks==null || tasks.size()<1)
            return null;
        List<TaskSimpleVO> ongoing = new ArrayList<>();
        for(Task task:tasks){
            if(task.getStatus().equals(TaskStatusEnum.ONGOING))
                ongoing.add(new TaskSimpleVO(task));
        }
        return ongoing;
    }

    @Override
    public List<TaskSimpleVO> getFinishedTasks(String userId){
        List<Task> tasks=taskRepository.findByRequesterId(userId);
        if(tasks==null || tasks.size()<1)
            return null;
        List<TaskSimpleVO> finished = new ArrayList<>();
        for(Task task:tasks){
            if(task.getStatus().equals(TaskStatusEnum.FINISHED))
                finished.add(new TaskSimpleVO(task));
        }
        return finished;
    }

    @Override
    public List<UserInfoVO> getRequesterInfoList(){
        List<UserInfo> infos=infoRepository.findByRole("Requester");
        if(infos==null || infos.size()<1)
            return null;
        List<UserInfoVO> vos=new ArrayList<>();
        for(UserInfo i:infos){
            vos.add(new UserInfoVO(i));
        }
        return vos;
    }


}
