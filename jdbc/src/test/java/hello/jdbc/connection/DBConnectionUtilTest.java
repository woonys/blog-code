package hello.jdbc.connection;

import java.sql.Connection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class DBConnectionUtilTest {
    @Test
    void connection() {
        Connection connection = DBConnectionUtil.getConnection(); // JdbcConnection이 구현체로 들어간다.
        Assertions.assertThat(connection).isNotNull();
    }
}
