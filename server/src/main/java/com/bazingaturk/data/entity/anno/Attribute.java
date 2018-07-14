package com.bazingaturk.data.entity.anno;

import com.bazingaturk.business.vo.annoVO.AttributeVO;
import com.bazingaturk.business.vo.annoVO.TagVO;
import com.bazingaturk.data.entity.tasks.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Attribute {

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    @Column(length = 32)
    private String id;

    //这个属性的名称，如年龄
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //这个属性下所有的标签，如{儿童，少年，青年，壮年，老年}
    private List<Tag> tags;

    public Attribute(AttributeVO v) {
        name = v.getName();

        tags = new ArrayList<>();
        for (TagVO t : v.getTags()) {
            tags.add(new Tag(t));
        }
    }

    public List<TagVO> getTagVOs() {
        List<TagVO> tagVOs = new ArrayList<>();
        for (Tag t : tags) {
            tagVOs.add(new TagVO(t));
        }
        return tagVOs;
    }

}
