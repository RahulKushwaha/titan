package rk.projects.titan.commons.opentracing;

import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

public class Main {

  private final Tracer tracer;

  private Main(final Tracer tracer) {
    this.tracer = tracer;
  }

  public static void main(String[] args) {
    new Main(GlobalTracer.get()).sayHello("Rahul");
  }

  private void sayHello(String to) {
    Span span = this.tracer.buildSpan("say-hello").start();
    System.out.println(String.format("Hello, %s!!", to));

    span.finish();
  }
}
