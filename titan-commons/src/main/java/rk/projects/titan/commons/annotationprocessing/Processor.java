package rk.projects.titan.commons.annotationprocessing;

import com.google.auto.service.AutoService;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

@AutoService(Processor.class)
public class Processor extends AbstractProcessor {

  private final ToBuilderValidator toBuilderValidator = new ToBuilderValidator();
  private final BuilderGenerator builderGenerator = new BuilderGenerator();

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    // Iterate over all @Factory annotated elements
    for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(ToBuilder.class)) {
      this.toBuilderValidator.validate(annotatedElement);
      this.builderGenerator.generate((DeclaredType) annotatedElement.asType());
    }

    return false;
  }

  private void getAnnotatedClasses() {

  }
}
