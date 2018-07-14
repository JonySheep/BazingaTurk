package com.bazingaturk.data.entity.anno;

import com.bazingaturk.business.vo.annoVO.TagVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Tag{

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    @Column(length = 32)
    private String id;

    //该标签的名称
    private String name = null;

    //该标签的颜色
    private String colorHex = null;

    public Tag(TagVO v) {
        name = v.getName();
        colorHex = v.getColorHex();
    }
}
