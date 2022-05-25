package com.example.demo.controler;

import com.example.demo.service.InputDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


//@RestController
//@RequestMapping("/inputData")
public class InputDataControler {

    @Autowired
    @Qualifier("InputDataService")
    private InputDataService inputDataService;





    @PostMapping("/")
    public String inputData(@RequestParam String rsiData){
        return ""+inputDataService.saveData(rsiData);
    }

    @Transactional
    @GetMapping("/debug")
    public void debug(){


        int day,hour,minute,second;
        /*
        reportService.generateDailyReport("2022-04-29");
        reportService.generateDailyReport("2022-04-30");
        reportService.generateDailyReport("2022-05-01");
        reportService.generateDailyReport("2022-05-02");
        reportService.generateDailyReport("2022-05-03");
        reportService.generateDailyReport("2022-05-04");
        reportService.generateDailyReport("2022-05-05");
        reportService.generateDailyReport("2022-05-06");
        reportService.generateDailyReport("2022-05-07");
        reportService.generateDailyReport("2022-05-08");
        reportService.generateDailyReport("2022-05-09");
        */

        /*
        for(day=29;day<=30;day++){
            for(hour=0;hour<24;hour++){
                for(minute=0;minute<60;minute++){
                    for(second=0;second<60;second++){
                        String name="D:\\Users\\zxr\\Documents\\Tencent Files\\326968413\\FileRecv\\result\\"
                                +"2022-04-"+String.format("%02d",day)+"T"+String.format("%02d",hour)+"^%"
                                +String.format("%02d",minute)+"^%"+String.format("%02d",second)+"Z.json";
                        File file=new File(name);
                        if(!file.exists()){
                            continue;
                        }
                        System.out.println("file:"+name+" founded");
                        try {
                            FileInputStream fileInputStream=new FileInputStream(file);
                            byte[] bys=new byte[16384];
                            while (fileInputStream.read(bys,0,16384)!=-1);
                            fileInputStream.close();
                            inputDataService.saveData(new String(bys));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            continue;
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            }

        }
        */
    /*
        for(day=1;day<=9;day++){
            for(hour=0;hour<24;hour++){
                for(minute=0;minute<60;minute++){
                    for(second=0;second<60;second++){
                        String name="D:\\Users\\zxr\\Documents\\Tencent Files\\326968413\\FileRecv\\result\\"
                                +"2022-05-"+String.format("%02d",day)+"T"+String.format("%02d",hour)+"^%"
                                +String.format("%02d",minute)+"^%"+String.format("%02d",second)+"Z.json";
                        File file=new File(name);
                        if(!file.exists()){
                            continue;
                        }
                        System.out.println("file:"+name+" founded");
                        try {
                            FileInputStream fileInputStream=new FileInputStream(file);
                            byte[] bys=new byte[16384];
                            while (fileInputStream.read(bys,0,16384)!=-1);
                            fileInputStream.close();
                            inputDataService.saveData(new String(bys));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            continue;
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            }

        }

     */
        System.out.println("====================ENDED!========================");
    }

}
