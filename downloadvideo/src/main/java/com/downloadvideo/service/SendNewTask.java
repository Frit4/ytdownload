package com.downloadvideo.service;

import com.downloadvideo.model.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class SendNewTask {
    @Value("${spring.kafka.producer.topic}")
    private String topic;
    @Autowired
    private KafkaTemplate<String,Task> kafkaTemplate;

    public boolean start(Task task){
        try {
            kafkaTemplate.send(topic,task).get();
            log.info("Новая задача успешно добавлена!");
            return true;
        } catch (InterruptedException e) {
            log.error("Задача была прервана! id: {}",task.getVideoDataId(),e);
            Thread.currentThread().interrupt();
            return false;
        } catch (ExecutionException e){
            log.error("Ошибка при отправке события на загрузку! id:{}",task.getVideoDataId(),e);
            return false;
        }
    }
}
