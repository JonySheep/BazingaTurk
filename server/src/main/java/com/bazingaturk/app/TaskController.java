package com.bazingaturk.app;

import com.bazingaturk.WebAppConfig;
import com.bazingaturk.business.service.TaskSvc;
import com.bazingaturk.business.vo.taskVO.TaskCreateVO;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Api
@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskSvc taskSvc;

    @Autowired
    public TaskController(TaskSvc taskSvc) {
        this.taskSvc = taskSvc;
    }

    @ApiOperation(
            value = "增加一个任务",
            notes = "增加一个任务。接受上传任务配置，以TaskCreateVO指定。接受上传时的数据，是一个压缩包下根目录存在所有需标注图片的zip压缩包")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "图片压缩包", required = true, dataType = "MultipartFile"),
            @ApiImplicitParam(name = "taskStr", value = "TaskVO转化成的String", required = true, dataType = "String")})

    @RequestMapping(value = "/create",method = POST)
    //增加一个任务
    public Object createTask(@RequestParam(value = "file") MultipartFile file,
                             @RequestParam(value = "task") String taskStr){

        Map<String,Object> map = new HashMap<>();

        //解析TaskVO
        Gson gson = new Gson();
        TaskCreateVO taskCreateVO = gson.fromJson(taskStr, TaskCreateVO.class);

        //上传zip到临时文件夹
        String path = WebAppConfig.BASE + "/assets/tasks/temp/" + UUID.randomUUID().toString().replace("-","") + "/";
        String filename = file.getOriginalFilename();
        File newFile = new File(path, filename);

        if(!newFile.getParentFile().exists()){
            newFile.getParentFile().mkdirs();
        }

        try {
            if(newFile.createNewFile()){
                file.transferTo(new File(newFile.getAbsolutePath()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            map.put("success",false);
            map.put("data",e.getStackTrace());
            return map;
        }

        //调用Service层新建任务
        map.put("success",true);
        map.put("data",taskSvc.createTask(newFile,"Images", taskCreateVO));

        return map;
    }

    //TODO 完成一个任务
    @RequestMapping(value = "/{task_id}/finish",method = GET)
    public Object finishTask(@PathVariable("task_id") String task_id){
        return null;
    }

    //TODO 删除一个任务
    @RequestMapping(value = "/{task_id}/remove",method = GET)
    public Object removeTask(@PathVariable("task_id") String task_id){
        return null;
    }

    @ApiOperation(
            value = "返回一个任务的简略信息",
            notes = "返回一个任务的简略信息。不包括该任务的静态资源映射地址，以及需要标注的属性列表")
    @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String", paramType = "path")
    @RequestMapping(value = "/{task_id}/brief",method = GET)
    //返回一个任务的简略信息
    public Object getTaskBrief(@PathVariable("task_id") String taskId){
        Map<String,Object> map = new HashMap<>();

        map.put("success",true);
        map.put("data",taskSvc.getTaskBrief(taskId));

        return map;
    }

    @ApiOperation(
            value = "返回一个任务的属性列表",
            notes = "返回一个任务的属性列表。")
    @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String", paramType = "path")
    @RequestMapping(value = "/{task_id}/attributes",method = GET)
    //返回一个任务的属性列表
    public Object getTaskAttributes(@PathVariable("task_id") String taskId){
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        map.put("data",taskSvc.getTaskAttributes(taskId));

        return map;
    }

    @ApiOperation(
            value = "返回一个任务的分组列表",
            notes = "返回一个任务的分组列表。")
    @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String", paramType = "path")
    @RequestMapping(value = "/{task_id}/partitions",method = GET)
    //返回一个任务的分组列表
    public Object getPartitions(@PathVariable("task_id") String taskId){
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        map.put("data",taskSvc.getPartitions(taskId));

        return map;
    }

    //增加一个任务的浏览量
    @RequestMapping(value = "/{task_id}/view", method = GET)
    public Object progressPageView(@PathVariable("task_id") String taskId) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("data", taskSvc.progressPageView(taskId));

        return map;
    }

    //发起整合一个分组的请求
    @RequestMapping(value = "/{task_id}/{partition_id}/{task_type}/integration", method = GET)
    public Object integratePartition(@PathVariable("partition_id") String partitionId, @PathVariable("task_type") String taskType, @PathVariable("task_id") String taskId){
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        map.put("data",taskSvc.integratePartition(taskId ,partitionId, taskType));

        return map;
    }

    //获取一个任务图片的预览
    @RequestMapping(value = "{task_id}/preview")
    public Object previewTask(@PathVariable("task_id") String taskId){


        File file = new File(WebAppConfig.BASE + "/assets/tasks/t" + taskId + "/Images");
        List<String> fileList = new ArrayList<>();
        for(int i = 0; i< 5 && i<file.list().length ;i++){
            fileList.add("/server/tasks/t" + taskId + "/Images/" + file.list()[i]);
        }

        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        map.put("data",fileList);

        return map;
    }
}