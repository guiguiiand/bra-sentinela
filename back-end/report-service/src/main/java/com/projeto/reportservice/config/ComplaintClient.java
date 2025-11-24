package com.projeto.reportservice.config;

import com.projeto.reportservice.controller.dto.ComplaintListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "complaint-service", url = "http://localhost:3002")
public interface ComplaintClient {

    @GetMapping("/v1/complaint")
    List<ComplaintListResponse> getComplaints();
}
