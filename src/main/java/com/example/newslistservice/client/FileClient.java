package com.example.newslistservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "FileClient", url="${swfm.file-service-url}")
public interface FileClient {
    @PostMapping
    List<String> getImg(@RequestBody List<String> imgPaths);


}
