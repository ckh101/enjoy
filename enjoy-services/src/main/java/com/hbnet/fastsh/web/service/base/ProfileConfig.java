package com.hbnet.fastsh.web.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class ProfileConfig {
    public static final String DEV_PROFILE = "dev";
    public static final String TEST_PROFILE = "test";
    public static final String PRO_PROFILE = "online";

    @Autowired
    private ApplicationContext context;

    public String getActiveProfile() {
        return context.getEnvironment().getActiveProfiles()[0];
    }

}
