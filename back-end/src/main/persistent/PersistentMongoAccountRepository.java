package main.persistent;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import main.core.User;
import main.util.Encoder;
import org.bson.Document;
import org.bson.types.Binary;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Stanislav Valov <stanislav.valov@clouway.com>
 */
class PersistentMongoAccountRepository implements AccountRepository {

    private final Provider<MongoDatabase> database;

    @Inject
    PersistentMongoAccountRepository(Provider<MongoDatabase> database) {
        this.database = database;
    }

    @Override
    public void create(User user) throws DuplicationException {

        if (userExists(user.nickname)) {
            throw new DuplicationException("User with nickname " + user.nickname + " already exists!");
        }

        Document document = new Document("_id", user.nickname);

        document.append("firstName", user.firstName);
        document.append("lastName", user.lastName);
        document.append("email", user.email);
        document.append("dateOfBirth", user.dateOfBirth);
        document.append("imageData", user.imageData);

        accounts().insertOne(document);
    }

    @Override
    public void update(User user){
        if (!userExists(user.nickname)){
            return;
        }

        Document query = new Document("_id", user.nickname);
        Document document = new Document("firstName", user.firstName);

        document.append("lastName", user.lastName);
        document.append("email", user.email);
        document.append("dateOfBirth", user.dateOfBirth);

        Document update = new Document("$set", document);

        accounts().updateOne(query, update);
    }

    @Override
    public void delete(String nickname) {
        Document document = new Document("_id", nickname);

        accounts().deleteOne(document);
    }

    @Override
    public List<User> findAll() {
        FindIterable<Document> documents = accounts().find();

        return adapt(documents);
    }

    @Override
    public void importImage(String nickname, String imageData) {
        if (!userExists(nickname)){
            return;
        }

        Document query = new Document("_id", nickname);
        Document document = new Document("imageData", imageData);

        Document update = new Document("$set", document);

        accounts().updateOne(query, update);
    }

    private List<User> adapt(FindIterable<Document> documents) {
        List<User> users = newArrayList();

        for (Document document : documents) {
            users.add(adapt(document));
        }

        return users;
    }

    private boolean userExists(String nickname) {
        return 1 == accounts().count(new Document("_id", nickname));
    }

    private User adapt(Document found) {
        String nickname = found.getString("_id");
        String firstName = found.getString("firstName");
        String lastName = found.getString("lastName");
        String email = found.getString("email");
        Date data = found.getDate("dateOfBirth");
        String imageData = found.getString("imageData");

        return new User(nickname, firstName, lastName, email, data, imageData);
    }

    private MongoCollection<Document> accounts() {
        return database.get().getCollection("accounts");
    }
}
