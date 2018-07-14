package com.bazingaturk.business;

import com.bazingaturk.WebAppConfig;
import com.bazingaturk.business.service.AnnoSvc;
import com.bazingaturk.business.service.AssignmentSvc;
import com.bazingaturk.business.service.TaskSvc;
import com.bazingaturk.business.vo.annoVO.*;
import com.bazingaturk.business.vo.taskVO.AssignmentVO;
import com.bazingaturk.data.entity.anno.Anno;
import com.bazingaturk.data.entity.anno.SegmentationAnno;
import com.bazingaturk.data.entity.tasks.Assignment;
import com.bazingaturk.data.entity.tasks.Partition;
import com.bazingaturk.data.repository.AnnoRepository;
import com.bazingaturk.data.repository.PartitionRepository;
import com.bazingaturk.data.repository.SegmentationAnnoRepository;
import com.bazingaturk.util.enums.ResultMessageEnum;
import com.bazingaturk.util.enums.TaskTypeEnum;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.bazingaturk.util.enums.ResultMessageEnum.FAIL;
import static com.bazingaturk.util.enums.TaskTypeEnum.CLASSIFICATION;
import static com.bazingaturk.util.enums.TaskTypeEnum.DETECTION;
import static com.bazingaturk.util.enums.TaskTypeEnum.SEGMENTATION;

@Service
public class AnnoSvcImpl implements AnnoSvc {
    private final AnnoRepository annoRepository;
    private final SegmentationAnnoRepository segmentationAnnoRepository;
    private final PartitionRepository partitionRepository;
    private final AssignmentSvc assignmentSvc;

    @Autowired
    public AnnoSvcImpl(AnnoRepository annoRepository, SegmentationAnnoRepository segmentationAnnoRepository, PartitionRepository partitionRepository,AssignmentSvc assignmentSvc) {
        this.annoRepository = annoRepository;
        this.segmentationAnnoRepository = segmentationAnnoRepository;
        this.partitionRepository = partitionRepository;
        this.assignmentSvc=assignmentSvc;
    }

    /*
     * 接口方法
     */
    @Override
    public Object getAnno(TaskTypeEnum taskType, String taskId, String partitionId, String assignmentId, int imageId) {
        switch (taskType){
            case CLASSIFICATION:
            case DETECTION:
                return getAnnoHelper(taskId, partitionId, assignmentId,imageId);
            case SEGMENTATION:
                return getSegmentationHelper(taskId, partitionId, assignmentId,imageId);
            default:
                return null;
        }
    }

    @Override
    public ResultMessageEnum putAnno(TaskTypeEnum taskType, String taskId, String assignmentId, int imageId, String workerId, String anno) {
        switch (taskType){
            case CLASSIFICATION:
            case DETECTION:
                return putAnnoHelper(assignmentId,imageId, workerId, anno);
            case SEGMENTATION:
                return putSegmentationHelper(assignmentId,imageId, workerId, anno);
            default:
                return FAIL;
        }
    }

    /*
     * 具体实现方法
     */

    private ResultMessageEnum putAnnoHelper(String assignmentId, int imageId, String workerId, String anno) {
        Anno old = annoRepository.findByAssignmentIdAndImageId(assignmentId, imageId);
        Gson gson = new Gson();
        AnnoVO vo = gson.fromJson(anno, AnnoVO.class);
        Anno newAnno = new Anno(vo);
        newAnno.setImageId(imageId);
        newAnno.setAssignmentId(assignmentId);
        newAnno.setWorkerId(workerId);
        if(old != null){
            old.setBoxes(newAnno.getBoxes());
            annoRepository.save(old);
        }else{
            annoRepository.save(newAnno);
        }

        return ResultMessageEnum.SUCCESS;
    }

    private ResultMessageEnum putSegmentationHelper(String assignmentId, int imageId, String workerId, String anno) {
        SegmentationAnno old = segmentationAnnoRepository.findByAssignmentIdAndImageId(assignmentId, imageId);
        Gson gson = new Gson();
        SegmentationAnnoVO vo = gson.fromJson(anno,SegmentationAnnoVO.class);
        SegmentationAnno newAnno = new SegmentationAnno(vo);
        newAnno.setImageId(imageId);
        newAnno.setAssignmentId(assignmentId);
        newAnno.setWorkerId(workerId);
        if (old != null) {
            old.setSegments(newAnno.getSegments());
            old.setColorSize(newAnno.getColorSize());
            segmentationAnnoRepository.save(old);
        }else{
            AssignmentVO a=assignmentSvc.getAssignment(assignmentId);
            newAnno.setLayerUrl("/server/tasks/t" +a.getTaskId() + "/p" + a.getPartitionId()+ "/a" + a.getAssignmentId() +"/" + imageId + ".png");
            segmentationAnnoRepository.save(newAnno);
        }

        return ResultMessageEnum.SUCCESS;
    }

    private AnnoVO getAnnoHelper(String taskId, String partitionId,  String assignmentId, int imageId){
        Anno anno = annoRepository.findByAssignmentIdAndImageId(assignmentId, imageId);
        Partition partition = partitionRepository.getOne(partitionId);
        int pos = partition.getLow() - 2 + imageId;
        if (anno != null) {
            return new AnnoVO(anno);
        }else{
            AnnoVO annoVO = new AnnoVO();
            List<BoxVO> boxes = new ArrayList<>();
            annoVO.setBoxes(boxes);
            File file = new File(WebAppConfig.BASE + "/assets/tasks/t" + taskId + "/Images");
            String[] fileList = file.list();
            annoVO.setImageUrl("/server/tasks/t" + taskId + "/Images/" + fileList[pos]);
            return annoVO;
        }
    }

    private SegmentationAnnoVO getSegmentationHelper(String taskId, String partitionId, String assignmentId, int imageId){
        SegmentationAnno anno = segmentationAnnoRepository.findByAssignmentIdAndImageId(assignmentId, imageId);
        Partition partition = partitionRepository.getOne(partitionId);
        int pos = partition.getLow() - 2 + imageId;
        if (anno != null) {
            return new SegmentationAnnoVO(anno);
        }else{
            SegmentationAnnoVO annoVO = new SegmentationAnnoVO();
            List<SegmentationVO> segmentationVOS = new ArrayList<>();
            annoVO.setSegList(segmentationVOS);
            File file = new File(WebAppConfig.BASE + "/assets/tasks/t" + taskId + "/Images");
            String[] fileList = file.list();
            annoVO.setImageUrl("/server/tasks/t" + taskId + "/Images/" + fileList[pos]);
            return annoVO;
        }
    }
}
