package ru.drudenko.tools.tibco.configuration;

import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class H2WebConsoleConfiguration {

    @Bean
    ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addInitParameter("webAllowOthers", "");
        registrationBean.addUrlMappings("/webapi-1.0/tibco-mock/h2-console/*");
        return registrationBean;
    }
}
