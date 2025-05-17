package by.redlaw.acocuntsapp.config.jwt;

import by.redlaw.acocuntsapp.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@Configuration
@RequiredArgsConstructor
public class AuthenticationProviderConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoderConfig passwordEncoderConfig;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoderConfig.getPasswordEncoder());

        return authenticationProvider;
    }
}
