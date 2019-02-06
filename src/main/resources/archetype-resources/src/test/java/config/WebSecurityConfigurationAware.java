package ${package}.config;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.web.FilterChainProxy;

import javax.inject.Inject;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public abstract class WebSecurityConfigurationAware extends WebAppConfigurationAware {

    @Inject
    private FilterChainProxy springSecurityFilterChain;

    @BeforeEach
    public void before() {
        this.mockMvc = webAppContextSetup(this.wac).apply(springSecurity())
                //.addFilters(this.springSecurityFilterChain)
                .build();
    }
}
