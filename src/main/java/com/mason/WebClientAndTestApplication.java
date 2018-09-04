package com.mason;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class WebClientAndTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebClientAndTestApplication.class, args);
	}

	@Service
	class MasonWebClientService {

	    @Value("${test.host.naver}")
        String naver;

        @Value("${test.host.daum}")
        String daum;

        @Value("${test.host.google}")
        String google;

		WebClient daumWebClient;

        WebClient naverWebClient;

        WebClient googleWebClient;

		@PostConstruct
		public void init() {
            daumWebClient = WebClient
                    .builder()
                    .baseUrl(daum)
                    .build();

            naverWebClient = WebClient
                    .builder()
                    .baseUrl(naver)
                    .build();

            googleWebClient = WebClient
                    .builder()
                        .baseUrl(google)
                    .build();
		}

		public Mono<String> requestToDaum() {
			return daumWebClient
					.get()
					.retrieve()
					.bodyToMono(String.class);
		}

        public Mono<String> requestToNaver() {
            return naverWebClient
                    .get()
                    .retrieve()
                    .bodyToMono(String.class);
        }

        public Mono<String> requestToGoogle() {
            return googleWebClient
                    .get()
                    .retrieve()
                    .bodyToMono(String.class);
        }
	}

	@Service
    class BusinessLoginService {

	    @Autowired
        private MasonWebClientService masonWebClientService;

	    public Mono<Integer> getHtmlSizeDaum() {
            return masonWebClientService
                    .requestToDaum()
                    .map(html -> html.length());

        }

        public Mono<Integer> getHtmlSizeNaver() {
            return masonWebClientService
                    .requestToNaver()
                    .map(html -> html.length());

        }

        public Mono<Integer> getHtmlSizeGoogle() {
            return masonWebClientService
                    .requestToGoogle()
                    .map(html -> html.length());

        }
    }

	@RestController
    class TestController {

	    @Autowired
        private BusinessLoginService businessLoginService;

	    @GetMapping("/daum")
        public Mono<Integer> daum() {
            return businessLoginService.getHtmlSizeDaum();
        }

        @GetMapping("/naver")
        public Mono<Integer> naver() {
            return businessLoginService.getHtmlSizeNaver();
        }

        @GetMapping("/google")
        public Mono<Integer> google() {
            return businessLoginService.getHtmlSizeGoogle();
        }
    }
}
