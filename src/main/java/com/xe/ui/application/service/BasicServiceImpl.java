package com.xe.ui.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xe.ui.model.basic.BasicDto;
import com.xe.ui.model.property.XeApiPropertyBean;
import com.xe.ui.util.ReturnMessage;
import com.xe.ui.util.XeHttpRequest;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BasicServiceImpl implements BasicService {

    @Resource
    private XeApiPropertyBean apiProperty;

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public BasicDto findUserName() {
        ReturnMessage returnMessage = XeHttpRequest.Builder.build(apiProperty.getUrlWithProtocol())
            .get("/api/basic")
            .executeAndGetBodyAs(ReturnMessage.class);
        log.info("API CALL: "+apiProperty.getUrlWithProtocol());
        return objectMapper.convertValue(returnMessage.getValue(), BasicDto.class);
    }
}
