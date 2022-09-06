# Thread

### 동시성 이슈 확인 및 해결 방법
#### 1) 동시성 문제 확인
private final List<User> users = new ArrayList<>();라는 공유 자원을 두 개의 스레드가 한 번에 읽으면서 발생

<img width="500" alt="스크린샷 2022-09-06 오후 8 32 23" src="https://user-images.githubusercontent.com/61091307/188625635-c2c03de5-1230-401c-818f-caa66621cd73.png">

디버깅을 걸어 놓고 확인해보면 해당 부분에 2개의 스레드가 들어와 있는 것을 알 수 있습니다.

해당 코드가 바이트 코드로 변환이 되었을 때, 여러 연산으로 이루어져 있기 때문입니다.
<img width="500" alt="스크린샷 2022-09-06 오후 8 18 42" src="https://user-images.githubusercontent.com/61091307/188626500-d972e2c4-fa1e-4a68-8eef-08a4daacac7f.png">

#### 2) 해결 방법
해당 메서드에 synchronized로 통해 동기화해주면 하나의 스레드만 들어오는 것을 확인할 수 있습니다.
<img width="500" alt="스크린샷 2022-09-06 오후 8 31 56" src="https://user-images.githubusercontent.com/61091307/188627042-0cd1f53a-f36c-4256-a5f2-510e24e04ac6.png">