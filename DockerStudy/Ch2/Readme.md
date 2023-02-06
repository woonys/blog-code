## 2.1 도커 엔진

- 도커는 기존 리눅스 컨테이너 기술을 이용해 애플리케이션을 컨테이너로서 사용할 수 있게 만들었다.
- 버전 정보를 확인해 보면 Go 언어로 구성된 것을 확인할 수 있다.(`docker version`)
![img.png](img.png)

### **컨테이너 가상화 기술의 변천사**

- Ref: [https://kangwoo.kr/2020/07/26/도커-구조/](https://kangwoo.kr/2020/07/26/%EB%8F%84%EC%BB%A4-%EA%B5%AC%EC%A1%B0/)
- 초기 도커: 리눅스 컨테이너 기술인 LXC를 기반으로 하는 컨테이너
    - 리눅스의 특수 기능인 LXC를 이용하면 실행 환경을 격리할 수 있다.
  ![img_1.png](img_1.png)
    - 하나의 OS에서 커널을 공유하고 각각의 실행 환경이 하나의 프로세스처럼 움직이므로 이전의 하이퍼바이저처럼 각 실행환경 별로 독자적인 OS를 가질 필요가 없어 훨씬 가벼움.
    - 리눅스 기술인 chroot 기능을 이용해 프로세스를 격리 
      > 💡 “혁신은 없었다” 애플의 신제품이 나올 때마다 들리는 이야기입니다만, 컨테이너야 말로 **“혁신은 없었던 신기술”** 중에 하나입니다.([링크](https://www.44bits.io/ko/post/change-root-directory-by-using-chroot))
      - `chroot`: 디렉토리에서 프로세스가 실행되는 루트를 변경한다.
      ![img_2.png](img_2.png)
      - chroot로 실행한 프로세스 K: /A를 새로운 루트로 사용한다.


- 0.9.0 버전부터는 libcontainer OCI를 이용
    - [libcontainer?](https://github.com/opencontainers/runc/tree/main/libcontainer)
      >  💡 **Libcontainer is pure Go library which we developed to access the kernel’s container APIs directly, without any other dependencies.** It provides a native Go implementation for creating containers with namespaces, cgroups, capabilities, and filesystem access controls. → 리눅스에 의존하던 기존 LXC와 달리 순수 Go 언어로 구현된 컨테이너 기술(의존성 낮춤)

    - [Why libcontainer instead of LXC?](https://www.docker.com/blog/docker-0-9-introducing-execution-drivers-and-libcontainer/)(도커 공식 블로그)
        - LXC → 리눅스에 의존되어 있었음. LXC의 경우 리눅스 커널 containment 기능을 위한 userspace interface를 제공했다.
        - 반면 libcontainer의 경우, 훨씬 더 넓은 범위의 격리 기술을 제공하는 추상화임.
        - libcontainer가 LXC에 비해 더 나은 이점은
          >  💡 Thanks to libcontainer, Docker out of the box can now manipulate namespaces, control groups, capabilities, apparmor profiles, network interfaces and firewalling rules – all in a consistent and predictable way, and without depending on LXC or any other userland package.
          **This drastically reduces the number of moving parts, and insulates Docker from the side-effects introduced across versions and distributions of LXC.(**[공식 블로그](https://www.docker.com/blog/docker-0-9-introducing-execution-drivers-and-libcontainer/)**)**

            1. lib는 커널의 컨테이너 API를 직접 접근할 수 있도록 만들어짐으로써 의존성을 없앴다는 점이다.
                - libcontainer 라이브러리로, 도커는 namespace, control groups(cgroup), capabilities, 방화벽 규칙 등을 LXC나 다른 외부 패키지에 의존하지 않고 제어할 수 있게 됐다.
                - libcontainer로 인해 의존성이 줄어들었고, 이는 더 안정적이고 효율적이게 됐다.
            2. Libcontainer의 경우 재사용성이 좋고 LXC에 비해 다른 벤더(CoreOS 등)에 적용하기가 더 쉽다.

            ![img_3.png](img_3.png)

- 1.11.0 이후 버전부터는 [runC OCI](https://github.com/opencontainers/runc)([공식문서 링크](https://github.com/opencontainers/runc))를 이용
    - runC(libcontainer 의 리팩토링 구현체):
        - libcontainer는 Go 라이브러리라면,
        - runc는 OCI 런타임 표준을 준수하는 컨테이너를 생성 및 실행하기 위한 CLI 툴 → libcontainer를 사용하며 원래는 도커를 위해 개발되었으나 OCI 표준으로 자리 잡으면서 도커에서 독립
        - `runc`는  `libcontainer`의 `client wrapper`로 Go 언어로 개발되었으며, 현재 Docker에서 저수준 컨테이너 런타임으로 사용되고 있음.
      ![img_4.png](img_4.png)
    - Background([도커 공식 블로그 글](https://www.docker.com/blog/runc/))
      - 도커가 발전함에 따라 그에 따른 보일러플레이트 코드 & 의존성이 많았다(Linux, Go, LXC, …)
          - 전체 코드의 50% 이상이 infrastructure plumbing
      - 최대한 코드를 재사용할 수 있는 방안 모색
      - runC is a lightweight, portable container runtime. It includes all of the plumbing code used by Docker to interact with system features related to containers. It is designed with the following principles in mind:
          - Designed for security.
          - Usable at large scale, in production, today.
          - No dependency on the rest of the Docker platform: just the container runtime and nothing else.

## macOS용 도커 엔진 설치
