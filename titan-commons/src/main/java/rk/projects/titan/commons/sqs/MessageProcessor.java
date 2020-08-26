package rk.projects.titan.commons.sqs;

import java.util.concurrent.CompletableFuture;
import software.amazon.awssdk.services.sqs.model.Message;

public interface MessageProcessor<T> {

  CompletableFuture<T> process(Message message);
}
