package com.example.demo.controler;

import com.example.demo.service.MeasuringPointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;

@RestController
@RequestMapping("/measuringPoints")
public class MeasuringPointsControler {

    @Autowired
    private MeasuringPointsService measuringPointsService;

    @PostMapping("/getData")
    public String getData( @RequestBody String requestData){
        try {
            return measuringPointsService.getMeasuringPointsData();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }

    }

    @PostMapping("/add")
    public String add( @RequestBody String requestData){
        return measuringPointsService.addMeasuringPoint(requestData);
    }

    @PostMapping("/edit")
    public String edit( @RequestBody String requestData){
        return measuringPointsService.editMeasuringPoint(requestData);
    }

    @PostMapping("/del")
    public String del( @RequestBody String requestData){
        return measuringPointsService.deleteMeasuringPoint(requestData);
    }
}
