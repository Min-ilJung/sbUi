package com.xe.ui.controller.api;

import com.xe.ui.application.service.BasicService;
import com.xe.ui.model.basic.BasicDto;
import com.xe.ui.util.ReturnMessage;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class BasicRestController {

    @Resource
    private BasicService basicService;

    @GetMapping("/basic")
    public ReturnMessage findUser() {
        BasicDto bsDto = basicService.findUserName();
        log.info("response: "+ bsDto.getName());
        return new ReturnMessage(bsDto);
    }
}
