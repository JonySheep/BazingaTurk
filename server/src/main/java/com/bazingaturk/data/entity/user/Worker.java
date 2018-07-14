package com.bazingaturk.data.entity.user;

import com.bazingaturk.data.entity.tasks.Assignment;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Worker {
    @Id
    @GenericGenerator(name="system-uuid",strategy="uuid")
    @GeneratedValue(generator = "system-uuid")
    @Column(length = 32)
    private String id;

    private String userId;

    private double reward=0.0;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RewardLog> rewardLogs;

    public Worker(String userId) {
        this.userId = userId;
        this.rewardLogs=null;
        this.id=null;
        this.reward=0.0;
    }

    public void rewardWorker(String date,double money){
        reward=reward+money;
        if(rewardLogs==null || rewardLogs.size()<1)
            rewardLogs=new ArrayList<RewardLog>();
        for(RewardLog log:rewardLogs){
            if(log.getDate().equals(date)){
                money=money+log.getMoney();
                rewardLogs.remove(log);
                log.setMoney(money);
                rewardLogs.add(log);
                return;
            }
        }
        RewardLog log=new RewardLog();
        log.setMoney(money);
        log.setDate(date);
        rewardLogs.add(log);

    }

}