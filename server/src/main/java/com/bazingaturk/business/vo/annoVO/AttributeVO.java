package com.bazingaturk.business.vo.annoVO;

import com.bazingaturk.data.entity.anno.Attribute;
import com.bazingaturk.data.entity.anno.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeVO {

    //这个属性的名称，如年龄
    private String name;
    //则个属性下所有的标签，如{儿童，少年，青年，壮年，老年}
    private List<TagVO> tags;

    public AttributeVO(Attribute a) {
        name = a.getName();
        tags = a.getTagVOs();
    }
}
