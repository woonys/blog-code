## 복잡한 SQL 쿼리 연습하기

### 서브 쿼리

```sql
SELECT *
FROM (
SELECT user_id, MAX(created_at) as latest_order_created_at
FROM a.order_summary
GROUP BY user_id
) orders
JOIN (
SELECT user_id, created_at as created_at
FROM a.user_request
) request
ON orders.user_id = request.user_id
JOIN tx.user u
ON request.user_id = u.user_id
JOIN (
SELECT user_id, order_status_cd
FROM tx.order_master
) master
ON orders.user_id = master.user_id
WHERE request.created_at < orders.latest_order_created_at
AND u.user_status_cd = 10
ORDER BY latest_order_created_at DESC
```

### 서브쿼리에서 최신 날짜 하나만 빼오는 방법 1: MAX() 이용하기 -> 이때는 GROUP BY를 이용해서 묶는다.

```sql
SELECT *
FROM (
-- orderBy가 아닌 MAX()를 이용한다
SELECT user_id, MAX(created_at) as latest_order_created_at
FROM a.order_summary
GROUP BY user_id
) orders
```

### 서브쿼리에서 최신 날짜 하나만 빼오는 방법 2: ORDER BY & LIMIT 1을 이용하기

```sql
SELECT *
FROM (
-- orderBy가 아닌 MAX()를 이용한다
SELECT user_id, created_at as latest_order_created_at
FROM a.order_summary
ORDER BY user_id DESC
LIMIT 1
) orders
```

### 주의사항: COUNT()와 ORDER BY는 절대 같이 쓸 수 없다!

실수하던 것 중 하나. 특정 row 수를 세고 싶은데, 기존에 썼던 쿼리에서 `SELECT *` 부분만 `SELECT COUNT(*)`로 바꿨더니 계속 오류가 터졌다.
알고보니 기존에 썼던 ORDER BY를 그대로 둔 상태였기에 발생한 문제였던 것.
딱 봐도 COUNT와 ORDER BY는 양립할 수 없는 문법이다. 갯수를 카운트하는데 정렬할 필요가 왜 있겠나. 설령 허용해준다고 한들 되려 시간만 더 낭비하는 꼴이 된다.

```sql
SELECT COUNT(*)
FROM (
SELECT user_id, MAX(created_at) as latest_order_created_at
FROM a.order_summary
GROUP BY user_id
) orders
JOIN (
SELECT user_id, created_at as created_at
FROM a.user_request
) request
ON orders.user_id = request.user_id
JOIN tx.user u
ON request.user_id = u.user_id
JOIN (
SELECT user_id, order_status_cd
FROM tx.order_master
) master
ON orders.user_id = master.user_id
WHERE request.created_at < orders.latest_order_created_at
AND u.user_status_cd = 10
-- ORDER BY latest_order_created_at DESC -> 반드시 지워줘야 함
```