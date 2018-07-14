package com.bazingaturk.business.vo.annoVO;

import com.bazingaturk.data.entity.anno.Pointer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointerVO {
    private int x, y;

    public PointerVO(Pointer p) {
        this.x = p.getX();
        this.y = p.getY();
    }
}
