# cache
캐시는 크게 private cache와 shared cache로 나눌 수 있습니다.

## 1.1) private cache
- 사용자의 개인화된 응답을 저장하는 캐시
- Server 응답에 Authorization 헤더가 있으면 private cache에 저장이 되지 않는다.

## 1.2) shared cache
- 한 명 이상의 사용자가 재사용할 수 있도록 응답을 저장하는 캐시
1) Proxy
   1) 중개자 역할: 클라이언트와 서버 사이에서 http 메시지를 전달 
   2) 개발자가 직접 제어할 수 없다. → http header를 통해서만 알려줄 수 있다.
2) Managed
   1) Reverse Proxy 또는 CDN을 말한다.
   2) 개발자가 직접 제어할 수 있다.

## 2) Cache 유효기간
### Cache Control
Cache-Control은 HTTP에서 요청과 응답 내의 캐시 매커니즘을 정하기 위해 사용하는 헤더이다.
- max-age: 리소스가 유효하다고 판단되는 최대 시간 (단위: 초)
  - fresh : 유효한 상태
  - stale : 만료 상태

유효기간이 지난 캐시를 바로 지우지 않고, 해당 캐시를 사용해도 되는지 **유효성 검증**을 한다.

## 3) 유효성 검증
### 조건부 요청
1) if-Modified-Since
   1) 날짜를 기준으로 검증
   2) 흐름
      1) 서버에서 Last-Modified에 마지막으로 수정한 시간, Cache-Control에 max-age를 담아 클라이언트에게 전송
      2) 클라이언트 측에서 만료가 되면, If-Modified-Since 헤더에 Last-Modified 값을 담아 서버에 전송
         - 만약 서버 자원에 변경이 없었다면 304 Not Modified를 응답 (304는 응답 값이 없다.)
         - 서버 자원에 변경이 있었다면 a번으로 돌아간다.
   3) 초 단위로 캐시 검증하기 때문에 더 작은 단위에 대해서는 불가한 단점이 존재
2) E-Tag/If-None-Match
   1) ETag에 해시값, Cache-Control에 max-age를 담아 클라이언트에게 전송
   2) 클라이언트 측에서 max-age가 만료되면 If-None-Match에 ETag 해시값을 넣어 서버에 전달
   3) 만약 리소스가 변경되지 않았다면 서버에서 304 Not Modified를 응답

## 4) 강제 유효성 검증
- HTTP1.1이전에는 서버측에서 Cache-Control에서 max-age=0, must-revalidate를 사용해 강제 재검증을 진행
- 최근에는 클라이언트 측에서 Cache-Control: **no-cache**를 담아서 보내면 **항상 서버에서 재검증**을 한다.
- no-store
  - **캐시에 아예 저장하지 말라**는 것을 나타낸다.
  - 브라우저가 가진 이점을 활용하지 못할 수 있다.

## 5) 일반적인 캐싱 패턴
- Cache-Control: no-cache 또는 Cache-Control: no-cache, private(개인화된 정보)를 사용
- Cache-Control 헤더를 쓰지 않으면 휴리스틱 캐싱이 발생
  - 휴리스틱 캐싱: 브라우저나 프록시 서버에서 임의로 캐싱 → 의도하지 않은 캐싱(응답이 제대로 적용이 안될 수 있다.)

### 캐시 무효화(Cache Busting)
- 보통 캐시는 잘 변하지 않는 css, js과 같은 정적 리소스 파일을 캐싱한다.
  - 따라서 최대한 오래 캐시하면서, 새로 배포가 되었을 때만 반영이 되게 하기 위해서 **URL**을 다르게 가져가는 전략을 사용한다. 
  - 즉, 새 버전이 배포되었을 때마다 다른 URL를 사용하는 것이다.
- 캐시는 URL을 기준으로 구분하기 때문에 캐시 무효화 전략이 필요하다.
  1) bundle.js, build.css같은 경우 파일에 버전을 부여(bundle.v.123.js, bundle.js?v=123) 
  2) 파일에 해시값을 부여( bundle.YsAIAAAA-QG4G6KAAAAOK.js, bundle.js?v=QG4G6KAAAAOK)
  - 배포 시에만 버전을 변경하여 최신 리소스로 배포되며, 그 이전까지는 최대한 길게 캐시한다.
- Main resources(html)
  - Cache-Control: no-cache