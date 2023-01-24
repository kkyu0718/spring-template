package com.kyuwon.spring;

import com.kyuwon.spring.domain.user.domain.RefreshToken;
import com.kyuwon.spring.domain.user.repository.TokenRedisRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

	@Autowired
	private TokenRedisRepository tokenRedisRepository;

	@Test
	void contextLoads() {
		tokenRedisRepository.save(RefreshToken.builder()
						.token("token")
						.userId(1L)
						.build());

		tokenRedisRepository.save(RefreshToken.builder()
				.token("token2")
				.userId(2L)
				.build());


		System.out.println(tokenRedisRepository.findAll());
		System.out.println(tokenRedisRepository.findById(2L).get().getUserId());
		System.out.println(tokenRedisRepository.findByToken("token").get().getToken());


	}

}
