package com.sdnc.common.conf;

import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sdnc.common.json.SnackHttpMessageConverter;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.AllArgsConstructor;

/**
 *
 * 全局配置信息
 *
 */
@Configuration
@AllArgsConstructor
public class GlobalConfigurer implements WebMvcConfigurer {

	private final IdempotentInterceptor idempotentInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(idempotentInterceptor)
				.addPathPatterns("/cmd/**");
	}

	@Bean
	public Validator validator() {
		ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
				.configure().failFast(true)
				.buildValidatorFactory();
		Validator validator = validatorFactory.getValidator();

		return validator;
	}

	@Bean
	public HttpMessageConverters snackHttpMessageConverters() {
		return new HttpMessageConverters(new SnackHttpMessageConverter<>());
	}

}
