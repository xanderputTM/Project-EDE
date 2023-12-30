package fact.it.apigateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity
                .authorizeExchange(exchange ->
                        exchange
                                // Allow all
                                .pathMatchers(HttpMethod.GET,"/airports")
                                .permitAll()

                                .pathMatchers(HttpMethod.GET,"/airport")
                                .permitAll()

                                .pathMatchers(HttpMethod.GET,"/flights")
                                .permitAll()

                                .pathMatchers(HttpMethod.GET,"/flight")
                                .permitAll()

                                .pathMatchers(HttpMethod.GET,"/gate/flights")
                                .permitAll()

                                .pathMatchers(HttpMethod.GET,"/gates")
                                .permitAll()

                                .pathMatchers(HttpMethod.GET,"/airport/gates")
                                .permitAll()

                                .pathMatchers(HttpMethod.GET,"/airport/gate")
                                .permitAll()

                                .pathMatchers("/actuator/**")
                                .permitAll()

                                // Other endpoints:  Authenticated only
                                .anyExchange()
                                .authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(withDefaults())
                );
        return serverHttpSecurity.build();
    }
}
