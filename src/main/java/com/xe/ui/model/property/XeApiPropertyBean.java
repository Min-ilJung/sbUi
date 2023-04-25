package com.xe.ui.model.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("xe.api")
@Getter
@Setter
public class XeApiPropertyBean {

    protected String url;

    public String getUrlWithProtocol() {
        return url.contains("localhost")
            ? "http://" + url
            : "https://" + url;
    }
}
