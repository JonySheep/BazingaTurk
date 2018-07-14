package com.bazingaturk.business.vo.annoVO;

import com.bazingaturk.data.entity.anno.Anno;
import com.bazingaturk.data.entity.anno.Box;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnoVO {
    private String imageUrl = null;
    private List<BoxVO> boxes = null;

    public AnnoVO(Anno anno) {
        this.imageUrl = anno.getImageUrl();
        List<BoxVO> boxes = new ArrayList<>();
        for(Box b: anno.getBoxes()){
            boxes.add(new BoxVO(b));
        }
        this.boxes = boxes;
    }
}
