package com.mason;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebClientAndTestApplicationTests {

	final Integer EXPECTED_DOMESTIC_COUNT = 150000;

	@Autowired
	private WebClientAndTestApplication.BusinessLoginService businessLoginService;

	@Test
	public void daumCountTest() {
		Mono<Integer> daumCount = businessLoginService.getHtmlSizeDaum();
		StepVerifier
				.create(daumCount)
				.expectNextMatches(count -> count > EXPECTED_DOMESTIC_COUNT)
				.verifyComplete();
	}

	@Test
	public void naverCountTest() {
		Mono<Integer> daumCount = businessLoginService.getHtmlSizeNaver();
		StepVerifier
				.create(daumCount)
				.expectNextMatches(count -> count > EXPECTED_DOMESTIC_COUNT)
				.verifyComplete();
	}

	// This test will fail
	@Test
	public void googleCountTest() {
		Mono<Integer> daumCount = businessLoginService.getHtmlSizeGoogle();
		StepVerifier
				.create(daumCount)
				.expectNextMatches(count -> count > EXPECTED_DOMESTIC_COUNT)
				.verifyComplete();
	}

}
