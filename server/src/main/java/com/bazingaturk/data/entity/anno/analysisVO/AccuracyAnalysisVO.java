package com.bazingaturk.data.entity.anno.analysisVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccuracyAnalysisVO {

    //准确率
    double accuracy  = 0.0;

    //工人id
    String workerId;
}
