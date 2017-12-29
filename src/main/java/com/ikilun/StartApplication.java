package com.ikilun;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ikilun.util.DateUtil;

@SpringBootApplication
public class StartApplication extends WebMvcConfigurerAdapter {
	public static void main(String[] args) {
		try {
			SpringApplication.run(StartApplication.class, args);
		} catch (Exception e) {
		}
	}

	@Bean
	public Converter<String, Date> addNewConvert() {
		return new Converter<String, Date>() {
			@Override
			public Date convert(String source) {
				return DateUtil.parseDate(source);
			}
		};
	}

	@Bean
	public Converter<String, Timestamp> addNewConvert2() {
		return new Converter<String, Timestamp>() {
			@Override
			public Timestamp convert(String source) {
				return DateUtil.parseTimestamp(source);
			}
		};
	}
}
