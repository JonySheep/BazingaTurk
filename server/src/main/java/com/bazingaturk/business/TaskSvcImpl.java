package com.bazingaturk.business;

import com.bazingaturk.WebAppConfig;
import com.bazingaturk.business.service.TaskSvc;
import com.bazingaturk.business.vo.annoVO.AttributeVO;
import com.bazingaturk.business.vo.taskVO.PartitionVO;
import com.bazingaturk.business.vo.taskVO.TaskCreateVO;
import com.bazingaturk.business.vo.taskVO.TaskSimpleVO;
import com.bazingaturk.data.entity.anno.Anno;
import com.bazingaturk.data.entity.anno.Box;
import com.bazingaturk.data.entity.anno.SegmentationAnno;
import com.bazingaturk.data.entity.anno.Tag;
import com.bazingaturk.data.entity.anno.analysisVO.AccuracyAnalysisVO;
import com.bazingaturk.data.entity.anno.analysisVO.BoxAnalysisVO;
import com.bazingaturk.data.entity.anno.analysisVO.TagAnalysisVO;
import com.bazingaturk.data.entity.tasks.Assignment;
import com.bazingaturk.data.entity.tasks.Partition;
import com.bazingaturk.data.entity.tasks.Task;
import com.bazingaturk.data.repository.*;
import com.bazingaturk.util.AnalysisPostHelper;
import com.bazingaturk.util.HibernateProxyTypeAdapter;
import com.bazingaturk.util.enums.AssignmentStatusEnum;
import com.bazingaturk.util.enums.ResultMessageEnum;
import com.bazingaturk.util.enums.TaskStatusEnum;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static com.bazingaturk.util.enums.ResultMessageEnum.FAIL;
import static com.bazingaturk.util.enums.ResultMessageEnum.SUCCESS;

@Service
public class TaskSvcImpl implements TaskSvc {

    private final TaskRepository taskRepository;
    private final PartitionRepository partitionRepository;
    private final AnnoRepository annoRepository;
    private final SegmentationAnnoRepository segmentationAnnoRepository;
    private final AssignmentRepository assignmentRepository;

    @Autowired
    TaskSvcImpl(TaskRepository taskRepository, PartitionRepository partitionRepository, AnnoRepository annoRepository, SegmentationAnnoRepository segmentationAnnoRepository, AssignmentRepository assignmentRepository) {
        this.taskRepository = taskRepository;
        this.partitionRepository = partitionRepository;
        this.annoRepository = annoRepository;
        this.segmentationAnnoRepository = segmentationAnnoRepository;
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    public ResultMessageEnum createTask(File tempFile, String parentName, TaskCreateVO taskCreateVO) {

        /*
         * 初始化：
         * 任务名称
         * 任务类型
         * 发起者Id
         * 任务描述
         * 任务状态
         * 任务需分类的属性列表
         * 分组数量
         * 任务奖励
         * 接受这个任务的人数
         * 收藏这个任务的人数
         * 一份作业的奖励
         * 单元限制时间
         *
         * 未初始化：
         * 任务Id
         * 任务创建时间
         * 任务大小
         * 任务分组列表
         * 任务静态资源目录
         */
        Task task = new Task(taskCreateVO);

        //初始化taskID
        task = taskRepository.save(task);

        //初始化task时间
        SimpleDateFormat f = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.CHINA);
        task.setCreateTime(f.format(Calendar.getInstance().getTime()));

        //初始化任务静态资源目录
        String path = WebAppConfig.BASE + "/assets/tasks/t" + task.getTaskId() + "/" + parentName + "/";
        task.setPath(path);

        //解压文件
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        unzip(tempFile.getAbsolutePath(), path);
        File parent = tempFile.getParentFile();
        tempFile.delete();
        parent.delete();
        parent = new File(path);
        for (File child : parent.listFiles()) {
            if (child.isDirectory()) {
                for (File cc : child.listFiles()) {
                    cc.delete();
                }
                child.delete();
            }
        }

        //初始化task任务大小
        File imageFile = new File(path);
        task.setSize(imageFile.listFiles().length);

        //初始化任务分组
        int partTimes = task.getPartTimes();
        int quotient = task.getSize() / partTimes;
        int remainder = task.getSize() % partTimes;
        List<Integer> numList = new ArrayList<>();
        for (int i = 0; i < partTimes; i++) {
            numList.add(quotient);
        }
        for (int i = 0; i < remainder; i++) {
            Integer e = numList.get(i);
            numList.remove(i);
            numList.add(i, e + 1);
        }

        List<Partition> partitions = new ArrayList<>();
        Integer assignTimes = taskCreateVO.getAssignTimes();
        Double assignmentReward = taskCreateVO.getAssignmentReward();
        Double unitLimitedTimes = taskCreateVO.getUnitLimitMinutes();

        int low = 1;
        for (int i = 1; i <= partTimes; i++) {
            int high = low + numList.get(i - 1) - 1;
            Partition partition = new Partition(
                    "",
                    i,
                    low,
                    low + numList.get(i - 1) - 1,
                    new HashMap<>(),
                    new ArrayList<>(),
                    assignTimes,
                    assignmentReward,
                    unitLimitedTimes
            );
            partitions.add(partition);
            low = ++high;
        }
        task.setPartitions(partitions);

        //初始化完成，存入数据库
        task = taskRepository.save(task);
        return ResultMessageEnum.SUCCESS;
    }

    @Override
    public TaskSimpleVO getTaskBrief(String taskId) {
        Optional<Task> optional = taskRepository.findById(taskId);
        return optional.map(TaskSimpleVO::new).orElse(null);
    }

    @Override
    public List<AttributeVO> getTaskAttributes(String taskId) {

        Optional<Task> optional = taskRepository.findById(taskId);
        return optional.map(Task::getAttributeVOs).orElse(null);
    }

    @Override
    public int getPartitionNo(String partitionId) {

        Optional<Partition> optional = partitionRepository.findById(partitionId);
        if (optional.isPresent()) {
            Partition p = optional.get();
            return p.getNo();
        } else {
            return -1;
        }
    }

    @Override
    public List<PartitionVO> getPartitions(String taskId) {

        Optional<Task> optional = taskRepository.findById(taskId);
        if (optional.isPresent()) {
            Task t = optional.get();
            List<PartitionVO> list = new ArrayList<>();
            for (Partition p : t.getPartitions()) {
                list.add(new PartitionVO(t, p));
            }
            return list;
        } else {
            return null;
        }
    }

    @Override
    public List<TaskSimpleVO> getTasks() {

        List<Task> list = taskRepository.findAll();
        List<TaskSimpleVO> ret = new ArrayList<>();
        for (Task t : list) {
            TaskSimpleVO vo = new TaskSimpleVO(t);
            ret.add(vo);
        }
        return ret;
    }

    @Override
    public ResultMessageEnum progressPageView(String taskId) {

        Optional<Task> optional = taskRepository.findById(taskId);
        if (optional.isPresent()) {
            Task t = optional.get();
            t.setPageViews(t.getPageViews() + 1);
            taskRepository.save(t);
            return ResultMessageEnum.SUCCESS;
        } else {
            return ResultMessageEnum.FAIL;
        }
    }

    @Override
    public List<TaskSimpleVO> getOngoingTasks() {
        List<TaskSimpleVO> tasks = getTasks();
        if (tasks == null || tasks.size() < 1)
            return null;
        List<TaskSimpleVO> result = new ArrayList<>();
        for (TaskSimpleVO vo : tasks) {
            if (vo.getStatus().equals(TaskStatusEnum.ONGOING))
                result.add(vo);
        }
        return result;
    }

    @Override
    public List<TaskSimpleVO> getFinishedTasks() {
        List<TaskSimpleVO> tasks = getTasks();
        if (tasks == null || tasks.size() < 1)
            return null;
        List<TaskSimpleVO> result = new ArrayList<>();
        for (TaskSimpleVO vo : tasks) {
            if (vo.getStatus().equals(TaskStatusEnum.FINISHED))
                result.add(vo);
        }
        return result;
    }

    @Override
    public ResultMessageEnum integratePartition(String taskId, String partitionId, String taskType) {
        switch (taskType) {
            case "CLASSIFICATION":
            case "DETECTION":
                return integratePartitionAnnoHelper(taskId, partitionId);
            case "SEGMENTATION":
                return integratePartitionSegAnnoHelper(taskId, partitionId);
            default:
                return FAIL;
        }
    }

    private ResultMessageEnum integratePartitionAnnoHelper(String taskId, String partitionId) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
                .create();

        //获得一个分组
        Partition partition = partitionRepository.getOne(partitionId);
        List<Assignment> assignments = partition.getAssignments();
        for(int i=0;i<assignments.size();i++){
            if(!assignments.get(i).getStatus().equals(AssignmentStatusEnum.CHECKING)){
                assignments.remove(i);
                i--;
            }
        }

        //按照ImageId依次整合图片的所有标注
        int size = partition.getHigh() - partition.getLow() + 1;
        List<AccuracyAnalysisVO> accuracy=new ArrayList<>();
        for (int imageId = 1; imageId <= size; imageId++) {
            List<Anno> annos_for_this_image = new ArrayList<>();
            List<TagAnalysisVO> tagAnalysisVOs = new ArrayList<>();
            List<BoxAnalysisVO> boxAnalysisVOs = new ArrayList<>();
            List<AccuracyAnalysisVO> accuracyAnalysisVOs = new ArrayList<>();

            for (Assignment assignment : assignments) {
                Anno anno = annoRepository.findByAssignmentIdAndImageId(assignment.getAssignmentId(), imageId);
                annos_for_this_image.add(anno);
            }

            //初始化post参数
            for (Anno anno : annos_for_this_image) {
                for(Box b: anno.getBoxes()){

                    BoxAnalysisVO boxAnalysisVO = new BoxAnalysisVO(b);
                    boxAnalysisVO.setWorkerId(anno.getWorkerId());
                    boxAnalysisVO.setBoxId(b.getId());
                    boxAnalysisVOs.add(boxAnalysisVO);

                    for(Tag t: b.getTags()){
                        TagAnalysisVO vo =  new TagAnalysisVO(t);
                        vo.setWorkerId(anno.getWorkerId());
                        vo.setBoxId(b.getId());
                        tagAnalysisVOs.add(vo);
                    }
                }
                AccuracyAnalysisVO vo = new AccuracyAnalysisVO();
                vo.setWorkerId(anno.getWorkerId());
                accuracyAnalysisVOs.add(vo);
            }

            Map<String, String> map = new HashMap<>();
            String tags_str = gson.toJson(tagAnalysisVOs);
            map.put("tags", tags_str);
            String boxes_str = gson.toJson(boxAnalysisVOs);
            map.put("boxes", boxes_str);
            String accuracies_str = gson.toJson(accuracyAnalysisVOs);
            map.put("accuracies", accuracies_str);
            int attributeSize = taskRepository.getOne(taskId).getAttributes().size();
            map.put("attributeSize", "" + attributeSize);
            map.put("imageId",""+imageId);

            String str="";
            try {
                HttpResponse r = AnalysisPostHelper.post(map, "/answer/" + assignments.get(0).getTaskId());
                Thread.sleep(1000);
                str=EntityUtils.toString(r.getEntity());
            } catch (Exception e) {
                e.printStackTrace();
                return FAIL;
            }
            String annoStr=str.split("\\|\\|")[0];
            annoStr=annoStr.replaceAll("'","\"");
            String accuracyStr=str.split("\\|\\|")[1];
            accuracyStr=accuracyStr.replaceAll("'","\"");
            accuracyStr=accuracyStr.substring(1,accuracyStr.length()-1);
            String path=WebAppConfig.BASE + "/assets/tasks/t" + taskId + "/Answers/";
            String imagePath=WebAppConfig.BASE+"/assets/tasks/t"+taskId+"/Images";
            File imageFile=new File(imagePath);
            String name= imageFile.list()[partition.getLow()-2+imageId];
            File file=new File(path);
            file.mkdirs();
            String annoPath=path+name+".json";
            writeFile(annoStr,annoPath);
            String[] temp=accuracyStr.split(",");
            List<String> strs=new ArrayList<>();
            for(int i=0;i<temp.length;i=i+2){
                strs.add(temp[i]+","+temp[i+1]);
            }
            for(String s:strs){
                if(imageId==1){
                 accuracy.add(gson.fromJson(s,AccuracyAnalysisVO.class));
                }
                else{
                    AccuracyAnalysisVO vo=gson.fromJson(s,AccuracyAnalysisVO.class);
                    for(int j=0;j<accuracy.size();j++){
                        if(accuracy.get(j).getWorkerId().equals(vo.getWorkerId())){
                            vo.setAccuracy(accuracy.get(j).getAccuracy()+vo.getAccuracy());
                            accuracy.set(j,vo);
                            break;
                        }
                    }
                }
            }

        }
        for(Assignment a:assignments){
            for(AccuracyAnalysisVO vo:accuracy){
                if(a.getWorkerId().equals(vo.getWorkerId())){
                    double temp=vo.getAccuracy()/size;
                    a.setAccuracy(temp);
                    assignmentRepository.save(a);
                    break;
                }
            }
        }

        return SUCCESS;
    }

    private ResultMessageEnum integratePartitionSegAnnoHelper(String taskId, String partitionId) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
                .create();

        //获得一个分组
        Partition partition = partitionRepository.getOne(partitionId);
        List<Assignment> assignments = partition.getAssignments();

        //按照ImageId依次整合图片的所有标注
        int size = partition.getHigh() - partition.getLow() + 1;
        for (int imageId = 1; imageId <= size; imageId++) {
            List<SegmentationAnno> annos_for_this_image = new ArrayList<>();
            for (Assignment assignment : assignments) {
                SegmentationAnno anno = segmentationAnnoRepository.findByAssignmentIdAndImageId(assignment.getAssignmentId(), imageId);
                annos_for_this_image.add(anno);

                String realPath = WebAppConfig.BASE + "/assets/" + anno.getLayerUrl().substring(7);
                List<Tag> tags = taskRepository.getOne(taskId).getAttributes().get(0).getTags();
                List<Color> colors = new ArrayList<>();
                for(Tag t : tags){
                    colors.add(new Color(Integer.parseInt(t.getColorHex().substring(1),16)));
                }
                colors.add(Color.WHITE);
                washImagePixel(realPath, colors);

            }

            String str = gson.toJson(annos_for_this_image);
            Map<String, String> map = new HashMap<>();
            map.put("annos_for_this_image", str);

            try {
                AnalysisPostHelper.post(map, "/answer/" + taskId);
            } catch (IOException e) {
                e.printStackTrace();
                return FAIL;
            }
        }

        return SUCCESS;
    }

    private static void unzip(String zipFile, String dir) {
        Expand expand = new Expand();
        expand.setSrc(new File(zipFile));
        expand.setDest(new File(dir));
        Project p = new Project();
        expand.setProject(p);
        expand.execute();
    }

    private void washImagePixel(String image, List<Color> colors) {
        Color pixelColor = null;
        File file = new File(image);
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int width = bi.getWidth();
        int height = bi.getHeight();
        int minx = bi.getMinX();
        int miny = bi.getMinY();
        for (int i = minx; i < width; i++) {
            for (int j = miny; j < height; j++) {
                int pixel = bi.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
                pixelColor = new Color(
                        (pixel & 0xff0000) >> 16,
                        (pixel & 0xff00) >> 8,
                        (pixel & 0xff)
                );

                Color washed = null;
                double min = 195075.0;
                for(Color c: colors){
                    double distance = getDistance(pixelColor, c);
                    if(distance < min){
                        min = distance;
                        washed = c;
                    }
                }

                bi.setRGB(i,j,washed.getRGB());
            }
        }
        try {
            ImageIO.write(bi, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double getDistance(Color pixelColor, Color c) {
        double delta_r = Math.pow((c.getRed()-pixelColor.getRed()),2);
        double delta_g = Math.pow((c.getGreen()-pixelColor.getGreen()),2);
        double delta_b = Math.pow((c.getBlue()-pixelColor.getBlue()),2);

        return delta_r+delta_g+delta_b;
    }

    private ResultMessageEnum writeFile(String str,String path){
        try {
            BufferedWriter bw=new BufferedWriter(new FileWriter(path));
            bw.write(str);
            bw.flush();;
            bw.close();
        } catch (IOException e) {
            return ResultMessageEnum.FAIL;
        }
        return ResultMessageEnum.SUCCESS;
    }
}
