package com.example.demo.controler;

import com.example.demo.service.InputDataService;
import com.example.demo.service.InputDataServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inputData")
public class InputDataControler {

    @Autowired
    @Qualifier("InputDataService")
    private InputDataService inputDataService;

    @PostMapping("/")
    public String inputData(@RequestParam String rsiData){
        return ""+inputDataService.saveData(rsiData);
    }

}
