# SEARCH-BACKEND(검색 서비스)

### 프로젝트 실행 전 필수 세팅
1. 환경변수에 다음 세 가지 값을 추가해주어야 합니다. Kakao/Naver API Server 와 통신하기 위한 검증키입니다.
````
  KAKAO_API_KEY
  NAVER_CLIENT_ID
  NAVER_CLIENT_SECRET
````

### 외부 라이브러리 설명
1. Feign Client - 넷플릭스에서 개발한 Http Client로 Kakao API Server와 통신하는 용도로 사용하였습니다.
2. ShedLock - 멀티 서버 환경에서 2개 이상의 서버의 중복 스케줄 수행을 방지하기 위해 Lock을 걸기 위한 용도로 사용하였습니다.
3. Swagger - OAS(Open Api Specification)를 제공하기 위해 사용하였습니다.(http://localhost:8080/swagger-ui/index.html)


### jar파일 다운로드 링크
- https://drive.google.com/file/d/14BBXAb4mlp0NemwTkOY1RZ84qG_2LXao/view?usp=share_link
