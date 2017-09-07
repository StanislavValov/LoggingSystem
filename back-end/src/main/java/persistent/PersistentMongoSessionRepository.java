package persistent;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Created by <hisazzul@gmail.com> on 8/8/2017.
 */
class PersistentMongoSessionRepository implements SessionRepository{

    private Provider<MongoDatabase> database;

    @Inject
    PersistentMongoSessionRepository(Provider<MongoDatabase> database) {
        this.database = database;
    }

    @Override
    public void addUser(String username, Object sessionId) {
        Document document = new Document("firstName", username);
        document.append("sessionID", sessionId);

        sessions().insertOne(document);
    }

    private MongoCollection<Document> sessions(){
        return database.get().getCollection("sessions");
    }
}
