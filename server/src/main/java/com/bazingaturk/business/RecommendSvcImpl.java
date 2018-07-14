package com.bazingaturk.business;

import com.bazingaturk.business.service.RecommendSvc;
import com.bazingaturk.business.service.UserSvc;
import com.bazingaturk.business.vo.taskVO.TaskSimpleVO;
import com.bazingaturk.data.entity.anno.Attribute;
import com.bazingaturk.data.entity.tasks.Assignment;
import com.bazingaturk.data.entity.tasks.Task;
import com.bazingaturk.data.repository.AssignmentRepository;
import com.bazingaturk.data.repository.TaskRepository;
import com.bazingaturk.util.enums.TaskStatusEnum;
import com.bazingaturk.util.enums.TaskTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RecommendSvcImpl implements RecommendSvc {

    final private TaskRepository taskRepository;
    final private AssignmentRepository assignmentRepository;
    final private UserSvc userSvc;

    @Autowired
    public RecommendSvcImpl(TaskRepository taskRepository, AssignmentRepository assignmentRepository, UserSvc userSvc) {
        this.taskRepository = taskRepository;
        this.assignmentRepository = assignmentRepository;
        this.userSvc = userSvc;
    }

    @Override
    public List<TaskSimpleVO> heatRecommend() throws ParseException {
        List<Task> tasks = taskRepository.findByStatus(TaskStatusEnum.ONGOING);
        if (tasks == null || tasks.size() < 1)
            return null;
        List<TaskSimpleVO> result = new ArrayList<>();
        for (Task task : tasks) {
            TaskSimpleVO vo = new TaskSimpleVO(task);
            vo.setRequesterName(userSvc.getUserInfo(vo.getRequesterId()).getName());
            vo.setHeat(calculateHeat(task));
            result.add(vo);
        }
        result.sort(new SortByHeat());
        return result;
    }

    @Override
    public List<TaskSimpleVO> contentRecommend(String userId) {
        List<String> userKeyWords = getUserKeyWords(userId);
        int classificationCount = getNumOfTasks(userId, TaskTypeEnum.CLASSIFICATION);
        int detectionCount = getNumOfTasks(userId, TaskTypeEnum.DETECTION);
        int segmentationCount = getNumOfTasks(userId, TaskTypeEnum.SEGMENTATION);
        int taskCount = classificationCount + detectionCount + segmentationCount;
        if (userKeyWords == null || userKeyWords.size() < 1)
            return null;
        List<Task> tasks = taskRepository.findByStatus(TaskStatusEnum.ONGOING);
        if (tasks == null || tasks.size() < 1)
            return null;
        List<TaskSimilarity> similarities = new ArrayList<>();
        for (Task task : tasks) {
            double rate = 0.0;
            switch (task.getTaskType()) {
                case DETECTION:
                    rate = detectionCount / taskCount;
                    break;
                case CLASSIFICATION:
                    rate = classificationCount / taskCount;
                    break;
                case SEGMENTATION:
                    rate = segmentationCount / taskCount;
                    break;
            }
            similarities.add(new TaskSimilarity(task.getTaskId(), rate * calculateSimilarity(userKeyWords, getTaskKeyWords(task))));
        }
        similarities.sort(new SortBySimilarity());
        List<TaskSimpleVO> result = new ArrayList<>();
        for (TaskSimilarity temp : similarities) {
            TaskSimpleVO vo = new TaskSimpleVO(taskRepository.getOne(temp.getTaskId()));
            vo.setRequesterName(userSvc.getUserInfo(vo.getRequesterId()).getName());
            result.add(vo);
        }
        return result;
    }

    private int getNumOfTasks(String userId, TaskTypeEnum type) {
        List<Assignment> assignments = assignmentRepository.findByWorkerId(userId);
        int count = 0;
        for (Assignment a : assignments) {
            if (taskRepository.getOne(a.getTaskId()).getTaskType().equals(type))
                count++;
        }
        return count;
    }

    //计算余弦相似度
    private double calculateSimilarity(List<String> str1, List<String> str2) {
        Map<String, int[]> vectorSpace = new HashMap<>();
        int[] itemCountArray;
        int size1 = str1.size();
        String[] strArray1 = str1.toArray(new String[size1]);

        for (String aStrArray1 : strArray1) {
            if (vectorSpace.containsKey(aStrArray1))
                ++(vectorSpace.get(aStrArray1)[0]);
            else {
                itemCountArray = new int[2];
                itemCountArray[0] = 1;
                itemCountArray[1] = 0;
                vectorSpace.put(aStrArray1, itemCountArray);
            }
        }
        int size2 = str2.size();
        String[] strArray2 = str2.toArray(new String[size2]);
        for (String aStrArray2 : strArray2) {
            if (vectorSpace.containsKey(aStrArray2))
                ++(vectorSpace.get(aStrArray2)[1]);
            else {
                itemCountArray = new int[2];
                itemCountArray[0] = 0;
                itemCountArray[1] = 1;
                vectorSpace.put(aStrArray2, itemCountArray);
            }
        }

        //计算相似度
        double vector1Modulo = 0.00;//向量1的模
        double vector2Modulo = 0.00;//向量2的模
        double vectorProduct = 0.00; //向量积

        for (Object o : vectorSpace.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            itemCountArray = (int[]) entry.getValue();

            vector1Modulo += itemCountArray[0] * itemCountArray[0];
            vector2Modulo += itemCountArray[1] * itemCountArray[1];

            vectorProduct += itemCountArray[0] * itemCountArray[1];
        }

        vector1Modulo = Math.sqrt(vector1Modulo);
        vector2Modulo = Math.sqrt(vector2Modulo);

        //返回相似度
        return (vectorProduct / (vector1Modulo * vector2Modulo));
    }


    private List<String> getUserKeyWords(String userId) {
        List<Assignment> assignments = assignmentRepository.findByWorkerId(userId);
        if (assignments == null || assignments.size() < 1)
            return null;
        List<String> userKeys = new ArrayList<>();
        for (Assignment assignment : assignments) {
            Task task = taskRepository.getOne(assignment.getTaskId());
            userKeys.addAll(getTaskKeyWords(task));
        }
        return userKeys;
    }

    private List<String> getTaskKeyWords(Task task) {
        List<String> result = new ArrayList<>();
        List<Attribute> attributes = task.getAttributes();
        for (Attribute a : attributes) {
            result.add(a.getName());
        }
        return result;
    }

    private double calculateHeat(Task task) throws ParseException {
        double userScore = 100 + task.getReceiveTimes() * 100 + task.getPageViews() * 5;
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.CHINA);
        Date today = new Date();
        Date create = format.parse(task.getCreateTime());
        long nm = 1000 * 60 * 60 * 6;
        double timeScore = Math.pow(Math.E, (today.getTime() - create.getTime()) / nm);
        return userScore / timeScore;
    }

    class SortByHeat implements Comparator<TaskSimpleVO> {
        @Override
        public int compare(TaskSimpleVO v1, TaskSimpleVO v2) {
            if (v1.getHeat() < v2.getHeat()) {
                return 1;
            } else if (v1.getHeat() == v2.getHeat())
                return 0;
            else
                return -1;
        }
    }

    class SortBySimilarity implements Comparator<TaskSimilarity> {
        @Override
        public int compare(TaskSimilarity v1, TaskSimilarity v2) {
            return Double.compare(v2.getSimilarity(), v1.getSimilarity());
        }
    }

    @NoArgsConstructor
    @Data
    class TaskSimilarity {
        String taskId;
        double similarity;

        TaskSimilarity(String taskId, double similarity) {
            this.taskId = taskId;
            this.similarity = similarity;
        }
    }
}
