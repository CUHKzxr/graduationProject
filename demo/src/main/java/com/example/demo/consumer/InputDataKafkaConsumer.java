package com.example.demo.consumer;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.service.InputDataService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
public class InputDataKafkaConsumer {

    @Autowired
    @Qualifier("InputDataService")
    private InputDataService inputDataService;

    @KafkaListener(topics = "rsiDataTest", id="01")
    public void receiverProducerRecord(List<ConsumerRecord<String, String>> consumerRecords, Acknowledgment acknowledgment){
        System.out.println(consumerRecords.size());
        for(ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            System.out.println("receiverProducerRecord key is " + JSONObject.toJSONString(consumerRecord.key()));
            System.out.println("receiverProducerRecord value is " + JSONObject.toJSONString(consumerRecord.value()));
            inputDataService.saveData(consumerRecord.value());
        }
        // 手动提交offset
        acknowledgment.acknowledge();
    }

}
