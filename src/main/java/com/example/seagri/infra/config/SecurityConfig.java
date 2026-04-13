package com.example.seagri.infra.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    TokenFilter tokenFilter;

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService udService() {
        return new UserProfileService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(udService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors(withDefaults()).csrf(csrf -> csrf.disable());
        http.authenticationProvider(authProvider());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(authorize ->
            authorize.requestMatchers(HttpMethod.POST, "/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/register").permitAll()
                .requestMatchers(HttpMethod.GET, "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/schedule/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/vehicle/available/**").permitAll()
                .requestMatchers("/auditoria/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/report/**").hasAnyAuthority("ADMIN", "REPORT_READ")
                .requestMatchers(HttpMethod.GET, "/integrity/**").hasAnyAuthority("ADMIN", "REPORT_READ")
                .requestMatchers(HttpMethod.GET, "/audit/**").hasAnyAuthority("ADMIN", "AUDIT_READ")
                
                .requestMatchers(HttpMethod.GET, "/vehicleType/**").hasAnyAuthority("ADMIN","VEHICLE_TYPE_READ")
                .requestMatchers(HttpMethod.POST, "/vehicleType/**").hasAnyAuthority("ADMIN","VEHICLE_TYPE_WRITE")
                .requestMatchers(HttpMethod.PUT, "/vehicleType/**").hasAnyAuthority("ADMIN","VEHICLE_TYPE_WRITE")
                .requestMatchers(HttpMethod.DELETE, "/vehicleType/**").hasAnyAuthority("ADMIN","VEHICLE_TYPE_DELETE")
                
                .requestMatchers(HttpMethod.GET, "/checklist/**").hasAnyAuthority("ADMIN","CHECKLIST_READ")
                .requestMatchers(HttpMethod.POST, "/checklist/**").hasAnyAuthority("ADMIN","CHECKLIST_WRITE")
                .requestMatchers(HttpMethod.PUT, "/checklist/**").hasAnyAuthority("ADMIN","CHECKLIST_WRITE")
                .requestMatchers(HttpMethod.DELETE, "/checklist/**").hasAnyAuthority("ADMIN","CHECKLIST_DELETE")
                
                .requestMatchers(HttpMethod.GET, "/vehicle/**").hasAnyAuthority("ADMIN","VEHICLE_READ")
                .requestMatchers(HttpMethod.POST, "/vehicle/**").hasAnyAuthority("ADMIN","VEHICLE_WRITE")
                .requestMatchers(HttpMethod.PUT, "/vehicle/**").hasAnyAuthority("ADMIN","VEHICLE_WRITE")
                .requestMatchers(HttpMethod.DELETE, "/vehicle/**").hasAnyAuthority("ADMIN","VEHICLE_DELETE")
                
                .requestMatchers(HttpMethod.GET, "/discontinuedVehicle/**").hasAnyAuthority("ADMIN","VEHICLE_READ")
                .requestMatchers(HttpMethod.POST, "/discontinuedVehicle/**").hasAnyAuthority("ADMIN", "VEHICLE_DELETE")
                .requestMatchers(HttpMethod.PUT, "/discontinuedVehicle/**").hasAnyAuthority("ADMIN", "VEHICLE_DELETE")
                // nesse caso não possui delete pois uma vez descontinuado o veículo nunca poderá ser recuperado.
                
                .requestMatchers(HttpMethod.GET, "/maintenance/**").hasAnyAuthority("ADMIN","MAINTENANCE_READ")
                .requestMatchers(HttpMethod.POST, "/maintenance/**").hasAnyAuthority("ADMIN","MAINTENANCE_WRITE")
                .requestMatchers(HttpMethod.PUT, "/maintenance/**").hasAnyAuthority("ADMIN","MAINTENANCE_WRITE")
                .requestMatchers(HttpMethod.DELETE, "/maintenance/**").hasAnyAuthority("ADMIN","MAINTENANCE_DELETE")
                
                .requestMatchers(HttpMethod.GET, "/supply/**").hasAnyAuthority("ADMIN","SUPPLY_READ")
                .requestMatchers(HttpMethod.POST, "/supply/**").hasAnyAuthority("ADMIN","SUPPLY_WRITE")
                .requestMatchers(HttpMethod.PUT, "/supply/**").hasAnyAuthority("ADMIN","SUPPLY_WRITE")
                .requestMatchers(HttpMethod.DELETE, "/supply/**").hasAnyAuthority("ADMIN","SUPPLY_DELETE")
                
                .requestMatchers(HttpMethod.GET, "/driver/**").hasAnyAuthority("ADMIN","DRIVER_READ")
                .requestMatchers(HttpMethod.POST, "/driver/**").hasAnyAuthority("ADMIN","DRIVER_WRITE")
                .requestMatchers(HttpMethod.PUT, "/driver/**").hasAnyAuthority("ADMIN","DRIVER_WRITE")
                .requestMatchers(HttpMethod.DELETE, "/driver/**").hasAnyAuthority("ADMIN","DRIVER_DELETE")
                
                .requestMatchers(HttpMethod.GET, "/schedule/**").hasAnyAuthority("ADMIN","SCHEDULE_READ")
                .requestMatchers(HttpMethod.POST, "/schedule/**").hasAnyAuthority("ADMIN","SCHEDULE_WRITE")
                .requestMatchers(HttpMethod.PUT, "/schedule/**").hasAnyAuthority("ADMIN","SCHEDULE_WRITE")
                .requestMatchers(HttpMethod.DELETE, "/schedule/**").hasAnyAuthority("ADMIN","SCHEDULE_DELETE")
                
                .requestMatchers(HttpMethod.GET, "/group/**").hasAnyAuthority("ADMIN","GROUP_READ")
                .requestMatchers(HttpMethod.POST, "/group/**").hasAnyAuthority("ADMIN","GROUP_WRITE")
                .requestMatchers(HttpMethod.PUT, "/group/**").hasAnyAuthority("ADMIN","GROUP_WRITE")
                .requestMatchers(HttpMethod.DELETE, "/group/**").hasAnyAuthority("ADMIN","GROUP_DELETE")
                
                // .requestMatchers(HttpMethod.GET, "/user/**").hasAnyAuthority("ADMIN","USER_READ")
                .requestMatchers(HttpMethod.POST, "/user/**").hasAnyAuthority("ADMIN","USER_WRITE")
                .requestMatchers(HttpMethod.PUT, "/user/**").hasAnyAuthority("ADMIN","USER_WRITE")
                .requestMatchers(HttpMethod.DELETE, "/user/**").hasAnyAuthority("ADMIN","USER_DELETE")
                
                .requestMatchers(HttpMethod.GET, "/brand/**").hasAnyAuthority("ADMIN","BRAND_READ")
                .requestMatchers(HttpMethod.POST, "/brand/**").hasAnyAuthority("ADMIN","BRAND_WRITE")
                .requestMatchers(HttpMethod.PUT, "/brand/**").hasAnyAuthority("ADMIN","BRAND_WRITE")
                .requestMatchers(HttpMethod.DELETE, "/brand/**").hasAnyAuthority("ADMIN","BRAND_DELETE")
                
                .requestMatchers(HttpMethod.GET, "/carModel/**").hasAnyAuthority("ADMIN","CAR_MODEL_READ")
                .requestMatchers(HttpMethod.POST, "/carModel/**").hasAnyAuthority("ADMIN","CAR_MODEL_WRITE")
                .requestMatchers(HttpMethod.PUT, "/carModel/**").hasAnyAuthority("ADMIN","CAR_MODEL_WRITE")
                .requestMatchers(HttpMethod.DELETE, "/carModel/**").hasAnyAuthority("ADMIN","CAR_MODEL_DELETE")
                
                .requestMatchers(HttpMethod.POST,"/fuel/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/fuel/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/fuel/**").hasAuthority("ADMIN")

                // Motor de Glosa (AP 04)
                .requestMatchers(HttpMethod.POST, "/glosa/**").hasAnyAuthority("ADMIN", "GLOSA_WRITE")
                .requestMatchers(HttpMethod.GET,  "/glosa/**").hasAnyAuthority("ADMIN", "GLOSA_READ")

                .anyRequest().authenticated()
        );
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }
    
}
