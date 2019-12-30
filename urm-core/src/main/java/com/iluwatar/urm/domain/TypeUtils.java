package com.iluwatar.urm.domain;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by moe on 10.08.16.
 */
class TypeUtils {

  private static final String JAVA_IDENTIFIER_REGEX = "([a-zA-Z_$][a-zA-Z\\d_$]*)";

  static Visibility getVisibility(int mod) {
    if (Modifier.isPublic(mod)) {
      return Visibility.PUBLIC;
    } else if (Modifier.isProtected(mod)) {
      return Visibility.PROTECTED;
    } else if (Modifier.isPrivate(mod)) {
      return Visibility.PRIVATE;
    } else {
      return Visibility.DEFAULT;
    }
  }

  static String getSimpleName(Type type) {
    if (type instanceof ParameterizedType) {
      return getSimpleTypeName(((ParameterizedType) type));
    } else if (type instanceof Class) {
      return getSimpleTypeName(((Class) type));
    } else if (type instanceof TypeVariable) {
      return getSimpleTypeName(((TypeVariable) type));
    } else {
      return cutPackages(type.getTypeName());
    }
  }

  @SuppressWarnings("StatementWithEmptyBody") // intellij idea
  private static String getSimpleTypeName(TypeVariable type) {
    StringBuilder sb = new StringBuilder();
    sb.append(type.getTypeName());

    if (type.getBounds() == null || type.getBounds().length <= 0) {
      return sb.toString();
    }

    String boundsString = Arrays.stream(type.getBounds())
        .filter(t -> !t.getTypeName().equals("java.lang.Object"))
        .map(t -> getSimpleTypeNameWithoutTypeVariable(t, type))
        .collect(Collectors.joining(" & "));
    if (boundsString != null && boundsString.trim().length() > 0) {
      sb.append(" extends ");
      sb.append(boundsString);
    }
    return sb.toString();
  }

  static String getSimpleTypeName(ParameterizedType type) {
    StringBuilder sb = new StringBuilder();

    if (type.getOwnerType() != null) {
      if (type.getOwnerType() instanceof Class) {
        sb.append(((Class) type.getOwnerType()).getSimpleName());
      } else {
        sb.append(cutPackages(type.getOwnerType().getTypeName()));
      }
      sb.append(".");
      sb.append(((Class) type.getRawType()).getSimpleName());
    } else {
      sb.append(((Class) type.getRawType()).getSimpleName());
    }

    Type[] actualTypeArguments = type.getActualTypeArguments();
    if (actualTypeArguments != null && actualTypeArguments.length > 0) {
      sb.append(getTypeParameters(type, actualTypeArguments));
    }

    return sb.toString();
  }

  static String getSimpleTypeName(Class type) {
    StringBuilder sb = new StringBuilder();

    sb.append(type.getSimpleName());
    TypeVariable[] typeParameters = type.getTypeParameters();
    if (typeParameters != null && typeParameters.length > 0) {
      sb.append(getTypeParameters(type, typeParameters));
    }

    return sb.toString();
  }

  private static String getTypeParameters(Type declaringType, Type[] typeParameters) {
    return Arrays.stream(typeParameters)
        .filter(t -> !t.getTypeName().equals(declaringType.getTypeName()))
        .map(TypeUtils::getSimpleName)
        .collect(Collectors.joining(", ", "<", ">"));
  }

  private static String getSimpleTypeNameWithoutTypeVariable(Type t, TypeVariable type) {
    if (t instanceof TypeVariable) {
      // If this is a TypeVariable again, it must have been defined in the
      // previously scanned classes, so we don't call getSimpleName on it
      // recursively to not have any duplicate definitions
      return t.getTypeName();
    } else if (t instanceof ParameterizedType) {
      if (((ParameterizedType) t).getRawType().equals(type.getGenericDeclaration())) {
        // block self (and endless) referencing of a class
        return ((Class) ((ParameterizedType) t).getRawType()).getSimpleName();
      } else {
        return cutPackages(t.getTypeName());
      }
    } else if (t instanceof Class) {
      return ((Class) t).getSimpleName();
    }
    return getSimpleName(t);
  }

  static String cutPackages(String parameter) {
    return parameter.replaceAll(JAVA_IDENTIFIER_REGEX + "\\.", "");
  }

}
