package com.learn.reviewms.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class KafkaTopicConfig {

    //you can create topic here directly or use application properties left to you

    @Value("${kafka.topic.company-rating_topic}")
    private String KAFKA_COMPANY_RATING_TOPIC;


    //advance part
    //bean of NewTopic is to be Created
    @Bean
    public NewTopic companyRating(){
        log.info("Company got a new rating");
        return new NewTopic(KAFKA_COMPANY_RATING_TOPIC,3,(short) 1);//1 is replication factor
    }

    //use kafka template and send the required object details to notify and save it there
    //take the avertage there and save it again
}
