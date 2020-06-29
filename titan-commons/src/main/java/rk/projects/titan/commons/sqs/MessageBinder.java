package rk.projects.titan.commons.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.glassfish.hk2.api.Immediate;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class MessageBinder extends AbstractBinder {

  /**
   * Implement to provide binding definitions using the exposed binding
   * methods.
   */
  @Override
  protected void configure() {
    this.bind(
        "https://sqs.us-east-1.amazonaws.com/735354846484/integration_userserviceasync_internal_messaging")
        .to(String.class)
        .named("queueUrl");

    this.bind(AmazonSQSClientBuilder.defaultClient())
        .to(AmazonSQS.class);

    this.bind(MessageOrchestrator.class)
        .to(MessageOrchestrator.class)
        .to(Immediate.class);

    this.bind(SQSMessageReceiver.class)
        .to(SQSMessageReceiver.class)
        .to(Immediate.class);
  }
}
