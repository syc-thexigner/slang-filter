package org.dolgo.filter;

import java.util.regex.Pattern;

/**
 * 텍스트 내에 비속어(slang)를 탐지하는 필터 클래스.
 * SlangDictionary 를 주입받아, 사전에 등록된 term 및 variants 를 기준으로
 * 입력된 문자열에 비속어가 포함되어 있는지를 검사한다.
 */
public class SlangFilter {

    // 비속어 사전
    private final SlangDictionary dictionary;

    /**
     * SlangFilter 생성자.
     *
     * @param dictionary 비속어 사전 객체 (비속어 term 및 variants 포함)
     */
    public SlangFilter(SlangDictionary dictionary) {
        this.dictionary = dictionary;
    }

    /**
     * 입력 텍스트에 비속어가 포함되어 있는지를 검사한다.
     *
     * @param text 검사할 입력 문자열
     * @return true → 비속어 포함됨 / false → 비속어 없음
     */
    public boolean contains(String text) {
        // 사전의 각 entry(term, variants) 에 대해 검사
        return dictionary.getEntries().stream()
                .anyMatch(entry -> {
                    // 1) term(기본 단어) 매칭 검사
                    boolean termMatch = Pattern.compile(
                                    Pattern.quote(entry.getTerm()), // 특수문자 이스케이프 처리
                                    Pattern.CASE_INSENSITIVE       // 대소문자 구분 없이 검사
                            )
                            .matcher(text)
                            .find(); // text 내에 term 존재 여부
                    if (termMatch) return true; // term 일치 시 true 반환

                    // 2) variants(변형 표현) 매칭 검사
                    return entry.getVariants().stream().anyMatch(variant ->
                            Pattern.compile(
                                            Pattern.quote(variant),
                                            Pattern.CASE_INSENSITIVE
                                    )
                                    .matcher(text)
                                    .find() // text 내에 variant 존재 여부
                    );
                });
    }
}