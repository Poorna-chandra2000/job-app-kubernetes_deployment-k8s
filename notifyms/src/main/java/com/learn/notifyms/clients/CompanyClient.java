package com.learn.notifyms.clients;


import com.learn.notifyms.external.Company;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "companyms",url = "${COMPANY_SERVICE_URI:http://companyms:8081}")//make sure service name is proper
public interface CompanyClient {
    //call this end points at centralised location in the application
    //http://companyms:8081/companies/{id} u can full or just like this below
    @GetMapping("/companies/{id}")//url
    Company getCompany(@PathVariable("id") Long id);
    //any concrete method name just add the input part id etc patj varaiable and requestparam

    @PutMapping("/companies/rating/{companyId}/{rating}")//url
    Company avgRatingCompany(@PathVariable("companyId") Long companyId,@PathVariable double rating);

}

