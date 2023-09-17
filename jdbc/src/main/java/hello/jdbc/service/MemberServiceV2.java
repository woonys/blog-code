package hello.jdbc.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {
    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        //이제는 여기서 커넥션 받아와야 한다! 트랜잭션 만들어야 하니
        Connection con = dataSource.getConnection();
        try {
            con.setAutoCommit(false); // 트랜잭션 시작 -> autoCommit 끄는 것부터
            // 비즈니스 로직
            bizLogic(fromId, toId, money, con);
            con.commit(); // 성공시 커밋
        } catch (Exception e) {
            con.rollback();
            throw new IllegalStateException(e);
        } finally {
            // Release
            release(con);
        }

    }

    private void bizLogic(String fromId, String toId, int money, Connection con) throws SQLException {
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, fromMember.getMoney() - money); // fromMember의 돈을 깎는다.
        // 오류 케이스를 위해 검증 로직 추가
        validation(toMember);
        memberRepository.update(con, toId, toMember.getMoney() + money); // toMember의 돈을 올린다.
    }

    private void release(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true); // 트랜잭션 끝나면 다시 autoCommit 켜줘야! 기본값은 true니까
                con.close();
            } catch (Exception e) {
                log.info("error", e);
            }
        }
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
