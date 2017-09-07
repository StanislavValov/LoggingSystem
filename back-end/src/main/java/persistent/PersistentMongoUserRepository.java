package persistent;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import core.User;
import org.bson.Document;

import java.util.Optional;

/**
 * Created by <hisazzul@gmail.com> on 8/8/2017.
 */
public class PersistentMongoUserRepository implements UserRepository {

    private Provider<MongoDatabase> database;

    @Inject
    public PersistentMongoUserRepository(Provider<MongoDatabase> database) {
        this.database = database;
    }

    @Override
    public Optional<Object> authenticate(String firstName, String password) {
        boolean userExists = userExists(firstName, password);

        if (!userExists){
            return Optional.empty();
        }

        return Optional.of(true);
    }

    @Override
    public void register(User user) {
        Document document = new Document("firstName", user.firstName);

        document.append("lastName", user.lastName);
        document.append("email", user.email);
        document.append("password", user.password);

        users().insertOne(document);
    }

    @Override
    public boolean userExists(String username) {
        Document document = new Document("firstName", username);

        return 1 == users().count(document);
    }

    private boolean userExists(String username, String password) {
        Document document = new Document("firstName", username);
        document.append("password", password);

        return 1 == users().count(document);
    }

    private MongoCollection<Document> users() {
        return database.get().getCollection("users");
    }
}