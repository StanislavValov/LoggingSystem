package persistent;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import core.Account;
import org.bson.Document;

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
    public void create(Account account) throws DuplicationException {

        if (userExists(account.nickname)) {
            throw new DuplicationException("Account with nickname " + account.nickname + " already exists!");
        }

        Document document = new Document("_id", account.nickname);

        document.append("firstName", account.firstName);
        document.append("lastName", account.lastName);
        document.append("email", account.email);
        document.append("dateOfBirth", account.dateOfBirth);
        document.append("imageData", account.imageData);

        accounts().insertOne(document);
    }

    @Override
    public void update(Account account){
        if (!userExists(account.nickname)){
            return;
        }

        Document query = new Document("_id", account.nickname);
        Document document = new Document("firstName", account.firstName);

        document.append("lastName", account.lastName);
        document.append("email", account.email);
        document.append("dateOfBirth", account.dateOfBirth);

        Document update = new Document("$set", document);

        accounts().updateOne(query, update);
    }

    @Override
    public void delete(String nickname) {
        Document document = new Document("_id", nickname);

        accounts().deleteOne(document);
    }

    @Override
    public List<Account> findAll() {
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

    private List<Account> adapt(FindIterable<Document> documents) {
        List<Account> accounts = newArrayList();

        for (Document document : documents) {
            accounts.add(adapt(document));
        }

        return accounts;
    }

    private boolean userExists(String nickname) {
        return 1 == accounts().count(new Document("_id", nickname));
    }

    private Account adapt(Document found) {
        String nickname = found.getString("_id");
        String firstName = found.getString("firstName");
        String lastName = found.getString("lastName");
        String email = found.getString("email");
        Date data = found.getDate("dateOfBirth");
        String imageData = found.getString("imageData");

        return new Account(nickname, firstName, lastName, email, data, imageData);
    }

    private MongoCollection<Document> accounts() {
        return database.get().getCollection("accounts");
    }
}
