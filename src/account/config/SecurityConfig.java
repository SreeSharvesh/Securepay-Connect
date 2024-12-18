package account.config;


//import account.exceptions.RestAuthenticationEntryPoint;

//import account.exceptions.CustomAccessDeniedHandler;
//import account.exceptions.CustomAuthenticationFailureHandler;

import account.entities.LogEntry;
import account.service.LogServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.sql.DataSource;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private AuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private LogServiceImpl logService;

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .csrf(csrf -> {
                    csrf.disable();
                    csrf.ignoringRequestMatchers("/h2-console/**");
                })
                .headers(headers -> headers.frameOptions().disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/h2-console/**").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/auth/signup", "/actuator/shutdown").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/auth/changepass").hasAnyRole("USER", "ACCOUNTANT", "ADMINISTRATOR");
                    auth.requestMatchers(HttpMethod.GET, "/api/empl/payment").hasAnyRole("USER", "ACCOUNTANT");
                    auth.requestMatchers(HttpMethod.POST, "/api/acct/payments").hasRole("ACCOUNTANT");
                    auth.requestMatchers(HttpMethod.PUT, "/api/acct/payments").hasRole("ACCOUNTANT");
                    auth.requestMatchers(HttpMethod.GET, "/api/admin/user/").hasAnyRole("ADMINISTRATOR");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/admin/user/**").hasAnyRole("ADMINISTRATOR");
                    auth.requestMatchers(HttpMethod.PUT, "/api/admin/user/role/**").hasAnyRole("ADMINISTRATOR");
                    auth.requestMatchers(HttpMethod.PUT, "/api/admin/user/role").hasAnyRole("ADMINISTRATOR");
                    auth.requestMatchers(HttpMethod.PUT, "/api/admin/user/access").hasAnyRole("ADMINISTRATOR");
                    auth.requestMatchers(HttpMethod.GET, "/api/security/events/**").hasRole("AUDITOR");
                    try {
                        auth.anyRequest().permitAll()
                                .and()
                                .exceptionHandling().accessDeniedHandler(getAccessDeniedHandler());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        return http.build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(13);
    }

    @Bean
    public AccessDeniedHandler getAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);


            //Removing 'uri=' from the path
            String path = request.getRequestURI();
            path = path.replace("uri=", "");

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseBody = new LinkedHashMap<>();

            responseBody.put("timestamp", Calendar.getInstance().getTime());
            responseBody.put("status", HttpStatus.FORBIDDEN.value());
            responseBody.put("error", "Forbidden");
            responseBody.put("message", "Access Denied!");
            responseBody.put("path", path);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            this.logService.save(new LogEntry(username, "ACCESS_DENIED", path, path));

            response.getOutputStream()
                    .println(objectMapper.writeValueAsString(responseBody));
        };
    }
}





