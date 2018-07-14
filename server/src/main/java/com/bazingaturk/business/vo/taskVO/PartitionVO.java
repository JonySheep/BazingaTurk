package com.bazingaturk.business.vo.taskVO;

import com.bazingaturk.business.vo.taskVO.AssignmentVO;
import com.bazingaturk.data.entity.tasks.Assignment;
import com.bazingaturk.data.entity.tasks.Partition;
import com.bazingaturk.data.entity.tasks.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartitionVO {

    private String partitionId;
    //任务内编号
    private int no;

    //起始图片编号
    private int low = 0;

    //终止图片编号
    private int high = 0;

    //作业列表
    private List<AssignmentVO> assignmentVOs = new ArrayList<>();

    public PartitionVO(Task t, Partition p) {
        this.partitionId = p.getPartitionId();
        this.no = p.getNo();
        this.low = p.getLow();
        this.high = p.getHigh();
        this.assignmentVOs = new ArrayList<>();

        for(Assignment a: p.getAssignments()){
            assignmentVOs.add(new AssignmentVO(t, a));
        }
    }
}
