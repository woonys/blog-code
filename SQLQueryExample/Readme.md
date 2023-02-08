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

### 서브쿼리에서 최신 날짜 하나만 빼오기

```sql
SELECT *
FROM (
-- orderBy가 아닌 MAX()를 이용한다
SELECT user_id, MAX(created_at) as latest_order_created_at
FROM a.order_summary
GROUP BY user_id
) orders
```