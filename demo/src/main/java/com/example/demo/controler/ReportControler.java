package com.example.demo.controler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/report")
public class ReportControler {
    private static final String check="00:00:00";

    @Autowired
    @Qualifier("ReportService")
    private ReportService reportService;

    @PostMapping("/getReport")
    public String getReport(@RequestBody String requestString){

        JSONArray timestampRange=JSON.parseObject(requestString).getJSONObject("data").getJSONArray("timestamprange");
        if(timestampRange.size()!=2){
            return "{code:400}";//TODO
        }
        String startTime=timestampRange.getString(0);
        String endTime=timestampRange.getString(1);
        return reportService.getExistedReport(startTime,endTime);
    }

}
