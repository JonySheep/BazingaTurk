package com.bazingaturk.business.vo.annoVO;

import com.bazingaturk.data.entity.anno.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagVO{

    //该标签的名称
    private String name = null;
    //该标签的颜色
    private String colorHex = null;

    public TagVO(Tag t) {
        name = t.getName();
        colorHex = t.getColorHex();
    }
}
