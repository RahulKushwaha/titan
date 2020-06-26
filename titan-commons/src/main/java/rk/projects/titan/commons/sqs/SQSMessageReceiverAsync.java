package rk.projects.titan.commons.sqs;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

public class SQSMessageReceiverAsync {

  private final SqsAsyncClient sqsAsyncClient;
  private final String queueUrl;
  private Supplier<CompletableFuture<ReceiveMessageResponse>> messageConsumer;

  public SQSMessageReceiverAsync(final SqsAsyncClient sqsAsyncClient, String queueUrl) {
    this.sqsAsyncClient = sqsAsyncClient;
    this.queueUrl = queueUrl;
    messageConsumer = () -> {
      System.out.println("Calling SQS for Messages");
      return this.sqsAsyncClient.receiveMessage(ReceiveMessageRequest.builder()
          .queueUrl(queueUrl).maxNumberOfMessages(10).build());
    };
  }

  public void start() {
    Mono.defer(() -> Mono.<ReceiveMessageResponse>fromFuture(messageConsumer.get()))
        .repeat()
        .filter(ReceiveMessageResponse::hasMessages)
        .map(ReceiveMessageResponse::messages)
        .flatMapIterable(Function.identity())
        .doOnNext(message -> {
          System.out.println("Received: " + message.body());
          SQSMessageReceiverAsync.this.sqsAsyncClient.deleteMessage(
              DeleteMessageRequest.builder()
                  .queueUrl(SQSMessageReceiverAsync.this.queueUrl)
                  .receiptHandle(message.receiptHandle())
                  .build());
        })
        .subscribe();
  }
}
