package com.bazingaturk.data.entity.anno;

import com.bazingaturk.business.vo.annoVO.BoxVO;
import com.bazingaturk.business.vo.annoVO.TagVO;
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
public class Box {

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    @Column(length = 32)
    private String id;

    //参考点的坐标
    private Integer xpos , ypos;
    //相对参考点的宽和高
    private Integer width, height;
    //该方框的标签列表
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tag> tags ;
    //该方框的颜色
    private String colorHex ;

    public Box(BoxVO vo){
        xpos=vo.getXpos();
        ypos=vo.getYpos();
        width=vo.getWidth();
        height=vo.getHeight();
        tags= new ArrayList<>();
        for(TagVO tag:vo.getTags()){
            tags.add(new Tag(tag));
        }
        colorHex=vo.getColorHex();
    }
}
