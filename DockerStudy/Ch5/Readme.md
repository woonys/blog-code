# Ch.5: 도커 컴포즈 도구를 이용한 다중 컨테이너 애플리케이션 서비스 배포

## 5.1 도커 컴포즈

- 도커 컴포즈: 공통성을 갖는 컨테이너 애플리케이션 스택을 YAML 코드로 정의하는 정의서이자 이를 실행하기 위한 다중 컨테이너 실행 도구
- 하나의 웹 애플리케이션을 생성하기 위해 필요한 도구 각각을 도커로 띄운다면?
    - React: 프론트엔드 서버(React, Angular, …)
    - node.js 서버: API 서버(플라스크, Node 생성)
    - MySQL 데이터베이스: 애플리케이션 데이터 저장
- 이 각각을 도커 컴포즈 YAML 코드로 정의하면 한 번에 서비스를 올리고 관리 가능!
    - 도커 컴포즈로 실행된 컨테이너: 독립된 기능을 가짐 & 공통 네트워크로 구성되어 있어 컨테이너 간 통신이 쉬움
    - 도커 컴포즈 ⇒ 공통성 있는 컨테이너들을 포함해 쉽고 빠른 런타임 환경을 제공
- 도커 컴포즈: 테스트, 개발, 운영의 모든 환경에서 구성이 가능한 오케스트레이션 도구

### 5.1.1 도커 컴포즈 설치


> 💡 여기서는 Ubuntu 리눅스 기반으로 도커 컴포즈를 설치

- 설치 명령어

```bash
sudo curl -L https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
```

- 실행 권한 설정하고 절대 경로 문제로 발생하는 실행 오류를 대비하기 위해 심벌릭 링크 설정

```bash
# 권한 변경
sudo chmod +x /usr/local/bin/docker-compose

#
sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose

sudo chown /usr/local/bin/docker-compose

# 설치된 도커 컴포즈 확인
$ docker-compose --version
```

**간단히 실습**

```bash
# 도커 컴포즈 YAML 코드 작성해보자

# docker-compose.yaml
version: '3.3'
services:
  mydb:
    image: mariadb:10.4.6
    restart: always
    environment:
      - MYSQL_ROOT_PASSWORD=woony
      - MYSQL_DATABASE=appdb
    volumes:
      - ./db-data:/var/lib/mysql
    ports:
      - '3306:3306'
```

```bash
#도커 컴포즈 실행

ubuntu@ip-172-31-3-145:~/5th/mariadb_app$ docker-compose up
```

```bash
# 다른 터미널 열어서 도커 컴포즈 명령어인 ps 명령으로 생성된 컨테이너 정보 조회

ubuntu@ip-172-31-3-145:~/5th/mariadb_app$ docker-compose ps
       Name                    Command             State                    Ports                  
---------------------------------------------------------------------------------------------------
mariadb_app_mydb_1   docker-entrypoint.sh mysqld   Up      0.0.0.0:3306->3306/tcp,:::3306->3306/tcp

#docker 데이터 지속성 확인

```

- MariaDB 접속

```bash
Welcome to the MariaDB monitor.  Commands end with ; or \g.
Your MariaDB connection id is 8
Server version: 10.4.6-MariaDB-1:10.4.6+maria~bionic mariadb.org binary distribution

Copyright (c) 2000, 2018, Oracle, MariaDB Corporation Ab and others.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

MariaDB [(none)]> show databases;
+--------------------+
| Database           |
+--------------------+
| appdb              |
| information_schema |
| mysql              |
| performance_schema |
+--------------------+
4 rows in set (0.001 sec)

MariaDB [(none)]> use appdb;
Database changed
MariaDB [appdb]> create table item (item_id int, item_name varchar(10));
Query OK, 0 rows affected (0.013 sec)

MariaDB [appdb]> insert into item values (20, 'docker-ce');
Query OK, 1 row affected (0.003 sec)

MariaDB [appdb]> select * from item;
+---------+-----------+
| item_id | item_name |
+---------+-----------+
|      20 | docker-ce |
+---------+-----------+
1 row in set (0.000 sec)

MariaDB [appdb]> exit;

# 데이터 확인

root@b91b58597940:/# ls -l /var/lib/mysql/appdb/
total 104
-rw-rw---- 1 mysql mysql    65 Mar  7 13:18 db.opt
-rw-rw---- 1 mysql mysql   476 Mar  7 13:27 item.frm
-rw-rw---- 1 mysql mysql 98304 Mar  7 13:27 item.ibd

# 볼륨을 통해 호스트 경로와 연결된 자료 확인 -> 바깥에 그대로 있더라!
ubuntu@ip-172-31-3-145:~/5th/mariadb_app$ sudo ls -l db-data/appdb/
total 104
-rw-rw---- 1 lxd docker    65 Mar  7 13:18 db.opt
-rw-rw---- 1 lxd docker   476 Mar  7 13:27 item.frm
-rw-rw---- 1 lxd docker 98304 Mar  7 13:27 item.ibd
```

- `docker-compose down` :  서비스 모두 내리고 네트워크 회수

```bash
ubuntu@ip-172-31-3-145:~/5th/mariadb_app$ docker-compose down
Stopping mariadb_app_mydb_1 ... done
Removing mariadb_app_mydb_1 ... done
Removing network mariadb_app_default
```

- 데이터 영속성을 위한 볼륨 기능 확인 → 도커 컴포즈를 백그라운드(-d)로 실행

```bash
ubuntu@ip-172-31-3-145:~/5th/mariadb_app$ docker-compose up -d
Creating network "mariadb_app_default" with the default driver
Creating mariadb_app_mydb_1 ... done
ubuntu@ip-172-31-3-145:~/5th/mariadb_app$ docker-compose ps
       Name                    Command             State                    Ports                  
---------------------------------------------------------------------------------------------------
mariadb_app_mydb_1   docker-entrypoint.sh mysqld   Up      0.0.0.0:3306->3306/tcp,:::3306->3306/tcp

ubuntu@ip-172-31-3-145:~/5th/mariadb_app$ docker exec -it mariadb_app_mydb_1 bash

root@e06e2f3931ec:/# mysql -uroot -p
```

- 내부 데이터 확인 → 그대로 유지!

```bash
MariaDB [(none)]> show databases;
+--------------------+
| Database           |
+--------------------+
| appdb              |
| information_schema |
| mysql              |
| performance_schema |
+--------------------+
4 rows in set (0.000 sec)

MariaDB [(none)]> show tables;
ERROR 1046 (3D000): No database selected
MariaDB [(none)]> show appdb;
ERROR 1064 (42000): You have an error in your SQL syntax; check the manual that corresponds to your MariaDB server version for the right syntax to use near 'appdb' at line 1
MariaDB [(none)]> use appdb;
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

Database changed
MariaDB [appdb]> show tables;
+-----------------+
| Tables_in_appdb |
+-----------------+
| item            |
+-----------------+
1 row in set (0.000 sec)

MariaDB [appdb]> select * from item;
+---------+-----------+
| item_id | item_name |
+---------+-----------+
|      20 | docker-ce |
+---------+-----------+
1 row in set (0.001 sec)
```


>💡 눈여겨볼 점: docker-compose up 명령을 수행하면 자체 기본 네트워크가 생성된다.
> 여러 개의 다중 컨테이너 설정인 경우 docker-compose up과 동시에 가장 먼저 네트워크를 디렉터리명_default 이름으로 생성한다.

```bash
# 도커 네트워크 조회
ubuntu@ip-172-31-3-145:~/5th/mariadb_app$ docker network ls
NETWORK ID     NAME                  DRIVER    SCOPE
8383b11eb801   bridge                bridge    local
48faf2927a8f   host                  host      local
188b1ccf3458   mariadb_app_default   bridge    local
576b3bd4e55a   none                  null      local
```

## 5.1.2 도커 컴포즈 YAML 코드 작성

- 쿠버네티스 역시 거의 모든 오브젝트를 YAML 코드로 작성 → 도커 이후 쿠버네티스 공부할 때도 도움
- YAML: 사용자가 쉽게 읽고 쓸 수 있는 텍스트 구조
  - 주의: 들여쓰기를 탭이 아닌 공백으로 정확히 구분해야 함(2칸)

### 1. 버전 정의

- 야믈 코드 첫 줄: 버전 명시

```bash
version: '3.8'
```

- 컴포즈 버전: 도커 엔진 릴리스와 연관

### 2. 서비스 정의

- 도커 컴포즈를 통해 실행할 서비스를 정의
- 도커 컴포즈: 컨테이너 대신 서비스 개념을 사용
  - 상위의 version과 동일 레벨에서 작성

```bash
version: '3.8'
services:
```

- `services` 하위에는 실행될 컨테이너 서비스를 작성 & 하위 레벨에 도커 명령 실행과 유사하게 컨테이너 실행에 필요한 옵션 작성

```bash
version: '3.8'
services:
	myweb:
		image: nginx:latest
	mydb:
		image: mariadb:10.4.6
```

- Dockerfile을 작성해서 실행하는 경우는 미리 빌드하거나 아래와 같이 build 옵션을 사용하면 알아서 이미지 빌드 후 up

```bash
version: '3.8'
services:
	myweb:
		build: .
```

### 3. 네트워크 정의

- 다중 컨테이너들이 사용할 최상위 네트워크 키를 정의하고 이하 하위 서비스 단위로 네트워크 선택할 수 있음.

```bash
version: '3.8'
services:
	...
networks:
	default:
		external:
			name: ~~
```

### 4. 볼륨 정의

- 데이터 지속성 유지하기 위해 최상위 레벨에 볼륨 정의하고 서비스 레벨에서 볼륨명과 서비스 내부 디렉터리를 바인드

```bash
version: '3.8'
services:
	myweb:
		image: mysql:5.7
		volumes:
			- db_data:/var/lib/mysql
```

### 5. 도커 명령어와 도커 컴포즈 야믈 코드 비교

- 도커 컴포즈 내 야믈 코드 옵션은 도커 명령어를 기반

### 구성 실습: docker-compose로 다중 컨테이너 서비스 실행하기

- WordPress
- MySQL

```bash
# 볼륨 생성
ubuntu@ip-172-31-3-145:~$ docker volume create mydb_data
mydb_data
ubuntu@ip-172-31-3-145:~$ docker volume create myweb_data
myweb_data
ubuntu@ip-172-31-3-145:~$ docker volume ls
DRIVER    VOLUME NAME
local     mydb_data
local     myweb_data

ubuntu@ip-172-31-3-145:~$ docker inspect --type volume mydb_data
[
    {
        "CreatedAt": "2023-03-10T14:15:07Z",
        "Driver": "local",
        "Labels": null,
        "Mountpoint": "/var/lib/docker/volumes/mydb_data/_data",
        "Name": "mydb_data",
        "Options": null,
        "Scope": "local"
    }
]

ubuntu@ip-172-31-3-145:~$ docker inspect --type volume myweb_data
[
    {
        "CreatedAt": "2023-03-10T14:15:17Z",
        "Driver": "local",
        "Labels": null,
        "Mountpoint": "/var/lib/docker/volumes/myweb_data/_data",
        "Name": "myweb_data",
        "Options": null,
        "Scope": "local"
    }
]
ubuntu@ip-172-31-3-145:~$ docker network create myapp-net
d84e8d8b345d9fc87d368e390824af2ea44001f762e45d80c7ad2f2e48fb0bb3
ubuntu@ip-172-31-3-145:~$ docker network ls
NETWORK ID     NAME                  DRIVER    SCOPE
8383b11eb801   bridge                bridge    local
48faf2927a8f   host                  host      local
223c2dee96bd   mariadb_app_default   bridge    local
d84e8d8b345d   myapp-net             bridge    local
576b3bd4e55a   none                  null      local

ubuntu@ip-172-31-3-145:~/my_wp$ ls
docker-compose.yml
ubuntu@ip-172-31-3-145:~/my_wp$ vi docker-compose.yml 
ubuntu@ip-172-31-3-145:~/my_wp$ docker-compose up -d
ERROR: The Compose file './docker-compose.yml' is invalid because:
Unsupported config option for services.networks: 'backend-net'
Unsupported config option for services.volumes: 'mydb_data'
ubuntu@ip-172-31-3-145:~/my_wp$ ls
docker-compose.yml
ubuntu@ip-172-31-3-145:~/my_wp$ vi docker-compose.yml 
ubuntu@ip-172-31-3-145:~/my_wp$ docker-compose up -d

ubuntu@ip-172-31-3-145:~/my_wp$ vi docker-compose.yml 
version: "3.9"
services:
# 첫번째 서비스 정의
  mydb:
    image: mysql:8.0
    container_name: mysql_app
    volumes:
      - mydb_data:/var/lib/mysql
    # 수동 제어를 제외한 컨테이너 종료 시 자동 재시작
    restart: always
    # 호스트 운영체제와 컨테이너의 3306 포트를 바인드
    # workbench 같은 클라이언트 도구와 연결하기 위해 필요
    ports:
      - "3306:3306"
    networks:
      - backend-net
    environment:
      MYSQL_ROOT_PASSWORD: password#
      MYSQL_DATABASE: wpdb
      MYSQL_USER: wpuser
      MYSQL_PASSWORD: wppassword
  # 두번째 서비스
  myweb:
    #의존성 설정
    depends_on:
      - mydb
    image: wordpress:5.7
    container_name: wordpress_app
    ports:
      - "8888:80"
    networks:
      - backend-net
      - frontend-net
    volumes:
      - myweb_data:/var/www/html
      - ${PWD}/myweb-log:/var/log \
    restart: always
    environment:
      WORDPRESS_DB_HOST: mydb:3306
      WORDPRESS_DB_USER: wpuser
      WORDPRESS_DB_PASSWORD: wppassword
      WORDPRESS_DB_NAME: wpdb

networks:
  frontend-net: {}
  backend-net: {}

  # 도커 컴포즈 애플리케이션이 사용할 볼륨 생성 -> docker volume create와 동일
volumes:
  mydb_data: {}
  myweb_data: {}
```