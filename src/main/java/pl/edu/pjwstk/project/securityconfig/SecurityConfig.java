package pl.edu.pjwstk.project.securityconfig;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * SecurityConfig class is responsible for securing endpoints defined in application.
 *
 * @author Zuzanna Borkowska
 */
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final UserService userService;

    /**
     * Method responsible for indicating which endpoints needs authenticated access - custom filter.
     * @param http variable allows configuring web based security for specific http requests.
     * @return SecurityFilterChain which defines a filter chain which is capable of being matched against an HttpServletRequest. In order to decide whether it applies to that request.
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception{
        http
                .cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/**/auth",
                        "/**/getGunData/**",
                        "/**/getWholeGunData/**",
                        "/**/find",
                        "/**/createUser/**",
                        "/**/getFirstAidFile/**",
                        "/**/getLocations/**",
                        "/**/getSurvivalData/**"
                ).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();

    }

    /**
     * Method which provides authentication.
     * @return AuthenticationProvider with set password encoder and user details service.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = 
                new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Method responsible for defining password encoder.
     * @return PasswordEncoder - defined password encoder in this case BCryptBCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Method define user details service.
     * @return UserDetailsService which loads user-specific data.
     */
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userService.findUserByUsername(username);

            }
        };
    }
}
