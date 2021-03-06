package citi.funcModule.mscard;

import citi.support.resultjson.ResultJson;
import citi.vo.MSCard;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * 接口设计：刘钟博
 * 代码填充：曹子轩
 */
//Membership card
@Controller
@RequestMapping(value = {"/mscard"},produces = {"text/html;charset=UTF-8"})
public class MSCardController {

    @Autowired
    private Gson gson;
    @Autowired
    private MSCardService msCardService;

    /**
     * 登陆成功时请求
     * @url /mscard/infos
     * @param userID 用户id
     * @param n 请求积分最多的n张卡
     * @return [{"cardID":"a7a8f255-f129-4b46-9de2-55f07c7ff65e","userID":"1ac9be07-f446-458c-b325-df2c7ecd113f","card_No":"888888","points":0}]
     */
    @ResponseBody
    @RequestMapping("/infos")
    public String getMSInfo(String userID,Integer n){
        if(n==null)
            return "[]";
        List<MSCard> cards = msCardService.getInfo(userID);
        if(cards==null){
            return "[]";
        }
        ArrayList<BriefCard> briefCards = msCardService.changeToBriefCards(cards);
        if(briefCards==null)
            return "[]";
        Collections.sort(briefCards);
        ArrayList<BriefCard> results = new ArrayList<>();
        for(int i=0;i<n&&i<briefCards.size();i++){
            results.add(briefCards.get(i));
        }
        String jsonStr = gson.toJson(results);
        return jsonStr;
    }

    @ResponseBody
    @RequestMapping("/getNum")
    public String getCardNum(String userID){
        int num = msCardService.getCardNum(userID);
        return "{\"num\": "+ num + "}";
    }

    @ResponseBody
    @RequestMapping("/getDetailCard")
    public String getDetailCard(String userID, String merchantID){
        DetailCard detailCard = msCardService.getDetailCard(userID,merchantID);

        return gson.toJson(detailCard);
    }



    /**
     * @ url:/mscard/addcard
     *@param userID
     * @return 成功：{"status":true}, {"status":false}
     */
    @ResponseBody
    @RequestMapping("/addcard")
    public String addMSCard(String userID, String merchantID, String cardNum, String password){
        boolean flag = msCardService.addMSCard(userID,merchantID,cardNum,password);
        if(flag)
            return ResultJson.SUCCESS;
        return ResultJson.FAILURE;
    }

    /*
     *@param userID, merchantID, cardNum
     * @url: /mscard/unbindcard
     * return: {"status":true}, {"status":false}
     */
    @ResponseBody
    @RequestMapping("/unbindcard")
    public String unbindcard(String userID, String merchantID, String cardNum){
        boolean flag = msCardService.unbindcard(userID, merchantID, cardNum);
        if(flag)
            return ResultJson.SUCCESS;
        return ResultJson.FAILURE;
    }
}
