package org.dolgo.filter;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * {@link SlangAutoConfiguration} 은 Spring Boot 애플리케이션 구동 시
 * {@link SlangDictionary} 와 {@link SlangFilter} 를 자동으로 빈으로 등록하는
 * Spring Boot AutoConfiguration 클래스입니다.
 *
 * <p>Spring Boot Starter 를 의존성에 추가하기만 하면
 * 별도 설정 없이도 {@link SlangFilter} 를 애플리케이션에서 바로 사용할 수 있습니다.</p>
 */
@AutoConfiguration
public class SlangAutoConfiguration {

    /**
     * 비속어 사전을 생성하여 Spring 컨텍스트에 등록합니다.
     *
     * @return {@link SlangDictionary} 기본 구현체
     */
    @Bean
    public SlangDictionary slangDictionary() {
        return new SlangDictionary();
    }

    /**
     * {@link SlangDictionary} 를 주입받아 {@link SlangFilter} 빈을 생성합니다.
     * <p>이 빈을 통해 애플리케이션에서 비속어 필터링 기능을 사용할 수 있습니다.</p>
     *
     * @param dictionary {@link SlangDictionary} 빈
     * @return {@link SlangFilter} 인스턴스
     */
    @Bean
    public SlangFilter slangFilter(SlangDictionary dictionary) {
        return new SlangFilter(dictionary);
    }
}