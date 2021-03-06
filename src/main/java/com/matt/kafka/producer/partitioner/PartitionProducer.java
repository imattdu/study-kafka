package com.matt.kafka.producer.partitioner;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

/**
 * @author matt
 * @create 2021-07-22 1:19
 */
public class PartitionProducer {

    public static void main(String[] args) {

        Properties properties = new Properties();
        //kafka 集群，broker-list
        properties.put("bootstrap.servers", "192.168.96.128:9092,192.168.96.129:9092,192.168.96.130:9092");
        //properties.put("acks", "all");
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        //重试次数
        properties.put("retries", 1);
        //批次大小 写入到buffer.memory
        properties.put("batch.size", 16384);
        //等待时间 1ms提交
        properties.put("linger.ms", 1);
        //RecordAccumulator 缓冲区大小
        properties.put("buffer.memory", 33554432);


        //分区器
        // com.matt.kafka.producer.partitioner.MyPartitioner
        properties.put("partitioner.class", "com.matt.kafka.producer.partitioner.MyPartitioner");

        // 序列化
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");


        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);


        for (int i = 0; i < 9; i++) {
            producer.send(new ProducerRecord<String, String>("first", "cc" + i),
                    new Callback() {
                        @Override
                        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                            System.out.println(recordMetadata.toString());
                            System.out.println("----------");
                        }
                    });
        }
        // 记得关闭资源
        producer.close();


    }
}
