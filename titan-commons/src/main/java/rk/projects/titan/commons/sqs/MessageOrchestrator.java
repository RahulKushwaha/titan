package rk.projects.titan.commons.sqs;

import java.util.UUID;
import java.util.function.Supplier;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class MessageOrchestrator {

  private static final Logger logger = LoggerFactory.getLogger(MessageOrchestrator.class);

  private final SQSMessageSender sqsMessageSender;

  @Inject
  public MessageOrchestrator(SQSMessageSender sqsMessageSender) {
    this.sqsMessageSender = sqsMessageSender;
  }

  @PostConstruct
  public void start() {
    logger.info("Message Sender Started");
    Flux.just((Supplier<String>) () -> UUID.randomUUID().toString())
        .publishOn(Schedulers.newParallel("message-sender-pool-1", 10))
        .map(Supplier::get)
        .doOnNext(message -> {
          this.sqsMessageSender.send(message);
          System.out.println("Message Send: " + message);
        })
        //.delaySequence(Duration.ofSeconds(1))
        .repeat()
        .subscribe();
  }
}
