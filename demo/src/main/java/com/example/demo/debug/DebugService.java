package com.example.demo.debug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.mapper.MeasuringPointsMapper;
import com.example.demo.service.InputDataService;
import com.example.demo.service.InputDataServiceImp;
import com.example.demo.service.MeasuringPointsService;
import com.example.demo.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Transactional
@Service
public class DebugService {

    private static final String windowsDebugPath="D:\\Works\\graduationProject\\lx_data";
    private static final String debugPath="/data/zxr/lx_data";
    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    //@Qualifier("InputDataService")
    private InputDataServiceImp inputDataService;
    @Autowired
    @Qualifier("ReportService")
    private ReportService reportService;
    @Autowired
    @Qualifier("MeasuringPointsService")
    private MeasuringPointsService measuringPointsService;
    @Autowired
    private MeasuringPointsMapper measuringPointsMapper;




    @Async("taskExecutor")
    public CompletableFuture<String> readData(int num, int pStart, int pEnd,int iStart,int iEnd){
        String str="D:\\Works\\graduationProject\\lx_data\\IE0135\\2022-05-13T07_15_23Z.json";
        File f=new File(str);
        System.out.println(f.exists());

        try {
            FileInputStream fileInputStream1 = new FileInputStream(f);
            System.out.println(fileInputStream1==null);
            byte[] b=new byte[32768];
            while (fileInputStream1.read(b,0,32768)!=-1);
            fileInputStream1.close();
            //System.out.println(fs[j].toString());
            inputDataService.saveData(new String(b));
        } catch (Exception e) {
            e.printStackTrace();
        }




        File all=new File("D:\\Works\\graduationProject\\lx_data");
        File[] pfs = all.listFiles();
        int index=AsyncConfiguration.num;
        byte[] bys;
        int length= Math.min(pfs.length, pEnd);
        for(int i=pStart;i<length;i++){
            File[] fs=pfs[i].listFiles();
            int jLength=Math.min(fs.length,iEnd);
            for (int j = iStart; j < jLength; j++) {
                if(j%index==num){
                    try {
                        FileInputStream fileInputStream=new FileInputStream(fs[j]);
                        if(fileInputStream.getChannel()==null){
                            fileInputStream.close();
                            logger.warn(fs[j].toString()+" may not existed, or is empty");
                            continue;
                        }else if(fileInputStream.getChannel().size()<=13){
                            fileInputStream.close();
                            logger.warn(fs[j].toString()+" may not existed, or is empty");
                            continue;
                        }
                        bys=new byte[32768];
                        while (fileInputStream.read(bys,0,32768)!=-1);
                        fileInputStream.close();
                        //System.out.println(fs[j].toString());
                        inputDataService.saveData(new String(bys));
                        bys=null;
                    } catch (Exception e){
                        e.printStackTrace();
                        logger.error("ERROR:"+fs[j].toString()+"\n"+e.toString());
                    }
                }
            }
        }

        System.out.println("====================ENDED!========================");
        return CompletableFuture.completedFuture("---");
    }

    /**
     * 服务器版本
     * @param num
     * @return
     */
    @Async("taskExecutor")
    public CompletableFuture<String> readData2(int num, int pStart, int pEnd,int iStart,int iEnd){
        File all=new File(debugPath);
        File[] pfs = all.listFiles();
        int index=AsyncConfiguration.num;
        byte[] bys;
        int length= Math.min(pfs.length, pEnd);
        for(int i=pStart;i<length;i++){
            File tempFile=new File(pfs[i].toString()+"/home/result");
            File[] fs=tempFile.listFiles();
            int jLength=Math.min(fs.length,iEnd);
            for (int j = iStart; j < jLength; j++) {
                if(j%index==num){
                    try {
                        FileInputStream fileInputStream=new FileInputStream(fs[j]);
                        if(fileInputStream.getChannel()==null){
                            fileInputStream.close();
                            logger.warn(fs[j].toString()+" may not existed, or is empty");
                        }else if(fileInputStream.getChannel().size()<=13){
                            fileInputStream.close();
                            logger.warn(fs[j].toString()+" may not existed, or is empty");
                        }else {
                            bys=new byte[32768];
                            while (fileInputStream.read(bys,0,32768)!=-1);
                            fileInputStream.close();
                            //System.out.println(fs[j].toString());
                            inputDataService.saveData(new String(bys));
                            bys=null;
                        }

                    } catch (Exception e){
                        e.printStackTrace();
                        logger.error("ERROR:"+fs[j].toString()+"\n"+ e);
                    }
                }
            }
            logger.info(pfs[i].toString()+" finished!");
        }

        System.out.println("====================ENDED!========================");
        return CompletableFuture.completedFuture("---");
    }



    @Async("taskExecutor")
    public CompletableFuture<String> generateReport(int asyncIndex){
        generate24Report(asyncIndex,"2022-04-29");
        generate24Report(asyncIndex,"2022-04-30");
        generate24Report(asyncIndex,"2022-05-01");
        generate24Report(asyncIndex,"2022-05-02");
        generate24Report(asyncIndex,"2022-05-03");
        generate24Report(asyncIndex,"2022-05-04");
        generate24Report(asyncIndex,"2022-05-05");
        generate24Report(asyncIndex,"2022-05-06");
        generate24Report(asyncIndex,"2022-05-07");
        generate24Report(asyncIndex,"2022-05-08");
        generate24Report(asyncIndex,"2022-05-09");
        generate24Report(asyncIndex,"2022-05-10");
        generate24Report(asyncIndex,"2022-05-11");
        generate24Report(asyncIndex,"2022-05-12");
        generate24Report(asyncIndex,"2022-05-13");
        generate24Report(asyncIndex,"2022-05-14");
        return CompletableFuture.completedFuture("---");
    }

    private void generate24Report(int asyncIndex,String dateString){
        int asyncTotal=AsyncConfiguration.num;
        for (int i = 0; i < 24; i++) {
            if(i%asyncTotal==asyncIndex){
                String d=dateString+"T"+String.format("%02d", i);
                reportService.generateHourlyReport(d);
            }
        }
    }

    public void readSingledata(String url){

        try {
            File file=new File(url);
            if(!file.exists()){
                System.out.println("File not existed");
                return;
            }
            FileInputStream fileInputStream=new FileInputStream(file);
            if(fileInputStream.getChannel()==null){
                fileInputStream.close();
                System.out.println("file may not existed, or is empty");
            }else if(fileInputStream.getChannel().size()<=13){
                fileInputStream.close();
                System.out.println("file may not existed, or is empty");
            }else {
                byte[] bys=new byte[32768];
                while (fileInputStream.read(bys,0,32768)!=-1);
                fileInputStream.close();
                //System.out.println(fs[j].toString());
                inputDataService.saveData(new String(bys));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readData1(int num){
        File all=new File("D:\\Works\\graduationProject\\lx_data");
        File[] pfs = all.listFiles();
        int index=AsyncConfiguration.num;
        byte[] bys;
        int length= pfs.length;
        for(int i=0;i<15;i++){
            File[] fs=pfs[i].listFiles();
            int jLength=fs.length;
            for (int j = 0; j < jLength; j++) {
                try {
                    FileInputStream fileInputStream=new FileInputStream(fs[j]);
                    bys=new byte[32768];
                    while (fileInputStream.read(bys,0,32768)!=-1);
                    fileInputStream.close();
                    System.out.println(fs[j].toString());
                    JSONObject rsiData13 = JSON.parseObject(new String(bys));
                    System.out.println(inputDataService.checkProvider(rsiData13));
                    System.out.println(inputDataService.saveData(new String(bys)));
                    bys=null;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    continue;
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }

        System.out.println("====================ENDED!========================");
    }
}
