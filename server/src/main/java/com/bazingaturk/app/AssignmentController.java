package com.bazingaturk.app;

import com.bazingaturk.WebAppConfig;
import com.bazingaturk.business.service.AnnoSvc;
import com.bazingaturk.business.service.AssignmentSvc;
import com.bazingaturk.business.service.UserSvc;
import com.bazingaturk.business.vo.taskVO.AssignmentVO;
import com.bazingaturk.security.MyUserDetails;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@RestController
@RequestMapping("/assignment")
public class AssignmentController {

    private final AssignmentSvc assignmentSvc;
    private final AnnoSvc annoSvc;
    private final UserSvc userSvc;

    @Autowired
    public AssignmentController(AssignmentSvc assignmentSvc, AnnoSvc annoSvc, UserSvc userSvc) {
        this.assignmentSvc = assignmentSvc;
        this.annoSvc = annoSvc;
        this.userSvc = userSvc;
    }


    //工人接受任务
    @RequestMapping(value = "/{task_id}/{worker_id}/accept", method = GET)
    public Object receiveAssignment(@PathVariable("worker_id") String workerId,
                                    @PathVariable("task_id") String taskId) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("data", assignmentSvc.receiveAssignment(workerId, taskId));
        return map;

    }

    //工人提交作业
    @RequestMapping(value = "/{assignment_id}/submit", method = GET)
    public Object submitAssignment(@PathVariable("assignment_id") String assignmentId) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("data", assignmentSvc.submitAssignment(assignmentId));
        return map;
    }

    //任务审核通过
    @RequestMapping(value = "/{assignment_id}/approve", method = GET)
    public Object approveAssignment(@PathVariable("assignment_id") String assignmentId) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("data", assignmentSvc.approveAssignment(assignmentId));
        return map;
    }

    //任务审核失败
    @RequestMapping(value = "/{assignment_id}/reject", method = GET)
    public Object rejectAssignment(@PathVariable("assignment_id") String assignmentId) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("data", assignmentSvc.rejectAssignment(assignmentId));
        return map;
    }

    //返回作业详情
    @RequestMapping(value = "/{assignment_id}", method = GET)
    public Object getAssignment(@PathVariable("assignment_id") String assignmentId) {
        Map<String, Object> map = new HashMap<>();
        AssignmentVO vo = assignmentSvc.getAssignment(assignmentId);
        String requesterName = userSvc.getUserInfo(vo.getRequesterId()).getName();
        vo.setRequesterName(requesterName);
        map.put("success", true);
        map.put("data", vo);
        return map;
    }

    //增加作业进度
    @RequestMapping(value = "/{assignment_id}/progress", method = GET)
    public Object progressAssignment(@PathVariable("assignment_id") String assignmentId) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("data", assignmentSvc.progressAssignment(assignmentId));
        return map;
    }

    //返回一个图的路径和标注
    @RequestMapping(value = "/{assignment_id}/{image_id}", method = GET)
    public Object getImageInfo(@PathVariable("assignment_id") String assignmentId,
                               @PathVariable("image_id") int imageId) {
        Map<String, Object> map = new HashMap<>();

        AssignmentVO a = assignmentSvc.getAssignment(assignmentId);

        map.put("data", annoSvc.getAnno(a.getTaskType(), a.getTaskId(), a.getPartitionId(), assignmentId, imageId));
        map.put("success", true);
        return map;
    }

    //接收一个图的标注结果
    @RequestMapping(value = "/{assignment_id}/{image_id}", method = POST)
    public Object putImageInfo(@PathVariable("assignment_id") String assignmentId,
                               @PathVariable("image_id") int imageId,
                               @RequestParam(value = "anno") String anno,
                               @RequestParam(value = "layer",required = false) MultipartFile layer) {   
        Map<String, Object> map = new HashMap<>();
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();

        AssignmentVO a = assignmentSvc.getAssignment(assignmentId);

        if (layer != null) {
            String path = WebAppConfig.BASE + "/assets/tasks/t" + a.getTaskId() + "/p" + a.getPartitionId() + "/a" + a.getAssignmentId() + "/" + imageId + ".png";
            File newFile = new File(path);
            if (newFile.exists()) {
                newFile.delete();
            }
            newFile = new File(path);
            try {
                if (newFile.getParent() != null && !new File(newFile.getParent()).exists()) {
                    new File(newFile.getParent()).mkdirs();
                }
                newFile.createNewFile();
                layer.transferTo(new File(newFile.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
                map.put("success", false);
                map.put("data", e.getStackTrace());
            }
        }

        map.put("data", annoSvc.putAnno(a.getTaskType(), a.getTaskId(), assignmentId, imageId, user.getUserId(), anno));
        map.put("success", true);
        return map;
    }

    @RequestMapping(value="/{assignment_id}/begin",method=GET)
    public Object beginAssignment(@PathVariable("assignment_id") String assignmentId){
        Map<String,Object> map=new HashMap<>();
        map.put("data",assignmentSvc.beginAssignment(assignmentId));
        map.put("success",true);
        return map;
    }
}