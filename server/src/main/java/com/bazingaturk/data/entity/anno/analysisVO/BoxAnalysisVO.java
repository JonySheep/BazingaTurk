package com.bazingaturk.data.entity.anno.analysisVO;


import com.bazingaturk.data.entity.anno.Box;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoxAnalysisVO {

    //框的Id
    private String boxId;

    //参考点的坐标
    private Integer xpos , ypos;

    //相对参考点的宽和高
    private Integer width, height;

    //工人的id
    private String workerId;

    public BoxAnalysisVO(Box b) {
        this.boxId = b.getId();
        this.xpos = b.getXpos();
        this.ypos = b.getYpos();
        this.width = b.getWidth();
        this.height = b.getHeight();
    }
}
