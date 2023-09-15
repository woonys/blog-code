package hello.jdbc.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;

import javax.sql.DataSource;

import org.springframework.jdbc.support.JdbcUtils;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

/**
 * JDBC - DataSource 사용, JdbcUtils 사용
 */
@Slf4j
public class MemberRepositoryV1 {

    private final DataSource dataSource; // dataSource 의존관계 주입 -> DriverManagerDataSource에서 HikariCP로 바뀌어도 여기 코드는 바뀔 일이 없음.

    public MemberRepositoryV1(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null; // pstmt: stmt를 상속받아서 기본적으로 sql문을 담는 객체인데 위에처럼 파라미터를 바인딩할 수 있음
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId()); // 위의 sql insert문에서 member_id column에 들어갈 (?)에 getMemberId() 매핑
            pstmt.setInt(2, member.getMoney());// 위의 sql insert문에서 money column에 들어갈 (?)에 getMoney() 매핑
            pstmt.executeUpdate(); // 쿼리가 실제 db로 날아간다.
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e; // 바깥에 exception 던진다. try-catch문에서 로그만 확인하고 바깥으로 exception을 던짐.
        } finally {
            // 작업 끝났으면 리소스 관리 측면에서 자원 닫아줘야!
            close(con, pstmt,null); // 항상 close 호출되는 게 보장되도록 finally에서 호출한다!
        }
    }

    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?"; // ? -> 파라미터 바인딩! SQLInjection 공격 방어를 위해!
        Connection con = null; // try-catch-finally 선언을 위해 바깥에서 null 선언..
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId); // 파라미터 넣어준다.
            rs = pstmt.executeQuery(); // 조회할 때는 executeQuery() 메소드를 사용한다.
            if (rs.next()) { // 처음에는 아무 곳도 안 가리킨다 -> 한 칸 이동해야 데이터 있는지 체크
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found: memberId = " + memberId);
                // memberId와 같은 키값은 로그 찍을 때 꼭 넣어주자.
            }

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs); // 해제할 때는 rs -> pstmt -> con 순으로
        }
    }

    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate(); // int: 쿼리에 의해 영향받은 row 수를 반환. 여기서는 값 하나만 업데이트하니 1을 반환.
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.info("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        // stmt와 con을 각각 분리해주는 이유 -> stmt가 close하는 도중에 exception 터지면 connection이 close되지 않으니
        // 리소스 정리할 때는 항상 역순으로 해야 함!
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }

    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }
}
