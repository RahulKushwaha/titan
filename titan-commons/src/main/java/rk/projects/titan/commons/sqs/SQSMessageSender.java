package rk.projects.titan.commons.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import javax.inject.Inject;
import javax.inject.Named;

public class SQSMessageSender {

  private final AmazonSQS amazonSQS;
  private final String queueUrl;

  @Inject
  public SQSMessageSender(final AmazonSQS amazonSQS, @Named("queueUrl") final String queueUrl) {
    this.amazonSQS = amazonSQS;
    this.queueUrl = queueUrl;
  }

  public void send(String message) {
    SendMessageRequest sendMessageRequest = new SendMessageRequest();
    sendMessageRequest.setQueueUrl(this.queueUrl);
    sendMessageRequest.setMessageBody(message);
    this.amazonSQS.sendMessage(sendMessageRequest);
  }
}
