package com.bazingaturk.data.entity.anno;

import com.bazingaturk.business.vo.annoVO.PointerVO;
import com.bazingaturk.business.vo.annoVO.SegmentationVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Segmentation {
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    @Column(length = 32)
    private String id;
    private String tagName;
    private String color;
    private String description;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pointer> pointerList;

    public Segmentation(SegmentationVO vo){
        tagName=vo.getTagName();
        color=vo.getColor();
        description=vo.getDescription();
        pointerList=new ArrayList<Pointer>();
        for(PointerVO pointer:vo.getPointerList()){
            pointerList.add(new Pointer(pointer));
        }
    }
}

