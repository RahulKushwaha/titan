package rk.projects.titan.commons.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Schedulers;

public class SQSMessageReceiver {

  private final AmazonSQS amazonSQS;
  private final ReceiveMessageRequest receiveMessageRequest;
  private final String queueUrl;

  private final static Logger logger = LoggerFactory.getLogger(SQSMessageReceiver.class);

  @Inject
  public SQSMessageReceiver(final AmazonSQS amazonSQS, @Named("queueUrl") final String queueUrl) {
    this.amazonSQS = amazonSQS;
    this.queueUrl = queueUrl;
    this.receiveMessageRequest = new ReceiveMessageRequest()
        .withMaxNumberOfMessages(10)
        .withWaitTimeSeconds(10)
        .withQueueUrl(queueUrl);
  }


  @PostConstruct
  public Flux<Message> receive() {
    BiFunction<State, SynchronousSink<List<Message>>, State> generator = (state, sink) -> {
      if (System.currentTimeMillis() - state.ts.get() > 1000) {
        List<Message> messages = SQSMessageReceiver.this.amazonSQS
            .receiveMessage(SQSMessageReceiver.this.receiveMessageRequest)
            .getMessages();

        sink.next(messages);
        System.out.println(Thread.currentThread().getName() + " Fetching message from Source");

        state.ts.set(System.currentTimeMillis());
        return state;
      }

      System.out.println(Thread.currentThread().getName() + " Returning Empty");
      sink.next(Collections.emptyList());
      return state;
    };
//    return Flux.<List<Message>>generate(new Consumer<SynchronousSink<List<Message>>>() {
//      @Override
//      public void accept(SynchronousSink<List<Message>> messageSynchronousSink) {
//        List<Message> messages = SQSMessageReceiver.this.amazonSQS
//            .receiveMessage(SQSMessageReceiver.this.receiveMessageRequest)
//            .getMessages();
//
//        messageSynchronousSink.next(messages);
//        System.out.println(Thread.currentThread().getName() + " Fetching message from Source.");
//      }
//    })

    return Flux.generate(State::new, generator)
        .flatMapIterable(messages -> messages)
        .doOnError(e -> {
          System.out.println(e);
        })
        .doOnNext(message -> {
          System.out.println(
              Thread.currentThread().getName() + " Message Received: " + message.getBody());
          SQSMessageReceiver.this.amazonSQS.deleteMessage(SQSMessageReceiver.this.queueUrl,
              message.getReceiptHandle());
        })
        .publishOn(Schedulers.newParallel("message-publisher-pool-3z", 10))
        .doOnComplete(() -> {
          System.out.println("Sink Completed.");
        });
  }

  private static final class State {

    public final AtomicLong ts = new AtomicLong(0);
  }
}
