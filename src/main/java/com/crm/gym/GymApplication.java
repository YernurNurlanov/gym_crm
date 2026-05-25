package com.crm.gym;

import com.crm.gym.config.AppConfig;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class GymApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context =
				new AnnotationConfigApplicationContext(
						AppConfig.class);

		GymFacade facade =
				context.getBean(GymFacade.class);
	}
}
