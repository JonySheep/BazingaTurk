package com.bazingaturk.business.service;

import com.bazingaturk.business.vo.taskVO.TaskSimpleVO;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public interface RecommendSvc {
    public List<TaskSimpleVO>  heatRecommend() throws ParseException;
    public List<TaskSimpleVO> contentRecommend(String userId);
}
