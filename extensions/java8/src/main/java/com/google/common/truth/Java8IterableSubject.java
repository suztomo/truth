package com.google.common.truth;


import java.util.function.Predicate;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class Java8IterableSubject<T> extends IterableSubject {
  private final Iterable<T> actual;

  protected Java8IterableSubject(FailureMetadata metadata, @NullableDecl Iterable<T> iterable) {
    super(metadata, iterable);
    actual = iterable;
  }

  public static <U> Subject.Factory<Java8IterableSubject<U>, Iterable<U>> iterables() {
    return (metadata, subject) -> new Java8IterableSubject(metadata, subject);
  }

  public final void anyMatch(@NullableDecl Predicate<T> predicate, String itemDescription) {
    boolean found = false;
    for (T item : actual) {
      if (predicate.test(item)) {
        found = true;
        break;
      }
    }
    if (!found) {
      failWithActual("expected to contain", itemDescription);
    }
  }
}
