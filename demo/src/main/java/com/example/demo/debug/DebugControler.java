package com.example.demo.debug;

import com.example.demo.mapper.MeasuringPointsMapper;
import com.example.demo.service.InputDataService;
import com.example.demo.service.MeasuringPointsService;
import com.example.demo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/debug")
public class DebugControler {

    @Autowired
    @Qualifier("InputDataService")
    private InputDataService inputDataService;
    @Autowired
    @Qualifier("ReportService")
    private ReportService reportService;
    @Autowired
    @Qualifier("MeasuringPointsService")
    private MeasuringPointsService measuringPointsService;
    @Autowired
    private MeasuringPointsMapper measuringPointsMapper;
    @Autowired
    private DebugService debugService;


    @GetMapping("test")
    public String debug2(){
        return "test";
    }


    //@GetMapping("/readData")
    public String debug(){
        //debugService.readData1(0);
    /*
        CompletableFuture<String> a0=debugService.readData(0);
        CompletableFuture<String> a1=debugService.readData(1);
        CompletableFuture<String> a2=debugService.readData(2);
        CompletableFuture<String> a3=debugService.readData(3);
        CompletableFuture<String> a4=debugService.readData(4);
        CompletableFuture<String> a5=debugService.readData(5);
        CompletableFuture<String> a6=debugService.readData(6);
        CompletableFuture<String> a7=debugService.readData(7);
        CompletableFuture<String> a8=debugService.readData(8);
        CompletableFuture<String> a9=debugService.readData(9);


        CompletableFuture.allOf(a0,a1,a2,a3,a4,a5,a6,a7,a8,a9).join();
        System.out.println("===============FULL COMPLETED!============");
        return "{finished}";


     */
        return null;
    }
}
