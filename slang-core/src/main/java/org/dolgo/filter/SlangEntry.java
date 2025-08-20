package org.dolgo.filter;

import java.util.List;

/**
 * slang.json 파일에서 하나의 비속어 엔트리를 표현하는 DTO 클래스.
 * JSON 예시:
 * {
 *   "term": "씨발",
 *   "language": "ko",
 *   "variants": ["씨바", "시발", "ㅅㅂ"]
 * }
 * term      → 기준이 되는 비속어 단어
 * language  → 비속어의 언어 코드 (선택적)
 * variants  → 다양한 변형/약어/표기법
 */
public class SlangEntry {

    /** 기준이 되는 비속어 단어 */
    private String term;

    /** 해당 비속어의 언어 코드 (예: "ko", "en", "es") */
    private String language;

    /** 비속어의 다양한 변형 표현들 */
    private List<String> variants;

    /**
     * @return 기준 비속어 단어
     */
    public String getTerm() {
        return term;
    }

    /**
     * @return 언어 코드
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @return 변형 비속어 리스트
     */
    public List<String> getVariants() {
        return variants;
    }

    /** 기본 생성자 (JSON 파싱 라이브러리에서 사용) */
    public SlangEntry() {
    }

    /**
     * 모든 필드를 초기화하는 생성자.
     *
     * @param term 기준 비속어
     * @param language 언어 코드
     * @param variants 변형 리스트
     */
    public SlangEntry(String term, String language, List<String> variants) {
        this.term = term;
        this.language = language;
        this.variants = variants;
    }
}
