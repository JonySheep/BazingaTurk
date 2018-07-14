package com.bazingaturk.business.vo.userVO;

import com.bazingaturk.data.entity.user.RewardLog;
import com.bazingaturk.data.entity.user.Worker;
import lombok.Data;

import java.util.List;

@Data
public class WorkerVO {
    private String userId;
    private double reward;
    private double accuracy;
    private double efficiency=0.0;
    private List<RewardLog> rewardLogs;

    public WorkerVO(Worker worker){
        this.userId=worker.getUserId();
        this.accuracy=0.0;
        this.reward=worker.getReward();
        this.efficiency=0.0;
        this.rewardLogs=worker.getRewardLogs();
    }
}
