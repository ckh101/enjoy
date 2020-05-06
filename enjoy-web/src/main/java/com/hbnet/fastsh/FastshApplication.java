package com.hbnet.fastsh;

import com.hbnet.fastsh.web.service.impl.AgentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@Slf4j
public class FastshApplication extends SpringBootServletInitializer implements ApplicationRunner {



    @Autowired
    AgentService agentService;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		builder.sources(this.getClass());
		return super.configure(builder);
	}

	public static void main(String[] args) {
		SpringApplication.run(FastshApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments applicationArguments){
		log.info("启动成功:" + applicationArguments.toString());
	}
}

