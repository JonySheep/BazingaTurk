package com.bazingaturk.app;

import com.bazingaturk.business.service.RecommendSvc;
import com.bazingaturk.util.enums.ResultMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/recommend")
public class RecommendController {

    private final RecommendSvc recommendSvc;

    @Autowired
    public RecommendController(RecommendSvc recommendSvc) {
        this.recommendSvc = recommendSvc;
    }

    @RequestMapping(value = "/heat",method = GET)
    public Object heatRecommend(){
        Map<String,Object> map=new HashMap<>();
        try{
        map.put("data", recommendSvc.heatRecommend());
        map.put("success",true);
        }catch(ParseException p){
            map.put("success",false);
            map.put("data",ResultMessageEnum.FAIL);
        }
        return map;
    }

    @RequestMapping(value = "/content/{user_id}",method = GET)
    public Object contentRecommend(@PathVariable("user_id")String userId ){
        Map<String,Object> map=new HashMap<>();
        map.put("success",true);
        map.put("data",recommendSvc.contentRecommend(userId));
        return map;
    }
}
