package com.bazingaturk.app;

import com.bazingaturk.business.service.TaskSvc;
import com.bazingaturk.business.service.UserSvc;
import com.bazingaturk.business.service.WorkerSvc;
import com.bazingaturk.business.vo.taskVO.AssignmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value="/worker")
public class WorkerController {

    private final WorkerSvc workerSvc;
    private final UserSvc userSvc;
    private final TaskSvc taskSvc;

    @Autowired
    WorkerController(WorkerSvc workerSvc, UserSvc userSvc, TaskSvc taskSvc) {
        this.workerSvc=workerSvc;
        this.userSvc = userSvc;
        this.taskSvc = taskSvc;
    }

    @RequestMapping(value="/{user_id}",method = GET)
    public Object getWorker(@PathVariable("user_id")String userId){
        Map<String,Object> map=new HashMap<>();
        try {
            map.put("data",workerSvc.getWorker(userId));
            map.put("success",true);
        } catch (ParseException e) {
            map.put("success",false);
            map.put("data",null);
        }
        return map;
    }
    @RequestMapping(value = "/workers", method = GET)
    public Object getWorkerList(){
        Map<String,Object> map = new HashMap<>();
        try {
            map.put("data",workerSvc.getWorkerInfoList());
            map.put("success",true);
        } catch (Exception e) {
            map.put("data",e.getStackTrace());
            map.put("success",false);
        }
        return map;
    }
    @RequestMapping(value = "/{user_id}/ongoing", method = GET)
    public Object getOngoingAssignments(@PathVariable("user_id") String userId){
        List<AssignmentVO> list = workerSvc.getOngoingAssignments(userId);
        return getAssignmentsVOsHelper(list);
    }

    @RequestMapping(value = "/{user_id}/finished", method = GET)
    public Object getFinishedAssignments(@PathVariable("user_id") String userId){
        List<AssignmentVO> list = workerSvc.getFinishedAssignments(userId);
        return getAssignmentsVOsHelper(list);
    }

    @RequestMapping(value="/{user_id}/rank",method=GET)
    public Object getWorkerRank(@PathVariable("user_id") String userId){
        Map<String,Object> map=new HashMap<>();
        try{
            map.put("data",workerSvc.getWorkerRank(userId));
            map.put("success",true);
        }catch(Exception e){
            map.put("data",e.getStackTrace());
            map.put("success",false);
        }
        return map;
    }

    @RequestMapping(value="/{user_id}/rewards",method=GET)
    public Object getWorkerRewardLog(@PathVariable("user_id")String userId){
        Map<String,Object> map=new HashMap<>();
        try{
            map.put("data",workerSvc.getWorkerRewardLog(userId));
            map.put("success",true);
        }catch(Exception e){
            map.put("data",e.getStackTrace());
            map.put("success",false);
        }
        return map;
    }

    @RequestMapping(value="/{user_id}/reword",method = GET)
    public Object getTodayReward(@PathVariable("user_id")String userId){
        Map<String,Object> map=new HashMap<>();
        map.put("data",workerSvc.getTodayReward(userId));
        map.put("success",true);
        return map;
    }

    private Map<String, Object> getAssignmentsVOsHelper(List<AssignmentVO> list) {
        Map<String, Object> map = new HashMap<>();

        if(list != null) {
            for (AssignmentVO vo : list) {
                String name = userSvc.getUserInfo(vo.getRequesterId()).getName();
                vo.setRequesterName(name);

                int no = taskSvc.getPartitionNo(vo.getPartitionId());
                vo.setPartitionNo(no);
            }
        }
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
