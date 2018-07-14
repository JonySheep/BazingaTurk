package com.bazingaturk.data.entity.anno;

import com.bazingaturk.business.vo.annoVO.AnnoVO;
import com.bazingaturk.business.vo.annoVO.BoxVO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@Table(name="Annos")
@Entity
public class Anno {
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    @Column(length = 32)
    private String id;
    private String imageUrl;
    private String assignmentId;
    private String workerId;
    private int imageId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Box> boxes;

    public Anno(AnnoVO vo){
        imageUrl=vo.getImageUrl();
        boxes=new ArrayList<>();
        for(BoxVO box:vo.getBoxes()){
            boxes.add(new Box(box));
        }
    }
}
