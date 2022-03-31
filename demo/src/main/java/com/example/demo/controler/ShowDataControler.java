package com.example.demo.controler;

import com.example.demo.service.InputDataService;
import com.example.demo.service.ShowDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/showdata")
public class ShowDataControler {
    @Autowired
    @Qualifier("ShowDataService")
    private ShowDataService showDataService;

    @PostMapping("/updatelocal")
    public String updateLocal(@RequestBody String data) {
        return showDataService.updateDataLocal(data);
    }

    @PostMapping("/search")
    public String search(@RequestBody String data) {

        return showDataService.searchData(data);
    }

    @GetMapping("/test")
    public String test() {
        showDataService.test();
        return "0";
    }
}
