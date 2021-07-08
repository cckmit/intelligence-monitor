package com.zhikuntech.intellimonitor.core.stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhikuntech.intellimonitor.core.stream.constants.TopicConstants;
import com.zhikuntech.intellimonitor.core.stream.dto.MonitorStructDTO;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author liukai
 */
public class DataProduceUtils {

    public static ObjectMapper serial;

    private static KafkaProducer<String, String> producer;

    static {
        // objectMapper
        serial = new ObjectMapper();
        // kafkaProducer
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.3.237:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(props);
    }

    public static void sendData(List<MonitorStructDTO> structDTOList) {
        for (MonitorStructDTO monitorStructDTO : structDTOList) {
            sendData(monitorStructDTO);
        }
    }

    public static void sendData(MonitorStructDTO structDTO) {
        // todo check
        try {
            final String dataForSend = serial.writeValueAsString(structDTO);
            ProducerRecord<String, String> record = new ProducerRecord<>(
                    TopicConstants.PUSH_TOPIC,
                    TopicConstants.TOPIC_KEY,
                    dataForSend);
            // TODO routing
            Future<RecordMetadata> future = producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (Objects.nonNull(exception)) {
                        // todo 发送数据出现异常
                        exception.printStackTrace();
                    }
                    System.out.println(metadata);
                }
            });

            RecordMetadata recordMetadata = future.get(5, TimeUnit.SECONDS);
            System.out.println(recordMetadata);
        } catch (JsonProcessingException | ExecutionException | TimeoutException | InterruptedException e) {
            e.printStackTrace();

        }
    }

}
