package persistent;

import com.google.inject.Inject;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import core.Account;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Stanislav Valov <stanislav.valov@clouway.com>
 *
 * Setup JDBC from PersistentModule
 */
class PersistentJDBCAccountRepository implements AccountRepository {
    private final MysqlDataSource dataSource;

    @Inject
    PersistentJDBCAccountRepository(MysqlDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(Account account) {
        try {
            PreparedStatement statement = connection().prepareStatement("INSERT INTO accounts VALUES (?,?,?,?,?)");

            statement.setString(1, account.nickname);
            statement.setString(2, account.firstName);
            statement.setString(3, account.lastName);
            statement.setString(4, account.email);
            statement.setDate(5, (Date) account.dateOfBirth);

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Account account) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public List<Account> findAll() {
        return newArrayList();
    }

    @Override
    public void importImage(String s, String nickname) {

    }

    private Connection connection() {
        try {
            return dataSource.getConnection("root", "hisazzul");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
