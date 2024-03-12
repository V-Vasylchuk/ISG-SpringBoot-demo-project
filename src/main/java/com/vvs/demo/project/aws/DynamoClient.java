package com.vvs.demo.project.aws;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

@Component
@RequiredArgsConstructor
public class DynamoClient {
    private static final String TABLE_NAME = "LL7TestDynamoDB";
    private final DynamoDbClient dynamoDbClient;

    public void saveData(String path, String method, String parameters, String body, String timestamp) {
        Map<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put("uuId", AttributeValue.builder().s(UUID.randomUUID().toString()).build());
        itemValues.put("Path", AttributeValue.builder().s(path).build());
        itemValues.put("Method", AttributeValue.builder().s(method).build());
        itemValues.put("Parameters", AttributeValue.builder().s(parameters).build());
        itemValues.put("Body", AttributeValue.builder().s(body).build());
        itemValues.put("Timestamp", AttributeValue.builder().s(timestamp).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(itemValues)
                .build();

        dynamoDbClient.putItem(request);
    }

/*    public void updateLogEntry(String path, String status, String response, String errorMessage) {
        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#S", "Status");
        expressionAttributeNames.put("#R", "Response");
        expressionAttributeNames.put("#E", "ErrorMessage");

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":s", AttributeValue.builder().s(status).build());
        expressionAttributeValues.put(":r", AttributeValue.builder().s(response).build());
        expressionAttributeValues.put(":e", AttributeValue.builder().s(errorMessage).build());

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of("Method", AttributeValue.builder().s(path).build()))
                .updateExpression("SET #S = :s, #R = :r, #E = :e")
                .expressionAttributeNames(expressionAttributeNames)
                .expressionAttributeValues(expressionAttributeValues)
                .build();

        dynamoDbClient.updateItem(request);
    }*/

}
