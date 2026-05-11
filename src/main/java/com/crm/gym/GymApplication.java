package com.crm.gym;

import com.crm.gym.config.AppConfig;
import com.crm.gym.entity.Trainer;
import com.crm.gym.entity.TrainingType;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class GymApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context =
				new AnnotationConfigApplicationContext(
						AppConfig.class);

		context.registerShutdownHook();

		GymFacade facade =
				context.getBean(GymFacade.class);

		Trainer trainer = new Trainer();

		trainer.setFirstName("John");
		trainer.setLastName("Smith");
		trainer.setSpecialization(TrainingType.CARDIO);
		trainer.setActive(true);

		trainer = facade.createTrainer(trainer);

		System.out.println(trainer);

		System.out.println(facade.selectAllTrainers());
	}
}
