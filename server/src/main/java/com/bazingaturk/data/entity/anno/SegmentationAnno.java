package com.bazingaturk.data.entity.anno;

import com.bazingaturk.business.vo.annoVO.SegmentationAnnoVO;
import com.bazingaturk.business.vo.annoVO.SegmentationVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Data
@Table(name="SegmentationAnnos")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SegmentationAnno {

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    @Column(length = 32)
    private String id;
    private String imageUrl;
    private String assignmentId;
    private String workerId;
    private String layerUrl;
    private int imageId;
    private int colorSize;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Segmentation> segments;

    public SegmentationAnno(SegmentationAnnoVO vo){
        imageUrl=vo.getImageUrl();
        colorSize = vo.getColorSize();
        segments= new ArrayList<>();
        for(SegmentationVO seg:vo.getSegList()){
            segments.add(new Segmentation(seg));
        }
    }
}
