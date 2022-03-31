package com.example.demo.kafkaTest;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

//@Component
public class DemoConsumer {
    /**
     * @param record record
     * @KafkaListener(groupId = "testGroup", topicPartitions = {
     * @TopicPartition(topic = "topic1", partitions = {"0", "1"}),
     * @TopicPartition(topic = "topic2", partitions = "0",
     * partitionOffsets = @PartitionOffset(partition = "1", initialOffset = "100"))
     * },concurrency = "6")
     * //concurrency就是同组下的消费者个数，就是并发消费数，必须小于等于分区总数
     */
    @KafkaListener(topics = "test", groupId = "jihuGroup")
    public void listenGroup1(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        System.out.println("jihuGroup message: " + value);
        System.out.println("jihuGroup record: " + record);
        //手动提交offset，一般是提交一个banch，幂等性防止重复消息
        // === 每条消费完确认性能不好！
        ack.acknowledge();
    }

    //配置多个消费组
    @KafkaListener(topics = "test", groupId = "jihuGroup2")
    public void listenGroup2(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        System.out.println("jihuGroup2 message: " + value);
        System.out.println("jihuGroup2 record: " + record);
        //手动提交offset
        ack.acknowledge();
    }
}
