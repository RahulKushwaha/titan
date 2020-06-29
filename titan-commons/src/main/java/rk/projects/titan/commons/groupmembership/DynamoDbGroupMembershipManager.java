package rk.projects.titan.commons.groupmembership;

import com.google.common.collect.ImmutableMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

public class DynamoDbGroupMembershipManager implements GroupMembershipManager {

  private final DynamoDbAsyncClient dynamoDbAsyncClient;

  private static final UUID ALL_ZEROS_UUID = new UUID(0, 0);

  public DynamoDbGroupMembershipManager(final DynamoDbAsyncClient dynamoDbAsyncClient) {
    this.dynamoDbAsyncClient = dynamoDbAsyncClient;
  }

  @Override
  public CompletableFuture<Integer> getCount(String groupId) {
    QueryRequest queryRequest = QueryRequest.builder()
        .keyConditionExpression(
            "#group_id = :group_id AND #client_id_alive_ts_composite_key > :last_minute_ts_comp_key")
        .expressionAttributeNames(ImmutableMap.<String, String>builder()
            .put("#group_id", "group_id")
            .put("#client_id_alive_ts_composite_key", "client_id_alive_ts_composite_key")
            .build())
        .expressionAttributeValues(ImmutableMap.<String, AttributeValue>builder()
            .put("group_id", AttributeValue.builder().s(groupId).build())
            .put("last_minute_ts_comp_key", AttributeValue.builder()
                .s(String.format("%d|%s", System.currentTimeMillis(), ALL_ZEROS_UUID.toString()))
                .build())
            .build())
        .build();
    return this.dynamoDbAsyncClient.query(queryRequest)
        .thenApply(QueryResponse::count);
  }

  @Override
  public CompletableFuture<Void> update(String groupId, String memberId) {
    PutItemRequest putRequest = PutItemRequest.builder()
        .item(ImmutableMap.<String, AttributeValue>builder()
            .put("group_id", AttributeValue.builder().s(groupId).build())
            .put("client_id_alive_ts_composite_key", AttributeValue.builder()
                .s(String.format("%d|%s", System.currentTimeMillis(), memberId))
                .build())
            .build())
        .build();

    return this.dynamoDbAsyncClient.putItem(putRequest)
        .thenAccept(r -> {
        });
  }
}
