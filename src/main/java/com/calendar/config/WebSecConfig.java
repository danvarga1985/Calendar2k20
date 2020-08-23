package com.calendar.config;

import com.calendar.config.error.CustomAccessDeniedHandler;
import com.calendar.config.security.MySavedRequestAwareAuthenticationSuccessHandler;
import com.calendar.config.security.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		prePostEnabled = true,
		securedEnabled = true,
		jsr250Enabled = true
)

public class WebSecConfig extends WebSecurityConfigurerAdapter {

	@Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler;

    private SimpleUrlAuthenticationFailureHandler myFailureHandler = new SimpleUrlAuthenticationFailureHandler();
	
	protected void configure(HttpSecurity http) throws Exception {
	    http
	    	.csrf().disable()
	    	.cors()
	    	.and()
	    	.authorizeRequests()
	    		.antMatchers(
                    "/login", 
                    "/registration"
                    )
	    		.permitAll()
	    		.and()
	    	.formLogin()
            	.successHandler(mySuccessHandler)
            	.failureHandler(myFailureHandler)
	    		.loginPage("/login")
	    		.permitAll()
	    		.and()
            .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint)
	    	.and()
	    	.logout()
	    		.permitAll()
	    		.invalidateHttpSession(true)
	    		.deleteCookies("JSESSIONID")
//	    		.logoutSuccessUrl("https://plan-my-day-mages-of-code.firebaseapp.com/login-registration")
	    		;
	}
	
	@Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "https://localhost:4200",
				"https://plan-my-day-dev.web.app/",
				"http://plan-my-day-dev.web.app/",
				"https://plan-my-day-dev.web.app/login-registration",
				"http://plan-my-day-dev.web.app/login-registration",
				"https://plan-my-day-dev.firebaseapp.com",
				"http://plan-my-day-dev.firebaseapp.com",
				"https://plan-my-day-dev.firebaseapp.com/login-registration",
				"http://plan-my-day-dev.firebaseapp.com/login-registration",
        		"https://plan-my-day-mages-of-code.firebaseapp.com",
        		"http://plan-my-day-mages-of-code.firebaseapp.com",
        		"https://plan-my-day-mages-of-code.firebaseapp.com/login-registration",
        		"http://plan-my-day-mages-of-code.firebaseapp.com/login-registration"));
        configuration.setAllowedMethods(Arrays.asList("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
