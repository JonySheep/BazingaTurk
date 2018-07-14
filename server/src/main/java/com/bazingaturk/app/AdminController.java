package com.bazingaturk.app;

import com.bazingaturk.business.service.RequesterSvc;
import com.bazingaturk.business.service.TaskSvc;
import com.bazingaturk.business.service.WorkerSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final TaskSvc taskSvc;
    private final WorkerSvc workerSvc;
    private final RequesterSvc requesterSvc;

    @Autowired
    public AdminController(TaskSvc taskSvc, WorkerSvc workerSvc,RequesterSvc requesterSvc) {
        this.taskSvc = taskSvc;
        this.workerSvc = workerSvc;
        this.requesterSvc=requesterSvc;
    }

    @RequestMapping(value = "/tasks", method = GET)
    public Object getTasks() {
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("data", taskSvc.getTasks());

        return map;
    }

    @RequestMapping(value="/tasks/ongoing",method=GET)
    public Object getOngoingTasks(){
        Map<String,Object> map=new HashMap<>();
        map.put("data",taskSvc.getOngoingTasks());
        map.put("success",true);
        return map;

    }

    @RequestMapping(value="/tasks/finished",method=GET)
    public Object getFinishedTasks(){
        Map<String,Object> map=new HashMap<>();
        map.put("data",taskSvc.getFinishedTasks());
        map.put("success",true);
        return map;

    }



    @RequestMapping(value = "/workers", method = GET)
    public Object getWorkerInfos() {
        Map<String, Object> map = new HashMap<>();
        map.put("data", workerSvc.getWorkerInfoList());
        map.put("success", true);
        return map;
    }

    @RequestMapping(value="/requesters",method=GET)
    public Object getRequesterInfos(){
        Map<String,Object> map=new HashMap<>();
        map.put("data",requesterSvc.getRequesterInfoList());
        map.put("success",true);
        return map;
    }


}
