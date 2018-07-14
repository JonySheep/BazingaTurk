package com.bazingaturk.business;

import com.bazingaturk.business.service.AssignmentSvc;
import com.bazingaturk.business.service.WorkerSvc;
import com.bazingaturk.business.vo.taskVO.AssignmentVO;
import com.bazingaturk.data.entity.tasks.Assignment;
import com.bazingaturk.data.entity.tasks.Partition;
import com.bazingaturk.data.entity.tasks.Task;
import com.bazingaturk.data.entity.user.UserInfo;
import com.bazingaturk.data.entity.user.Worker;
import com.bazingaturk.data.repository.*;
import com.bazingaturk.util.enums.ResultMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.bazingaturk.util.enums.ResultMessageEnum.FAIL;
import static com.bazingaturk.util.enums.ResultMessageEnum.SUCCESS;

@Service
public class AssignmentSvcImpl implements AssignmentSvc {

    private TaskRepository taskRepository;
    private PartitionRepository partitionRepository;
    private AssignmentRepository assignmentRepository;
    private UserInfoRepository infoRepository;
    private WorkerRepository workerRepository;


    @Autowired
    AssignmentSvcImpl(TaskRepository taskRepository, PartitionRepository partitionRepository, AssignmentRepository assignmentRepository,UserInfoRepository userInfoRepository,WorkerRepository workerRepository) {
        this.taskRepository = taskRepository;
        this.partitionRepository = partitionRepository;
        this.assignmentRepository = assignmentRepository;
        this.infoRepository=userInfoRepository;
        this.workerRepository=workerRepository;
    }


    @Override
    public synchronized ResultMessageEnum receiveAssignment(String workerId, String taskId) {

        //首先获得空闲的Partition
        Task task = taskRepository.getOne(taskId);
        List<Partition> list = task.getPartitions();
        Partition partition = null;

        //可能多个请求请求到同一个Partition,引入简单的随机机制
        Random random = new Random();
        int ptr = random.nextInt(list.size());

        for (int i = 0; i < list.size(); i++) {
            Partition p = list.get(ptr);
            if (p.isReceivable(workerId)) {
                partition = p;
                break;
            }
            ptr += 1;
            ptr %= list.size();
        }

        if(partition == null)
            return FAIL;

        //在partition中注册
        String createTime = task.getCreateTime();
        partition.receive(taskId, createTime, workerId);

        task.setReceiveTimes(task.getReceiveTimes()+1);

        Task saved_task = taskRepository.save(task);
        return ResultMessageEnum.SUCCESS;
    }

    @Override
    public AssignmentVO getAssignment(String assignmentId) {
        Assignment a = assignmentRepository.getOne(assignmentId);
        Task t = taskRepository.getOne(a.getTaskId());
        AssignmentVO vo = new AssignmentVO(t, a);
        return vo;
    }

    @Override
    public ResultMessageEnum progressAssignment(String assignmentId) {
        Assignment assignment = assignmentRepository.getOne(assignmentId);
        int progress = assignment.getProgress();
        int size = assignment.getSize();
        if (progress < size) {
            assignment.setProgress(progress + 1);
            assignmentRepository.save(assignment);
            return SUCCESS;
        } else {
            return FAIL;
        }

    }

    @Override
    public ResultMessageEnum submitAssignment(String assignmentId) {

        Assignment assignment = assignmentRepository.getOne(assignmentId);
        Partition partition = partitionRepository.getOne(assignment.getPartitionId());
        partition.submit(assignment.getWorkerId());
        DateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Date today = new Date();
        String date = format.format(today);
        assignment.setSubmitTime(date);
        assignmentRepository.save(assignment);
        return SUCCESS;
    }

    @Override
    public ResultMessageEnum approveAssignment(String assignmentId) {

        Assignment assignment = assignmentRepository.getOne(assignmentId);
        Partition partition = partitionRepository.getOne(assignment.getPartitionId());
        partition.approve(assignment.getWorkerId());
        assignmentRepository.save(assignment);
        try {
            Worker worker = workerRepository.findByUserId(assignment.getWorkerId());
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date();
            String date = format.format(today);
            worker.rewardWorker(date,assignment.getAssignmentReward());
            workerRepository.save(worker);
        } catch (Exception e) {
            e.printStackTrace();
            return FAIL;
        }
        return ResultMessageEnum.SUCCESS;
    }

    @Override
    public ResultMessageEnum rejectAssignment(String assignmentId) {

        Assignment assignment = assignmentRepository.getOne(assignmentId);
        Partition partition = partitionRepository.getOne(assignment.getPartitionId());
        partition.reject(assignment.getWorkerId());
        assignmentRepository.save(assignment);

        return SUCCESS;

    }

    @Override
    public ResultMessageEnum beginAssignment(String assignmentId) {
        Assignment assignment=assignmentRepository.getOne(assignmentId);
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.CHINA);
        String date = format.format(new Date());
        assignment.setStartTime(date);
        assignmentRepository.save(assignment);
        return ResultMessageEnum.SUCCESS;
    }

}
