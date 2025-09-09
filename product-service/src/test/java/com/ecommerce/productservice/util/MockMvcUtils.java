package com.ecommerce.productservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;

@Component
public class MockMvcUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public <T> ResponseEntity<T> performGet(String path, Class<T> responseClass) {
        return performRequest(MockMvcRequestBuilders.get(path), null, responseClass, null);
    }

    public <T> ResponseEntity<T> performGet(String path, TypeReference<T> responseType) {
        return performRequest(MockMvcRequestBuilders.get(path), null, null, responseType);
    }

    public <T> ResponseEntity<T> performPost(String path, Object body, Class<T> responseClass) {
        return performRequest(MockMvcRequestBuilders.post(path), body, responseClass, null);
    }

    public <T> ResponseEntity<T> performPost(String path, Object body, TypeReference<T> responseType) {
        return performRequest(MockMvcRequestBuilders.post(path), body, null, responseType);
    }

    public <T> ResponseEntity<T> performPut(String path, Object body, Class<T> responseClass) {
        return performRequest(MockMvcRequestBuilders.put(path), body, responseClass, null);
    }

    public <T> ResponseEntity<T> performPut(String path, Object body, TypeReference<T> responseType) {
        return performRequest(MockMvcRequestBuilders.put(path), body, null, responseType);
    }

    public <T> ResponseEntity<T> performDelete(String path, Class<T> responseClass) {
        return performRequest(MockMvcRequestBuilders.delete(path), null, responseClass, null);
    }

    public <T> ResponseEntity<T> performDelete(String path, TypeReference<T> responseType) {
        return performRequest(MockMvcRequestBuilders.delete(path), null, null, responseType);
    }

    private <T> ResponseEntity<T> performRequest(MockHttpServletRequestBuilder request, Object body, Class<T> responseClass, TypeReference<T> responseType) {
        try {
            if (body != null) {
                String jsonBody = (body instanceof String) ? (String) body : objectMapper.writeValueAsString(body);
                request.contentType(MediaType.APPLICATION_JSON).content(jsonBody);
            }

            request.accept(MediaType.APPLICATION_JSON);
            MvcResult mvcResult = mockMvc.perform(request).andReturn();

            return convertToResponseEntity(mvcResult, responseClass, responseType);
        } catch (Exception e) {
            throw new RuntimeException("Request failed: " + request, e);
        }
    }

    private <T> ResponseEntity<T> convertToResponseEntity(MvcResult mvcResult, Class<T> responseClass, TypeReference<T> responseType) {
        HttpHeaders headers = extractHeaders(mvcResult);
        HttpStatus status = extractStatus(mvcResult);

        String body;
        try {
            body = mvcResult.getResponse().getContentAsString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to read response body", e);
        }

        try {
            T responseBody = null;
            if (body != null && !body.isEmpty()) {
                if (responseClass != null) {
                    responseBody = objectMapper.readValue(body, responseClass);
                } else if (responseType != null) {
                    responseBody = objectMapper.readValue(body, responseType);
                }
            }
            return new ResponseEntity<>(responseBody, headers, status);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize response body: " + body, e);
        }
    }

    private HttpHeaders extractHeaders(MvcResult mvcResult) {
        HttpHeaders headers = new HttpHeaders();
        mvcResult.getResponse().getHeaderNames()
                .forEach(headerName -> headers.add(headerName, mvcResult.getResponse().getHeader(headerName)));
        return headers;
    }

    private HttpStatus extractStatus(MvcResult mvcResult) {
        return HttpStatus.valueOf(mvcResult.getResponse().getStatus());
    }
}