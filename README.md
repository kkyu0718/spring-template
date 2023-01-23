## spring-template
개인용 spring 템플렛
### **구현 상황**
|기능|설명|
|------|---|
|유저 관련 api| 회원가입, 로그인, 로그아웃 구현|
|spring security|accessToken, refreshToken filter 구현 ( refreshToken은 로컬db에 저장 )| 
|spring security|authority 에 따른 접근 권한 구현|
|api response|response 형식 통일|
|환경 분리|application.yml 통한 환경 분리|


### **API 명세서**
|기능|분류|메소드|path|
|------|---|---|-----|
|회원 가입|auth|POST|api/auth/signup|
|로그인|auth|POST|api/auth/login|
|로그아웃|auth|POST|api/auth/logout|
|토큰 재발급|auth|POST|api/auth/reissue|
|관리자 권한 보드|board|GET|api/board/admin|
|일반 권한 보드|board|GET|api/board/user|


