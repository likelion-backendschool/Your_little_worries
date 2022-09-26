# 아이디어 15팀

---

### 팀명 : 자질구레
### 팀원

- 이우엽(팀장)
- 김준기
- 도석영
- 이주원
- 현지혜

---

# branch

### 계속 남아 있는 브랜치

- `master` 메인
- `develop` 개발용

### 만들고 바로 지워지는 브랜치

- `feature` : 로컬에서 만들고 PR 후 머지하고 바로 지워진다
    - `feature/user` (예시)
    - `feature/article` (예시)
    - `feature/comment` (예시)

# Commit Message

### 형식

> 타입 : 제목
본문
>

### 타입

- `feat` : 새로운 기능의 추가
- `fix` : 버그 수정
- `docs` : 문서 수정
- `style` : 스타일 관련 기능(코드 포맷팅, 세미콜론 누락, 코드 자체의 변경이 없는 경우)
- `refactor` : 코드 리펙토링
- `test` : 테스트 코트, 리펙토링 테스트 코드 추가
- `chore` : 빌드 업무 수정, 패키지 매니저 수정(ex .gitignore 수정 같은 경우)
- `del` : 불필요한 코드 삭제, 파일 삭제

### 제목

- 한글 입력 가능하다.
- 50 글자로 제한한다.
- 마침표를 넣지 않는다.
- 명령문으로 사용하며 과거형을 사용하지 않는다.

### 본문

- 제목과 본문을 빈 행으로 구분한다.
- 어떻게 보다는 무엇과 왜를 설명한다.

### Tips

- 커밋 메시지의 경우 따옴표를 닫지 않고 엔터를 침으로써 여러줄 입력 가능하다.

    ```bash
    git commit -m "commit message line 1
    
    commit message line2
    commit message line3
    commit message last line"
    ```


# 작업 순서

`git checkout develop`

`git pull origin develop`

`git checkout -b 브랜치명`

`(코드 작업)`

`git add .`

`git commit -m “커밋 메시지”`

`git push origin 브랜치명`

`(GitHub에서 Pull Request)`

`(생성한 branch 삭제)`