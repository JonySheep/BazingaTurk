package com.bazingaturk.data.entity.tasks;

import com.bazingaturk.util.enums.AssignmentStatusEnum;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.bazingaturk.util.enums.AssignmentStatusEnum.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity(name = "Partitions")
public class Partition {

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    @Column(length = 32)
    //分组id
    private String partitionId;

    //任务内编号
    private Integer no;

    //分组起始点
    private Integer low;

    //分组终止点
    private Integer high;

    @ElementCollection
    @MapKeyColumn(name = "workerId")
    @Column(name = "status")
    //工作者历史
    private Map<String, String> workerHistory;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Assignment> assignments;

    //一个分组需要的作业次数
    private Integer assignTimes;

    //一份作业的奖励
    private Double assignmentReward;

    //最小单元平均限制时间
    private Double unitLimitMinutes;

    public boolean isReceivable(String workerId) {
        //判断该任务是否有空位,以及
        int counter = 0;
        for (Map.Entry<String, String> entry : workerHistory.entrySet()) {
            if (!entry.getValue().equals(AssignmentStatusEnum.REJECTED.toString())) {
                counter++;
            }
            if (entry.getKey().equals(workerId)) {
                return false;
            }
        }
        if (counter >= assignTimes) {
            return false;
        } else {
            return true;
        }
    }

    public boolean receive(String taskId, String createTime, String workerId) {
        if (isReceivable(workerId)) {
            //创建assignment
            Assignment a = new Assignment();
            a.setTaskId(taskId);
            a.setPartitionId(partitionId);
            a.setWorkerId(workerId);
            a.setSize(high - low + 1);
            a.setProgress(0);
            a.setStatus(AssignmentStatusEnum.ONGOING);
            //TODO 更新ddl计算方式
            a.setDeadline(getDeadline(createTime, unitLimitMinutes, a.getSize()));

            a.setAssignmentReward(assignmentReward);
            assignments.add(a);

            workerHistory.put(workerId, ONGOING.toString());
            return true;
        } else {
            return false;
        }
    }

    public boolean submit(String workerId) {
        if (workerHistory.containsKey(workerId)) {
            findAssignmentByWorkerId(workerId).setStatus(CHECKING);
            workerHistory.put(workerId, CHECKING.toString());
            return true;
        } else {
            return false;
        }
    }

    public boolean approve(String workerId) {
        if (workerHistory.containsKey(workerId)) {
            findAssignmentByWorkerId(workerId).setStatus(APPROVED);
            workerHistory.put(workerId, APPROVED.toString());
            return true;
        } else {
            return false;
        }
    }

    public boolean reject(String workerId) {
        if (workerHistory.containsKey(workerId)) {
            findAssignmentByWorkerId(workerId).setStatus(REJECTED);
            workerHistory.put(workerId, REJECTED.toString());
            return true;
        } else {
            return false;
        }
    }

    private Assignment findAssignmentByWorkerId(String workerId) {
        for (Assignment a : assignments) {
            if (a.getWorkerId().equals(workerId)) {
                return a;
            }
        }
        return null;
    }

    private String getDeadline(String str, double unitLimitMinutes, int size){

        SimpleDateFormat f = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.CHINA);
        Date date;
        try {
            date = f.parse(str);
            System.out.println(date);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }

        int milliseconds = (int) Math.ceil(unitLimitMinutes*60*1000*size);
        Date now = new Date();
        Date deadline = new Date(now.getTime() + milliseconds);

        String ret = f.format(deadline.getTime());
        return ret;
    }

}
