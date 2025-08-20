# slang-filter 🚫🗣️

멀티모듈(Java 17, Gradle) 기반의 **비속어(슬랭) 필터** 라이브러리입니다.
핵심 로직은 `slang-core`, Spring Boot 3.x 자동설정은 `slang-spring-boot-starter` 모듈에 있습니다.

---

## 모듈 구성

```
slang-filter/
├─ slang-core/                    # 핵심 엔진 (프레임워크 독립)
│  ├─ src/main/java/org/dolgo/filter/
│  │  ├─ SlangEntry.java
│  │  ├─ SlangDictionary.java     # resources/slang.json 로딩
│  │  └─ SlangFilter.java         # contains(...) 탐지
│  └─ src/main/resources/
│     └─ slang.json               # 비속어/변형 리스트(배열)
│
└─ slang-spring-boot-starter/     # Spring Boot 3.x 자동 설정
   ├─ src/main/java/org/dolgo/filter/SlangAutoConfiguration.java
   └─ src/main/resources/META-INF/spring/
      └─ org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

---

## 요구사항

* Java 17
* Gradle 8.x
* Spring Boot 3.x (Starter 사용 시)

---

## 설치 & 의존성

### 1) 로컬 저장소에 배포

루트에서 한 번에 배포(예: `publishAllToMavenLocal` 태스크 또는 스크립트 사용):

```bash
sh deploy.sh
```

배포 후 결과물:

```
~/.m2/repository/org/dolgo/slang-core/1.0.0/...
~/.m2/repository/org/dolgo/slang-spring-boot-starter/1.0.0/...
```

### 2) Spring Boot 프로젝트에서 사용

```gradle
repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation "org.springframework.boot:spring-boot-starter-web"
    implementation "org.dolgo:slang-spring-boot-starter:1.0.0"  // core 자동 포함
}
```

---

## 사용법

### A. 순수 자바(프레임워크 독립, slang-core 직접 사용)

```java
SlangDictionary dict = new SlangDictionary(); // resources/slang.json 로드
SlangFilter filter = new SlangFilter(dict);

boolean hasSlang = filter.contains("이 문장 안에 금칙어가 있나요?");
System.out.println(hasSlang ? "금칙어 포함" : "정상");
```

### B. Spring Boot 3.x (Starter 사용, 자동 빈 등록)

Starter를 의존하면 `SlangDictionary`, `SlangFilter` 가 자동 빈으로 등록됩니다.

```java
@RestController
@RequiredArgsConstructor
public class DemoController {
    private final SlangFilter slangFilter;

    @GetMapping("/check")
    public String check(@RequestParam String text) {
        return slangFilter.contains(text) ? "금칙어 포함" : "정상";
    }
}
```

**중요:** `slang-spring-boot-starter` 안에 아래 파일이 있어야 자동설정이 활성화됩니다.

```
src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
# 내용:
org.dolgo.filter.SlangAutoConfiguration
```

---

## 데이터 포맷 (`slang.json`)

현재 구현은 **최상위가 배열**인 JSON을 기대합니다.

```json
[
  {
    "term": "예시용_단어",
    "language": "ko",
    "variants": ["예시", "ye-si", "yeshi"]
  },
  {
    "term": "example",
    "language": "en",
    "variants": ["ex4mple", "ex@mple"]
  }
]
```

* `term`: 기준 단어
* `language`: ISO 639-1 등 간단 코드(선택)
* `variants`: 오타/치환/별칭 등 변형 목록

> 만약 기존에 `{ "slang_glossary": [ ... ] }` 형태를 쓰고 있다면,
> `SlangDictionary`를 그 구조에 맞게 수정하거나 JSON을 배열 최상위로 맞춰주세요.

---

## 빌드 & 테스트

```bash
./gradlew clean build
```

* `slang-core` 테스트는 JUnit 5 사용.
* 콘솔에서 테스트 로그가 보이도록 루트 Gradle에 `showStandardStreams = true` 설정되어 있습니다.
* `slang.json` 로딩 정상 시 예: `slang.json loaded successfully, size=11`

---

## 퍼블리시(로컬) 스크립트 예시

루트 `build.gradle`에 커스텀 태스크:

```gradle
tasks.register("publishAllToMavenLocal") {
    group = "publishing"
    description = "Publish all subprojects to MavenLocal"
    dependsOn(
        ":slang-core:publishToMavenLocal",
        ":slang-spring-boot-starter:publishToMavenLocal"
    )
}
```

쉘 스크립트(`deploy.sh`) 예시:

```bash
#!/bin/bash
set -e

echo "🔨 Cleaning..."
./gradlew clean

echo "📦 Building & Publishing slang-core..."
./gradlew :slang-core:publishToMavenLocal

echo "📦 Building & Publishing slang-spring-boot-starter..."
./gradlew :slang-spring-boot-starter:publishToMavenLocal

echo "✅ Done! Artifacts published to ~/.m2/repository/org/dolgo/"
```

---

## 설계 포인트

* **모듈 분리**

    * `slang-core`: 엔진(로직/데이터 로딩), 프레임워크 의존 없음
    * `starter`: Spring Boot 3.x 자동 구성(@AutoConfiguration)
* **의존성 전파**

    * starter는 `java-library` 플러그인을 사용하고, `api project(":slang-core")` 로 **core를 외부에 노출**
      → starter만 추가해도 `SlangFilter`를 직접 주입/사용 가능
* **Lombok & 소스 배포**

    * `slang-core`에 `io.freefair.lombok` 플러그인 사용 → **delombok된 sources.jar** 생성
      → 소비자 IDE에서 소스 열어도 `getTerm()` 등 실제 메서드가 보임
* **자원 로딩**

    * `SlangDictionary`는 클래스패스에서 `slang.json`을 읽습니다.
      배포 시 `slang.json`이 `slang-core/src/main/resources/`에 존재해야 합니다.

---

## 향후 개선(TODO)

* [ ] **성능 최적화**: 정규식 사전 컴파일/캐시, Aho–Corasick 등 다중 패턴 매칭 도입
* [ ] **Censor 기능**: 매칭 토큰을 `***` 등으로 마스킹(현재 예제는 contains 탐지)
* [ ] **언어별/카테고리별 가중치** 및 **오탐 감소** 로직
* [ ] **외부 사전 동적 로딩**(DB/원격 URL) 및 핫리로드
* [ ] **Micrometer** 지표, **Actuator** 헬스체크

---

## 기여

Issue/PR 환영합니다.
데이터셋 기여 시, 출처와 라이선스를 명확히 해주세요.
