package com.example.SimbirsoftPractice.feign;

import com.example.SimbirsoftPractice.rest.dto.CustomerWithUUIDRequestDto;
import com.example.SimbirsoftPractice.rest.dto.PaymentProjectRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "${feign.client.name}",
                url = "${feign.client.url}")
public interface PaymentClient {
    @PostMapping(value = "/clients")
    ResponseEntity<String> createClient(@RequestBody CustomerWithUUIDRequestDto request);

    @PostMapping(value = "/clients/openProject")
    ResponseEntity<String> payProject(@RequestBody PaymentProjectRequestDto paymentProjectDto);
}
