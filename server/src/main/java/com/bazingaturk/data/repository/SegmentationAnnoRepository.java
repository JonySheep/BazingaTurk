package com.bazingaturk.data.repository;

import com.bazingaturk.data.entity.anno.Segmentation;
import com.bazingaturk.data.entity.anno.SegmentationAnno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SegmentationAnnoRepository extends JpaRepository<SegmentationAnno, String> {
    SegmentationAnno findByAssignmentIdAndImageId(String assignmentId,int imageId);
}
