package com.xe.ui.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xe.ui.model.EmptyRequestDto;
import com.xe.ui.model.RequestDtoInterface;
import java.nio.charset.Charset;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * HttpRequestUtil 작성을 간단하게 하기 위한 Builder
 */
@Slf4j
public class XeHttpRequest {

    private ObjectMapper objectMapper;

    private final String CHARACTER_SET = "UTF-8";

    private RestTemplate restTemplate;
    private String accessToken;

    private String serverUrl;

    private String uri;
    private RequestDtoInterface payload;
    private HttpMethod method;
    private HttpHeaders headers = new HttpHeaders();

    protected MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
    protected FormHttpMessageConverter formHttpMessageConverter;

    public XeHttpRequest(String serverUrl) {
        this.serverUrl = serverUrl;

        this.objectMapper = new ObjectMapper();
        this.restTemplate = new RestTemplate();

        this.mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        this.mappingJackson2HttpMessageConverter.setDefaultCharset(Charset.forName(CHARACTER_SET));

        this.formHttpMessageConverter = new FormHttpMessageConverter();
        this.formHttpMessageConverter.setCharset(Charset.forName(CHARACTER_SET));

        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
        restTemplate.getMessageConverters().add(formHttpMessageConverter);
    }

    public XeHttpRequest get(String uri) {
        this.uri(uri);
        this.method(HttpMethod.GET);

        return this;
    }

    public XeHttpRequest post(String uri) {
        this.uri(uri);
        this.method(HttpMethod.POST);

        return this;
    }

    public XeHttpRequest put(String uri) {
        this.uri(uri);
        this.method(HttpMethod.PUT);

        return this;
    }

    public XeHttpRequest delete(String uri) {
        this.uri(uri);
        this.method(HttpMethod.DELETE);

        return this;
    }

    public XeHttpRequest method(HttpMethod method) {
        this.method = method;

        return this;
    }

    public XeHttpRequest uri(String uri) {
        this.uri = uri;

        return this;
    }

    public XeHttpRequest headers(HttpHeaders headers) {
        this.headers = headers;

        return this;
    }

    public <T extends RequestDtoInterface> XeHttpRequest payload(T payload) {
        this.payload = payload;

        return this;
    }

    public XeHttpRequest authorization(String accessToken) {
        this.accessToken = accessToken;
        this.headers.setBearerAuth(accessToken);

        return this;
    }

    public XeHttpRequest contentType(MediaType contentType) {
        restTemplate.getInterceptors().add((request, body, execution) -> {
            ClientHttpResponse response = execution.execute(request, body);
            response.getHeaders().setContentType(contentType);
            return response;
        });

        return this;
    }

    public <T> ResponseEntity<T> execute(Class<T> clazz) {
        if (payload == null) {
            this.payload = new EmptyRequestDto();
        }

        String requestUrl = this.serverUrl + this.uri;
        if (this.method.equals(HttpMethod.GET)) {
            Map<String, Object> payloadMap = this.payload.convertToMap();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.serverUrl + this.uri);
            for(String parameterName : payloadMap.keySet()) {

                builder.queryParam(parameterName, payloadMap.get(parameterName));
            }
            requestUrl = builder.build().toUriString();
        }

        try {

            HttpEntity<Map<?, ?>> httpEntity = new HttpEntity<>(
                this.payload.convertToMap(),
                this.headers
            );

            return restTemplate.exchange(
                requestUrl,
                this.method,
                httpEntity,
                clazz
            );
        } catch (HttpClientErrorException e) {
            throw e;
        } catch(Exception e) {
            log.info("Error while request to {} {}", this.serverUrl + this.uri, e);
            return new ResponseEntity<T>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public <T> T executeAndGetBodyAs(Class<T> clazz) {
        return objectMapper.convertValue(
            this.execute(clazz).getBody(),
            clazz
        );
    }

    public static class Builder {
        public static XeHttpRequest build(String serverUrl) {
            return new XeHttpRequest(serverUrl);
        }
    }
}
