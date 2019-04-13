package br.com.sector7.springsecurityjwt.api;

import br.com.sector7.springsecurityjwt.service.WelcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeAPI {

    @Autowired
    private WelcomeService welcomeService;

    @GetMapping("/sayHello")
    public void sayHello() {
        System.out.println(this.welcomeService.getWelcomeString());
    }
}
