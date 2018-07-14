package com.bazingaturk.business;

import com.bazingaturk.business.service.WorkerSvc;
import com.bazingaturk.business.vo.taskVO.AssignmentVO;
import com.bazingaturk.business.vo.userVO.UserInfoVO;
import com.bazingaturk.business.vo.userVO.WorkerVO;
import com.bazingaturk.data.entity.tasks.Task;
import com.bazingaturk.data.entity.user.*;
import com.bazingaturk.data.repository.AssignmentRepository;
import com.bazingaturk.data.repository.TaskRepository;
import com.bazingaturk.data.repository.UserInfoRepository;
import com.bazingaturk.data.repository.WorkerRepository;
import com.bazingaturk.data.entity.tasks.Assignment;
import com.bazingaturk.util.enums.AssignmentStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WorkerSvcImpl implements WorkerSvc {

    private final WorkerRepository workerRepository;
    private final UserInfoRepository infoRepository;
    private final AssignmentRepository assignmentRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public WorkerSvcImpl(WorkerRepository workerRepository, UserInfoRepository infoRepository, AssignmentRepository assignmentRepository, TaskRepository taskRepository) {
        this.workerRepository = workerRepository;
        this.infoRepository = infoRepository;
        this.assignmentRepository = assignmentRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public List<AssignmentVO> getOngoingAssignments(String userId) {
        List<Assignment> assignments = assignmentRepository.findByWorkerId(userId);
        if (assignments == null || assignments.size() < 1)
            return null;
        List<AssignmentVO> ongoing = new ArrayList<>();
        for (Assignment assignment : assignments) {
            if (assignment.getStatus().equals(AssignmentStatusEnum.ONGOING)) {
                Task task = taskRepository.getOne(assignment.getTaskId());
                ongoing.add(new AssignmentVO(task, assignment));
            }
        }
        return ongoing;
    }

    @Override
    public List<AssignmentVO> getFinishedAssignments(String userId) {
        List<Assignment> assignments = assignmentRepository.findByWorkerId(userId);
        if (assignments == null || assignments.size() < 1)
            return null;
        List<AssignmentVO> finished = new ArrayList<>();
        for (Assignment assignment : assignments) {
            if (!assignment.getStatus().equals(AssignmentStatusEnum.ONGOING)) {
                Task task = taskRepository.getOne(assignment.getTaskId());
                finished.add(new AssignmentVO(task, assignment));
            }
        }
        return finished;
    }


    @Override
    public int getWorkerRank(String userId) {
        List<Worker> list = workerRepository.findAll();
        Worker worker = workerRepository.findByUserId(userId);
        double reward = worker.getReward();
        int count = 0;
        for (Worker temp : list) {
            if (temp.getReward() > reward) {
                count++;
            }
        }
        return count + 1;
    }

    @Override
    public List<String> getWorkerRewardLog(String userId) {
        List<RewardLog> logs = workerRepository.findByUserId(userId).getRewardLogs();
        if (logs == null || logs.size() < 1)
            return null;
        List<String> result = new ArrayList<>();
        for (RewardLog temp : logs) {
            result.add(temp.getDate() + ":" + temp.getMoney());
        }
        return result;
    }

    @Override
    public List<UserInfoVO> getWorkerInfoList() {
        List<UserInfo> userInfos = infoRepository.findByRole("Worker");
        if (userInfos == null || userInfos.size() < 1)
            return null;
        List<UserInfoVO> vos = new ArrayList<>();
        for (UserInfo i : userInfos) {
            vos.add(new UserInfoVO(i));
        }
        return vos;
    }

    @Override
    public WorkerVO getWorker(String userId) throws ParseException {
        WorkerVO vo = new WorkerVO(workerRepository.findByUserId(userId));

        List<Assignment> assignments = assignmentRepository.findByWorkerId(userId);
        if (assignments == null || assignments.size() < 1)
            return vo;
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.CHINA);
        int nh = 1000 * 60 * 60;
        double time = 0;
        double accuracy=0.0;
        int size=0;
        for (Assignment assignment : assignments) {
            if(assignment.getAccuracy()!=0.0){
                size++;
                accuracy=accuracy+assignment.getAccuracy();
            }
            if (assignment.getStatus().equals(AssignmentStatusEnum.REJECTED) || assignment.getStatus().equals(AssignmentStatusEnum.APPROVED)) {
                time = time + (format.parse(assignment.getSubmitTime()).getTime() - format.parse(assignment.getStartTime()).getTime());
            }
        }
        if(time==0)
            return vo;
        time=time/nh;
        vo.setEfficiency(vo.getReward() / time);
        vo.setAccuracy(accuracy/size);
        return vo;
    }

    @Override
    public double getTodayReward(String workerId) {
        List<String> logs=getWorkerRewardLog(workerId);
        if(logs==null || logs.size()<1)
            return 0.0;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        String date = format.format(today);
        double result=0.0;
        for(String s:logs){
            if(s.contains(date)){
                result=Double.parseDouble(s.split(":")[1]);
            break;
            }
        }
        return result;
    }


}
