package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DemoApplication extends SpringBootServletInitializer {

	/*
	Slownik polsko-angielski/angielsko-polski
	Przechowywanie danych w bazie danych + pobieranie z zewnetrznego api: https://dictionaryapi.dev/
	Dane w .xml: https://github.com/freedict/fd-dictionaries/tree/master/eng-pol/letters
	Dodawanie wlasnych slow, nowych tlumaczen do istniejacych
	Quizy, generacja, wypelnianie, udostepnianie w formie linku, moze logowanie (OAuth, Spring Security)
	Wyszukiwane slowa, wyniki quizow - zapisywane do bazy danych
	Zaleznie od tego co wyszukujemy aplikacja moze generowac quiz
	Testy do funkcjonalnosci.


	// controller/words
	// controller/quizes
	// repository/words
	// repository/quizes
	// service/...

	// words/controller
	// words/repository
	// words/service

	 */

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
