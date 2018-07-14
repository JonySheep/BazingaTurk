package com.bazingaturk.app;

import com.bazingaturk.business.service.RequesterSvc;
import com.bazingaturk.business.vo.taskVO.TaskSimpleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
@RestController
@RequestMapping(value="/requester")
public class RequesterController {

    private final RequesterSvc requesterSvc;

    @Autowired
    RequesterController(RequesterSvc requesterSvc) {
        this.requesterSvc=requesterSvc;
    }

    @RequestMapping(value = "/requesters", method = GET)
    public Object getRequesterList(){
        Map<String,Object> map = new HashMap<>();
        try {
            map.put("data",requesterSvc.getRequesterInfoList());
            map.put("success",true);
        } catch (Exception e) {
            map.put("data",e.getStackTrace());
            map.put("success",false);
        }
        return map;
    }

    @RequestMapping(value = "/{user_id}/ongoing", method = GET)
    public Object getOngoingTasks(@PathVariable("user_id") String userId){
        List<TaskSimpleVO> list = requesterSvc.getOngoingTasks(userId);
        return getTaskSimpleVOsHelper(list);
    }

    @RequestMapping(value = "/{user_id}/finished", method = GET)
    public Object getFinishedTasks(@PathVariable("user_id") String userId){
        List<TaskSimpleVO> list = requesterSvc.getFinishedTasks(userId);
        return getTaskSimpleVOsHelper(list);
    }


    private Map<String, Object> getTaskSimpleVOsHelper(List<TaskSimpleVO> list) {
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("data", list);
            map.put("success", true);
        } catch (Exception e) {
            map.put("data", e.getStackTrace());
            map.put("success", false);
        }
        return map;
    }
}
