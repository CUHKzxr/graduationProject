package com.example.demo.debug;

import com.example.demo.report.FindMedian;
import com.example.demo.report.QuickSort;
import com.example.demo.report.Report;
import com.example.demo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;


@SpringBootApplication
public class DebugApplication implements ApplicationRunner {

    @Autowired
    private DebugService debugService;
    @Autowired
    @Qualifier("ReportService")
    private ReportService reportService;

    public static void main(String[] args) {
        SpringApplication.run(DebugApplication.class, args);
    }
    @Override
    public void run(ApplicationArguments arg0) throws Exception {
        Scanner scanner=new Scanner(System.in);
        System.out.println("debug? y/n");
        String str=scanner.nextLine();
        if(str.equals("y")){
            System.out.println("debug:\n 1:input data   2:generate report");
            int c=scanner.nextInt();
            if(c==1){
                debug1();
            }else if (c==2){
                debug2();
            }
        }

    }
    private void debug2(){
        Scanner scanner=new Scanner(System.in);
        System.out.println("test? y/n");
        String str=scanner.nextLine();
        if(str.equals("y")){
            int[] arr=new int[100];
            for (int i = 0; i < 100; i++) {
                arr[i]=(int)(Math.random()*100);
            }
            FindMedian f=new QuickSort();
            int r=f.getMedian(arr,0,99);
            Boolean flag=true;
            for (int i = 0; i < 99; i++) {
                if(arr[i]>arr[i+1]){
                    flag=false;
                    break;
                }
            }
            System.out.println(flag);
            reportService.generateHourlyReport("2022-04-30T08");
        }else {
            CompletableFuture<String> a0=debugService.generateReport(0);
            CompletableFuture<String> a1=debugService.generateReport(1);
            CompletableFuture<String> a2=debugService.generateReport(2);
            CompletableFuture<String> a3=debugService.generateReport(3);

            CompletableFuture.allOf(a0,a1,a2,a3).join();
        }
        System.out.println("===============FULL COMPLETED!============");
    }
    private void debug1(){
        Scanner scanner=new Scanner(System.in);
        System.out.println("read single file? y/n");
        String str=scanner.nextLine();
        if(str.equals("y")){
            System.out.println("url:");
            str=scanner.nextLine();
            debugService.readSingledata(str);
        }else {
            int pStart,pEnd, iStart, iEnd;
            System.out.println("provider start,provider end,file start, file end");
            pStart=scanner.nextInt();
            pEnd=scanner.nextInt();
            iStart=scanner.nextInt();
            iEnd=scanner.nextInt();
            scanner.close();
            if(pStart==0 && pEnd==0){
                return;
            }
            int num=AsyncConfiguration.num;
            CompletableFuture<String> a0=debugService.readData2(0,pStart,pEnd,iStart,iEnd);
            CompletableFuture<String> a1=debugService.readData2(1,pStart,pEnd,iStart,iEnd);
            CompletableFuture<String> a2=debugService.readData2(2,pStart,pEnd,iStart,iEnd);
            CompletableFuture<String> a3=debugService.readData2(3,pStart,pEnd,iStart,iEnd);



            CompletableFuture.allOf(a0,a1,a2,a3).join();

        }
        System.out.println("===============FULL COMPLETED!============");
    }
}
