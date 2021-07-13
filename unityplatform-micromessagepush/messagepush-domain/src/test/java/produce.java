import org.apache.kafka.clients.producer.*;

import java.util.Properties;

/**
 * @author 滕楠
 * @className produce
 * @create 2021/7/13 10:31
 **/
public class produce {
    public static void main(String[] args) {

        //添加kafka的配置信息
        Properties properties = new Properties();
        //配置broker信息
        properties.put("bootstrap.servers","192.168.3.237:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.RETRIES_CONFIG,10);

        //生产者对象
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

        //封装消息
        ProducerRecord<String,String> record = new ProducerRecord<String, String>("1","00001","hello,kafka");
        //发送消息
        try {
            /*RecordMetadata recordMetadata = producer.send(record).get();
            System.out.println(recordMetadata.offset());*/
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if(e!=null){
                        e.printStackTrace();
                    }
                    System.out.println(recordMetadata.offset());
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

        //关系消息通道
        producer.close();
    }
}