package com.crm.gym.integration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class MongoWorkloadTestRepository {

    private final MongoClient mongoClient;

    private final MongoCollection<Document> collection;

    public MongoWorkloadTestRepository() {

        mongoClient =
                MongoClients.create(
                        "mongodb://admin:admin@localhost:27017/?authSource=admin"
                );

        MongoDatabase database =
                mongoClient.getDatabase("trainer-workload-db");

        collection =
                database.getCollection("trainer_workloads");
    }

    public void deleteAll() {

        collection.deleteMany(new Document());
    }

    public List<Document> findAll() {

        List<Document> result = new ArrayList<>();

        collection.find().into(result);

        return result;
    }

    public Optional<Map<String, Object>> findByUsername(
            String username) {

        Document document =
                collection.find(
                        eq("username", username)
                ).first();

        if (document == null) {
            return Optional.empty();
        }

        return Optional.of(document);
    }

    public int calculateTotalDuration(
            Map<String, Object> workload) {

        Object yearsObject =
                workload.get("years");

        if (!(yearsObject instanceof List<?> years)) {
            return 0;
        }

        int total = 0;

        for (Object yearObject : years) {

            if (!(yearObject instanceof Map<?, ?> year)) {
                continue;
            }

            Object monthsObject =
                    year.get("months");

            if (!(monthsObject instanceof List<?> months)) {
                continue;
            }

            for (Object monthObject : months) {

                if (!(monthObject instanceof Map<?, ?> month)) {
                    continue;
                }

                Object durationObject =
                        month.get("duration");

                if (durationObject instanceof Number duration) {

                    total += duration.intValue();
                }
            }
        }

        return total;
    }

    public void close() {

        mongoClient.close();
    }
}