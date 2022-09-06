# Thread

- JVM은 대부분 단일 프로세스에서 실행이 된다.
- 프로세스를 새로 생성하는 것보다, 스레드를 생성하는 것이 리소스가 덜 든다. 
  - ➔ 이유: 스레드는 프로세스 내에서 각각 Stack만  독립적으로 가지고 있고,  Code, Data, Heap 영역은 공유한다.
      - 따라서 context switching 비용도 더 적다.
- 자바는 모든 응용 프로그램당 하나 이상의 스레드가 있다. ➔ main 메서드 스레드 1개

### Synchronization
1. 스레드 간섭(Thread Interference)
    1. 동일한 데이터에 여러 작업이 실행되는 경우
    2. 자바 코드를 기계어로 변환하면, 한 줄의 코드라도 여러 줄로 변환이 되는 경우가 있다.
        1. 해당 상태에서 공유된 데이터를 사용하면 데이터 손실이 발생할 수 있다.
2. 메모리 일관성 오류(Memory Consistency errors)
    1. 다른 스레드가 일관성 없이 같은 데이터를 바라볼 때, 메모리 일관성 오류 발생
        1. Happens-before-relationship를 설정해줘야 한다.

#### Thread-safe class
- 상태 변수를 스레드 간에 공유하지 않는다.
- 상태 변수를 변경할 수 없도록 만든다.
- 상태 변수에 접근할 땐 언제나 동기화(synchronization)를 사용한다.
- 캡슐화나 데이터 은닉은 스레드 안전한 클래스 작성에 도움이 된다.(final)

#### Thread-safe class 방법
- 상태 없는 객체는 항상 스레드 안전하다.

## 1단계 - 동시성 이슈 확인 및 해결 방법
#### 1) 동시성 문제 확인
private final List<User> users = new ArrayList<>();라는 공유 자원을 두 개의 스레드가 한 번에 읽으면서 발생

<img width="500" alt="스크린샷 2022-09-06 오후 8 32 23" src="https://user-images.githubusercontent.com/61091307/188625635-c2c03de5-1230-401c-818f-caa66621cd73.png">

디버깅을 걸어 놓고 확인해보면 해당 부분에 2개의 스레드가 들어와 있는 것을 알 수 있습니다.

해당 코드가 바이트 코드로 변환이 되었을 때, 여러 연산으로 이루어져 있기 때문입니다.
<img width="500" alt="스크린샷 2022-09-06 오후 8 18 42" src="https://user-images.githubusercontent.com/61091307/188626500-d972e2c4-fa1e-4a68-8eef-08a4daacac7f.png">

#### 2) 해결 방법
해당 메서드에 synchronized로 통해 동기화해주면 하나의 스레드만 들어오는 것을 확인할 수 있습니다.
<img width="500" alt="스크린샷 2022-09-06 오후 8 31 56" src="https://user-images.githubusercontent.com/61091307/188627042-0cd1f53a-f36c-4256-a5f2-510e24e04ac6.png">


##  2단계 - WAS 스레드 설정
- accept-count: max-connections의 수에 도달했을 때, 연결 요청에 대해 운영 체제가 제공하는 큐의 최대 길이
- max-connections: 서버가 허용하는 최대 커넥션의 수
- threads.max: Connector가 생성할 수 있는 최대 Thread 수