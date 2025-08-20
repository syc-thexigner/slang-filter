package org.dolgo.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * 비속어 사전(Dictionary) 클래스.
 * slang.json 리소스 파일을 읽어서 {@link SlangEntry} 리스트로 로드한다.
 * SlangFilter 에서 이 사전을 사용하여 입력 텍스트 내의 비속어를 탐지한다.
 */
public class SlangDictionary {

    /** slang.json 에서 로드된 비속어 엔트리 리스트 */
    private final List<SlangEntry> entries;

    /**
     * 기본 생성자.
     * <p>
     * 클래스패스 리소스의 slang.json 파일을 읽어서 entries 를 초기화한다.
     * 파일이 존재하지 않으면 빈 리스트로 설정된다.
     * </p>
     */
    public SlangDictionary() {
        ObjectMapper mapper = new ObjectMapper(); // JSON 직렬화/역직렬화 도구
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("slang.json")) {

            // slang.json 이 classpath 에 없을 경우 → 빈 리스트로 처리
            if (is == null) {
                System.err.println("slang.json not found in resources!");
                entries = Collections.emptyList();
                return;
            }

            // slang.json 을 List<SlangEntry> 로 역직렬화
            entries = mapper.readValue(is, new TypeReference<List<SlangEntry>>() {});
            System.out.println("slang.json loaded successfully, size=" + entries.size());

        } catch (Exception e) {
            // 로드 중 오류 발생 시 런타임 예외 던짐 → 앱 실행 중 조기 발견 가능
            throw new RuntimeException("Failed to load slang.json", e);
        }
    }

    public List<SlangEntry> getEntries() {
        return entries;
    }
}