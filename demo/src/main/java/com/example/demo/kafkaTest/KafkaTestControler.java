package com.example.demo.kafkaTest;


import com.example.demo.DnsAnalysisApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

//@RestController
public class KafkaTestControler {
    /**
    @Autowired
    private KafkaTemplate<Object,Object> template;

    @GetMapping("/send/{input}")
    public void sendFoo(@PathVariable String input) {
        this.template.send("topic_input", input);
    }
    @KafkaListener(id = "webGroup", topics = "topic_input")
    public void listen(String input) {
        DnsAnalysisApplication.logger.info("input value: {}" , input);
    }
    */

    private final static String TOPIC_NAME = "test";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @RequestMapping("/send")
    public String send(@RequestParam("msg") String msg) {
        kafkaTemplate.send(TOPIC_NAME, "key", msg);
        return String.format(" %s ", msg);
    }

}
