package com.zhikuntech.intellimonitor.core.stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhikuntech.intellimonitor.core.stream.dto.MonitorStructDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;

import static com.zhikuntech.intellimonitor.core.stream.constants.TopicConstants.CONSUME_GROUP;
import static com.zhikuntech.intellimonitor.core.stream.constants.TopicConstants.PUSH_TOPIC;

/**
 * @author liukai
 */
public class DataConsumerUtils {

    private static KafkaConsumer<String, String> consumer;

    static {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.3.237:9092");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        props.put("group.id", CONSUME_GROUP);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset","earliest");
        consumer = new KafkaConsumer<>(props);
//        Collection<TopicPartition> topicPartitions = new ArrayList<>();
        TopicPartition partitionA = new TopicPartition(PUSH_TOPIC, 0);
        TopicPartition partitionB = new TopicPartition(PUSH_TOPIC, 1);
        TopicPartition partitionC = new TopicPartition(PUSH_TOPIC, 2);
        Collection<TopicPartition> topicPartitions = Arrays.asList(partitionA, partitionB, partitionC);
        consumer.assign(topicPartitions);
    }

    public static List<MonitorStructDTO> fetchData() {
        ConsumerRecords<String, String> poll = consumer.poll(Duration.ofSeconds(5));

        Iterable<ConsumerRecord<String, String>> records = poll.records(PUSH_TOPIC);



        List<MonitorStructDTO> results = new ArrayList<>();
        for (ConsumerRecord<String, String> record : records) {
            String value = record.value();
            MonitorStructDTO monitorStructDTO = null;
            try {
                monitorStructDTO = DataProduceUtils.serial.readValue(value, MonitorStructDTO.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if (Objects.nonNull(monitorStructDTO)) {
                results.add(monitorStructDTO);
            }
        }

        return results;
    }

    public static void commitSync() {
        consumer.commitSync();
    }

}
