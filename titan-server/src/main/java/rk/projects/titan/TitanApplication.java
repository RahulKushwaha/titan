package rk.projects.titan;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import rk.projects.titan.commons.sqs.MessageBinder;
import rk.projects.titan.commons.sqs.MessageOrchestrator;
import rk.projects.titan.commons.sqs.SQSMessageReceiverAsync;
import rk.projects.titan.commons.sqs.SQSMessageSender;
import rk.projects.titan.di.factory.FactoryBinder;
import rk.projects.titan.resource.TestResource;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

/**
 * Main entry point to the titan  API server.
 */
public final class TitanApplication extends Application<TitanConfiguration> {

  public static void main(final String[] args) throws Exception {
    new TitanApplication().run(args);
  }

  @Override
  public void initialize(Bootstrap<TitanConfiguration> bootstrap) {

  }

  @Override
  public void run(final TitanConfiguration configuration, final Environment environment) {
//    environment.jersey().register(new AbstractBinder() {
//      @Override
//      protected void configure() {
//        this.bind(ManagedObject.class).to(ManagedObject.class).in(Singleton.class);
//      }
//    });

    environment.jersey().register(new FactoryBinder());
    environment.jersey().register(new MessageBinder());
    environment.jersey().register(ImmediateFeature.class);
    environment.jersey().register(TestResource.class);

    String queueUrl = "https://sqs.us-east-1.amazonaws.com/735354846484/integration_userserviceasync_internal_messaging";
    AmazonSQS amazonSQS = AmazonSQSClientBuilder.defaultClient();
//    SQSMessageSender sqsMessageSender = new SQSMessageSender(amazonSQS, queueUrl);
//    MessageOrchestrator messageOrchestrator = new MessageOrchestrator(sqsMessageSender);
//    messageOrchestrator.start();

//    SQSMessageReceiver sqsMessageReceiver = new SQSMessageReceiver(amazonSQS, queueUrl);
//    sqsMessageReceiver.receive()
//        .subscribeOn(Schedulers.newParallel("message-receiver-pool1=", 1))
//        .subscribe();
    SqsAsyncClient sqsAsyncClient = SqsAsyncClient.builder().build();
    SQSMessageReceiverAsync sqsMessageReceiverAsync = new SQSMessageReceiverAsync(sqsAsyncClient,
        queueUrl);
    sqsMessageReceiverAsync.start();
  }
}
