package rk.projects.titan.commons.annotationprocessing;

import com.google.common.base.Preconditions;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;

public class ToBuilderValidator {

  public void validate(Element element) {
    Preconditions.checkNotNull(element);

    // Only classes can be annotated with the Builder Annotation.
    if (element.getKind() != ElementKind.CLASS) {
      throw new InvalidTargetForAnnotation();
    }

    // Only public classes can be annotated.
    if (!element.getModifiers().contains(Modifier.PUBLIC)) {
      throw new InvalidTargetForAnnotation();
    }

    // Only abstract classes can be annotated.
    if (!element.getModifiers().contains(Modifier.ABSTRACT)) {
      throw new InvalidTargetForAnnotation();
    }
  }
}
