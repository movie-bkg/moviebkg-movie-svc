package com.sid.moviebkg.movie.config;


import com.sid.moviebkg.movie.filter.JwtAuthenticationFilter;
import com.sid.moviebkg.movie.filter.RequestResponseLoggingFilter;
import com.sid.moviebkg.movie.filter.TraceResponseFilter;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.sid.moviebkg.movie.util.MovieCmnConstants.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {

    @Value("${token.disabled:#{null}}")
    private Boolean tokenDisabled;

    private static final String[] WHITE_LIST_URL = {
            SWAGGER_API_DOCS_V2,
            SWAGGER_API_DOCS_V3,
            SWAGGER_API_DOCS_V3_ALL,
            SWAGGER_RESOURCES,
            SWAGGER_RESOURCES_ALL,
            SWAGGER_CONFIGURATION_UI,
            SWAGGER_CONFIGURATION_SECURITY,
            SWAGGER_UI,
            WEBJARS,
            SWAGGER_UI_HTML
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter,
                                           RequestResponseLoggingFilter requestResponseLoggingFilter, TraceResponseFilter traceResponseFilter) throws Exception {
        HttpSecurity httpSecurity = http.csrf(AbstractHttpConfigurer::disable).cors(Customizer.withDefaults());
        if (tokenDisabled != null && BooleanUtils.isTrue(tokenDisabled)) {
            httpSecurity.authorizeHttpRequests(authManager -> authManager.anyRequest().permitAll())
                    .addFilterAfter(requestResponseLoggingFilter, UsernamePasswordAuthenticationFilter.class)
                    .addFilterAfter(traceResponseFilter, RequestResponseLoggingFilter.class);
        } else {
            httpSecurity.authorizeHttpRequests(authManager -> authManager
                    .requestMatchers(WHITE_LIST_URL)
                    .permitAll()
                    .anyRequest()
                    .authenticated())
                    .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(requestResponseLoggingFilter, JwtAuthenticationFilter.class)
                    .addFilterAfter(traceResponseFilter, JwtAuthenticationFilter.class);
        }

        return httpSecurity.build();
    }
}
