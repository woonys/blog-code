package hello.jdbc.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

/**
 * JDBC - DriverManager 사용
 */
@Slf4j
public class MemberRepositoryV0 {
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

    private void close(Connection con, Statement stmt, ResultSet rs) {
        // stmt와 con을 각각 분리해주는 이유 -> stmt가 close하는 도중에 exception 터지면 connection이 close되지 않으니
        // 리소스 정리할 때는 항상 역순으로 해야 함!
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
