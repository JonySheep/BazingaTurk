package com.bazingaturk.business.vo.annoVO;

import com.bazingaturk.data.entity.anno.Box;
import com.bazingaturk.data.entity.anno.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoxVO {
    private int xpos = 0;
    private int ypos = 0;
    private int width = 0;
    private int height = 0;
    private List<TagVO> tags = new ArrayList<>();
    private String colorHex = "";

    public BoxVO(Box box){
        xpos=box.getXpos();
        ypos=box.getYpos();
        width=box.getWidth();
        height=box.getHeight();
        for(Tag tag:box.getTags()){
            tags.add(new TagVO(tag));
        }
        colorHex=box.getColorHex();

    }
}
