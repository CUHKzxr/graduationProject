package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan("com.example.demo.mapper")
@SpringBootApplication
public class DnsAnalysisApplication {
	public static final Logger logger = LoggerFactory.getLogger(DnsAnalysisApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(DnsAnalysisApplication.class, args);
	}

}
