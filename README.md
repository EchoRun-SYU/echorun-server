# echorun-server
러닝과 플로깅을 결합한 AI 친환경 서비스 'EchoRun' 백엔드 레포지토리

# Git Convention

## 🔵 Commit Convention

* `Feat` : 새로운 기능 추가
* `Fix` : 버그 및 오류 수정
* `Refactor` : 기능 변화 없이 코드 구조 개선
* `Docs` : README, WIKI 등 문서 수정
* `Chore` : 설정 파일 수정, 패키지 설치 등 기타 작업

### Commit Message

```bash
git commit -m "#이슈번호 태그: 작업 내용"
```

### Example

```bash
git commit -m "#12 Feat: 로그인 API 구현"
git commit -m "#24 Fix: JWT 토큰 검증 오류 수정"
```

---

# Branch Convention

## 🔵 Branch Type

* `main` : 최종 배포 브랜치
* `develop` : 개발 통합 브랜치
* `feat` : 기능 개발 브랜치
* `fix` : 버그 수정 브랜치
* `refactor` : 리팩토링 브랜치

### Branch Naming

```bash
feat/#이슈번호-기능명
fix/#이슈번호-수정내용
```

### Example

```bash
feat/#15-login-api
fix/#21-token-error
```

---

# Branch Strategy

## 🔵 Git Flow Strategy

* `main` 브랜치는 배포 시에만 사용합니다.
* 모든 개발은 `develop` 브랜치에서 통합합니다.
* 기능 개발은 각자의 `feat` 브랜치에서 진행합니다.
* 작업 완료 후 Pull Request를 통해 `develop` 브랜치에 merge 합니다.
* merge 완료 후 브랜치는 삭제합니다.

---

# Issue Convention

## 🔵 Issue Type

* `[Feat]` : 기능 추가
* `[Fix]` : 버그 수정
* `[Refactor]` : 리팩토링
* `[Docs]` : 문서 작업
* `[Chore]` : 기타 작업

### Issue Title Example

```bash
[Feat] 로그인 API 구현
[Fix] 회원가입 예외 처리 수정
```
