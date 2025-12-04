package com.human.dalligo.config;


	import org.springframework.context.annotation.Configuration;
	import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
	import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

	@Configuration
	public class RRconfig implements WebMvcConfigurer{

	    @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("/upload/**")
	                .addResourceLocations("file:///C:/upload/"); // ← 실제 폴더 위치
	    }
	}

	
//	@Configuration
//	public class RRconfig implements WebMvcConfigurer {
//	    @Value("${file.upload-dir}")
//	    private String uploadDir;
//
//	    @Override
//	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//	        registry.addResourceHandler("/images/**")
//	                .addResourceLocations("file:" + uploadDir + "/");
//	    }
//	}



