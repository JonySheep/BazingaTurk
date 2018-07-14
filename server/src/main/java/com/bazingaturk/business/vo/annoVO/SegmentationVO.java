package com.bazingaturk.business.vo.annoVO;

import com.bazingaturk.data.entity.anno.Pointer;
import com.bazingaturk.data.entity.anno.Segmentation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SegmentationVO {
    private String tagName;
    private String color;
    private String description;
    private int isShow;
    private List<PointerVO> pointerList;

    public SegmentationVO(Segmentation seg) {
        this.tagName = seg.getTagName();
        this.color = seg.getColor();
        this.description = seg.getDescription();
        this.pointerList = new ArrayList<>();
        this.isShow=1;
        for(Pointer p: seg.getPointerList()){
            pointerList.add(new PointerVO(p));
        }
    }
}
