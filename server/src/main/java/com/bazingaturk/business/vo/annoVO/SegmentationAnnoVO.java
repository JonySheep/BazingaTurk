package com.bazingaturk.business.vo.annoVO;

import com.bazingaturk.data.entity.anno.Segmentation;
import com.bazingaturk.data.entity.anno.SegmentationAnno;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SegmentationAnnoVO {

    private String imageUrl = null;
    private List<SegmentationVO> segList;
    private String layerUrl;
    private int colorSize;

    public SegmentationAnnoVO(SegmentationAnno anno){
        imageUrl=anno.getImageUrl();
        segList= new ArrayList<>();
        colorSize = anno.getColorSize();
        for(Segmentation seg:anno.getSegments()){
            segList.add(new SegmentationVO(seg));
        }
    }
}
