package com.learn.notifyms.consumer;

import com.learn.notifyms.NotifyEntity;
import com.learn.notifyms.NotifyRepository;
import com.learn.notifyms.clients.CompanyClient;
import com.learn.reviewms.event.Ratingevent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyRatingConsumer {
    private final NotifyRepository notifyRepository;
    private final CompanyClient companyClient;

    //now here we need to use consumer i.e kafka listener
    @KafkaListener(topics = "company-rating-topic")
    public void handleCompanyRatingTopic(Ratingevent ratingevent){
            log.info("companyid:"+ratingevent.getCompanyId()+" rating"+ratingevent.getRating());
            //update here itself
//the event is not recignised as entity so make sure create a entity and maps events to entity and save it in repo
        NotifyEntity notifyEntity=NotifyEntity.builder()
                .companyId(ratingevent.getCompanyId())
                .rating(ratingevent.getRating())
                .build();

        notifyRepository.save(notifyEntity);

        List<NotifyEntity> getallratingofCompany=notifyRepository.findByCompanyId(ratingevent.getCompanyId());
        double avg=getallratingofCompany
                .stream()
                .mapToDouble(NotifyEntity::getRating)
                .average()
                .orElse(0.0);

        companyClient.avgRatingCompany(ratingevent.getCompanyId(),avg);

    }

}
