package br.com.sector7.springsecurityjwt.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WelcomeService {

    @Value("${welcomeString}")
    private String welcomeString;


    public String getWelcomeString() {
        return welcomeString;
    }
}
