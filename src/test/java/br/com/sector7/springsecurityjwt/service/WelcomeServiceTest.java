package br.com.sector7.springsecurityjwt.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest
public class WelcomeServiceTest {

    @Autowired
    private WelcomeService welcomeService;

    @Test
    public void sayHelloDev() {
        assertEquals("helloDev", this.welcomeService.getWelcomeString());
    }
}