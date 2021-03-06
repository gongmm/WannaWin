package citi.funcModule.recommend;


import citi.persist.procedure.probean.ActivityBean;
import citi.support.resultjson.ResultJson;
import citi.support.resultjson.SerializeGson;
import citi.vo.Activity;
import citi.vo.Item;

import citi.vo.Type;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;

import static citi.funcModule.recommend.RecommendService.cosineSimilarity;


/**
 *协同过滤的推荐算法
 */

@RequestMapping(value = {"/recommend"},produces = {"text/json;charset=UTF-8"})
@Controller
public class RecommendController {
    @Autowired
    Gson gson;
    @Autowired
    citi.funcModule.recommend.RecommendService recommendService;


    @ResponseBody
    @RequestMapping("isInvestigated")
    public String isInvestigated(String userID){
        if(recommendService.isInvestigated(userID)){
            return ResultJson.SUCCESS;
        }
        return ResultJson.FAILURE;
    }

    @ResponseBody
    @RequestMapping("getAds")
    public String getAds(String userID){
        ArrayList<ActivityBean> activities = recommendService.getAds(userID);
        return gson.toJson(activities);
    }

    @ResponseBody
    @RequestMapping("/initPref")
    public String initPref(boolean type1,boolean type2,boolean type3,boolean type4,boolean type5,boolean type6,String userID){
        ArrayList<Type.ItemType> prefList = new ArrayList<Type.ItemType>();
        if(type1){
            prefList.add(Type.ItemType.normal);
        }
        if(type2){
            prefList.add(Type.ItemType.catering);
        }
        if(type3){
            prefList.add(Type.ItemType.communication);
        }
        if(type4){
            prefList.add(Type.ItemType.costume);
        }
        if(type5){
            //prefList.add('1');
        }
        if(type6){
            //prefList.add('1');
        }
        if(recommendService.initPref(userID,prefList))
            return ResultJson.SUCCESS;
        else
            return ResultJson.FAILURE;
    }


    /**
     * 记录用户的浏览记录
     * @param1 ItemID 被浏览商品的ID
     * @Return status [{"status":"true/false"}]
     */
    @ResponseBody
    @RequestMapping("/addRecord")
    public String addRecord(String itemID, Timestamp time){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        String userID = (String)session.getAttribute("userID");
        if(recommendService.addRecord(userID,itemID,time))
            return "[{\"status\":\"true\"}]";
        else
            return "[{\"status\":\"false\"}]";
    }



    /**
     * 获取用户的商品推荐列表(暂定只推荐2个)
     * @Return itemList
     */
    @ResponseBody
    @RequestMapping("/getRecommendedItems")
    public String getRecommendedItems(String userID){
        if(userID==null){
            userID = "06bef837-9ad4-4e8e-9d3d-f20275d6fcb5";
        }
        return gson.toJson(recommendService.getRecommendedItems(userID,3));
    }

    @ResponseBody
    @RequestMapping("/getRecommendedMerchants")
    public String getRecommendedMerchants(String userID){
        if(userID==null){
            userID = "06bef837-9ad4-4e8e-9d3d-f20275d6fcb5";
        }
        return SerializeGson.GSON.toJson(recommendService.getRecommendedMerchants(userID));
    }


//    /**
//     * 获取用户的商户推荐列表(暂定只推荐5个)
//     * @Return itemList
//     */
//    @ResponseBody
//    @RequestMapping("/getRecommendedItems")
//    public String getRecommendedItems(){
//        if(recommendService.addRecord(itemID))
//            return "[{\"status\":\"true\"}]";
//        else
//            return "[{\"status\":\"false\"}]";
//    }
}
