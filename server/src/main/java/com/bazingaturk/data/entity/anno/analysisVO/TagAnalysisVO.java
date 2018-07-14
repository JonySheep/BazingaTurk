package com.bazingaturk.data.entity.anno.analysisVO;

import com.bazingaturk.data.entity.anno.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagAnalysisVO {

    //框的id
    private String boxId;

    //该标签的名称
    private String name;

    //该标签的颜色
    private String colorHex;

    //该标签是谁打的
    private String workerId;

    //该标签的簇号
    private int clusterId = -1;

    public TagAnalysisVO(Tag tag) {
        this.name = tag.getName();
        this.colorHex = tag.getColorHex();
    }
}
