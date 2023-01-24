## spring-template
개인용 spring 템플렛

**branch develop** : refresh 토큰을 로컬 db에 적용한 버젼

**branch redis** : refresh 토큰을 redis에 적용한 버젼

### **구현 상황**
|기능|설명|
|------|---|
|유저 관련 api| 회원가입, 로그인, 로그아웃 구현|
|spring security|accessToken, refreshToken filter 구현 ( refreshToken은 로컬db or redis에 저장 )| 
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

### **기능 상세 설명**
<details>
<summary>로그인</summary>
<div markdown="1">       
<div>- accessToken, refreshToken 모두 클라이언트에게 넘겨줌</div>

<div>- refreshToken 은 db 에 따로 저장</div>

</div>
</details>

<details>
<summary>로그아웃</summary>
<div markdown="1">       
  <div>- db 의 refreshToken 를 삭제해줌</div>
</div>
</details>

<details>
<summary>인증 filter</summary>
<div markdown="1">       
  <div>- accessToken 이 있는 유저만 authorized 되는 형태 </div>
  <div>- 그러나 accessToken 은 있는데 refreshToken 이 db에 저장되지 않은 경우 로그아웃한 유저로 간주 </div>
</div>
</details>

<details>
<summary>토큰 재발급</summary>
<div markdown="1">       
<div>- 클라이언트 측에서 accessToken 이 만료됨을 알고 재요청하는 상황으로 가정</div>

<div>- 클라이언트 측에서 refreshToken을 header 에 넣어 보내주는데 이때의 토큰과 db의 토큰이 일치하는지 확인하여 일치한다면 accessToken, refreshToken 모두 재발급</div>

<div>- 새로 생성된 refreshToken으로 db도 갱신</div>

</div>
</details>
