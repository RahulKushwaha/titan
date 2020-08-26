package rk.projects.titan.commons.sqs;

public class Builder<T> {

  private MessageProcessor<T> messageProcessor;
  private MessageAcknowledger<T> messageAcknowledger;

  public Builder<T> withMessageProcessor(MessageProcessor<T> messageProcessor) {
    this.messageProcessor = messageProcessor;
    return this;
  }

  public Builder<T> withMessageAcknowledger(MessageAcknowledger<T> messageAcknowledger) {
    this.messageAcknowledger = messageAcknowledger;
    return this;
  }
}
