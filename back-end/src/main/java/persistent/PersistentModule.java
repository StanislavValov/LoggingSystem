package persistent;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * @author Stanislav Valov <stanislav.valov@clouway.com>
 */
public class PersistentModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(AccountRepository.class).to(PersistentMongoAccountRepository.class).in(Singleton.class);
    bind(SessionRepository.class).to(PersistentMongoSessionRepository.class).in(Singleton.class);
    bind(UserRepository.class).to(PersistentMongoUserRepository.class).in(Singleton.class);
  }

  @Provides
  @Singleton
  public MongoClient getMongoDb() {
    return new MongoClient();
  }

  @Provides
  @Singleton
  public MysqlDataSource getDatasource(){
    MysqlDataSource dataSource = new MysqlDataSource();
    dataSource.setPassword("hisazzul");
    dataSource.setUser("root");
    dataSource.setURL("jdbc:mysql://127.0.0.1/accounts");

    return dataSource;
  }

  @Provides
  public MongoDatabase getProvisioningDatabase(Provider<MongoClient> mongoDatabaseProvider, @Named("db.name") String databaseName) {
    return mongoDatabaseProvider.get().getDatabase(databaseName);
  }

}
