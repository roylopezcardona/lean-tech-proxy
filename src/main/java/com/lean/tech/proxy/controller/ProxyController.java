package com.lean.tech.proxy.controller;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ProxyController {

    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<Object> proxy(@RequestParam("site") String site) {
        
        HttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        List<HttpMessageConverter<?>> httpMessageConverter = new ArrayList<>();
        httpMessageConverter.add(stringHttpMessageConverter);
        restTemplate.setMessageConverters(httpMessageConverter);
        
        HttpHeaders headers = new HttpHeaders();
        Charset utf8 = Charset.forName("UTF-8");
        MediaType mediaType = new MediaType("text", "html", utf8);
        headers.setContentType(mediaType);
        headers.set("User-Agent", "mozilla");
        
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(site, HttpMethod.GET, entity, String.class);
        String result = responseEntity.getBody();
        
        log.info("Showing headers...");
        responseEntity.getHeaders().forEach((key, value) -> {
            log.info("Header Key: {}, Header Value: {}", key, value);
        });
        log.info("Finished showing headers...");
        
        return ResponseEntity.of(Optional.of(result));
    }

}
