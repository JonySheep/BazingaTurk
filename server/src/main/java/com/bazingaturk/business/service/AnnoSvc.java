package com.bazingaturk.business.service;

import com.bazingaturk.business.vo.annoVO.AnnoVO;
import com.bazingaturk.business.vo.annoVO.SegmentationAnnoVO;
import com.bazingaturk.data.entity.anno.Anno;
import com.bazingaturk.data.entity.anno.SegmentationAnno;
import com.bazingaturk.util.enums.ResultMessageEnum;
import com.bazingaturk.util.enums.TaskTypeEnum;
import org.springframework.stereotype.Service;

@Service
public interface AnnoSvc {
    /**
     * 获得一个标注
     *
     * @param taskType
     * @param taskId
     * @param assignmentId
     * @param imageId
     * @return
     */
    Object getAnno(TaskTypeEnum taskType, String taskId, String partitionId, String assignmentId, int imageId);

    /**
     * 保存一个标注
     *
     * @param taskType
     * @param taskId
     * @param assignmentId
     * @param imageId
     * @param anno
     * @return
     */
    ResultMessageEnum putAnno(TaskTypeEnum taskType, String taskId, String assignmentId, int imageId, String workerId, String anno);

}
