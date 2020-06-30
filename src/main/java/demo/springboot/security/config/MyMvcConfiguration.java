package demo.springboot.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@ComponentScan(basePackages = "demo.springboot.security")
@EnableWebMvc
public class MyMvcConfiguration /* extends WebMvcConfigurerAdapter */ {
	
	@Bean
	public ViewResolver getViewResolver() {
		
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setViewClass(JstlView.class);
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
		
	}

	/*
	 * @Override public void configureViewResolvers(ViewResolverRegistry registry) {
	 * 
	 * InternalResourceViewResolver resolver = new InternalResourceViewResolver();
	 * 
	 * resolver.setViewClass(JstlView.class); resolver.setPrefix("/WEB-INF/views/");
	 * resolver.setSuffix(".jsp");
	 * 
	 * registry.viewResolver(resolver);
	 * 
	 * }
	 */
	

}
