package com.sid.moviebkg.movie;

import org.apache.camel.observation.starter.CamelObservation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.sid.moviebkg.*","com.sid.moviebkg.user.*","com.sid.moviebkg.common.*","com.sid.moviebkg.common.model*"})
@EnableJpaRepositories(basePackages = {"com.sid.moviebkg.*"})
@EntityScan(basePackages = {"com.sid.moviebkg.*"})
@CamelObservation
public class MoviebkgMovieSvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviebkgMovieSvcApplication.class, args);
	}

}
