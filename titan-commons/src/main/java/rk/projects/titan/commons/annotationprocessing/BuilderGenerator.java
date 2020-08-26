package rk.projects.titan.commons.annotationprocessing;

import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

public class BuilderGenerator {

  public void generate(DeclaredType classTypeMirror) {
    // Get Constructor Parameters
    ImmutableList<VariableElement> constructorParameters = this.getConstructorParameters(
        classTypeMirror.asElement());

    ImmutableList<MethodSpec> getters = constructorParameters.stream()
        .map(i -> this.getter(i.getSimpleName().toString(), i.asType()))
        .collect(ImmutableList.toImmutableList());

    TypeSpec classType = TypeSpec.classBuilder(
        classTypeMirror.asElement().getSimpleName().toString())
        .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
        .addMethods(getters)
        .build();

    System.out.println(classType.toString());
  }

  public MethodSpec getter(String name, TypeMirror returnType) {
    return MethodSpec.methodBuilder(name)
        .returns(returnType.getClass())
        .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
        .build();
  }

  private ImmutableList<VariableElement> getConstructorParameters(Element element) {
    for (Element enclosed : element.getEnclosedElements()) {
      if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
        ExecutableElement executableElement = (ExecutableElement) enclosed;
        return ImmutableList.copyOf(executableElement.getParameters());
      }
    }

    return ImmutableList.of();
  }
}
