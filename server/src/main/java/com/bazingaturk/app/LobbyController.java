package com.bazingaturk.app;

import com.bazingaturk.business.service.TaskSvc;
import com.bazingaturk.business.service.UserSvc;
import com.bazingaturk.business.vo.taskVO.TaskSimpleVO;
import com.bazingaturk.business.vo.userVO.UserInfoVO;
import com.bazingaturk.data.entity.user.UserInfo;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/lobby")
public class LobbyController {

    private final TaskSvc taskSvc;
    private final UserSvc userSvc;

    @Autowired
    public LobbyController(TaskSvc taskSvc, UserSvc userSvc) {
        this.taskSvc = taskSvc;
        this.userSvc = userSvc;
    }


    //获得任务大厅的所有任务
    @RequestMapping(value = "/")
    public Object getTasks(HttpServletRequest request){

        Map<String, Object> map = new HashMap<>();
        try{
            map.put("success",true);
            List<TaskSimpleVO> list = taskSvc.getTasks();
            for(TaskSimpleVO vo : list){
                String requesterId =  vo.getRequesterId();

                UserInfoVO info = userSvc.getUserInfo(requesterId);

                String requesterName = userSvc.getUserInfo(requesterId).getName();
                vo.setRequesterName(requesterName);
            }
            map.put("data", list);
        } catch (Exception e) {
            map.put("success",false);
            map.put("data",e.getStackTrace());
        }

        return map;
    }
}
