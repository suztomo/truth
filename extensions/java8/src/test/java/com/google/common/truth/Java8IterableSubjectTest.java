/*
 * Copyright (c) 2016 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.common.truth;

import static com.google.common.truth.Java8IterableSubject.iterables;
import static com.google.common.truth.Truth8.assertThat;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for Java 8 IterableSubject */
@RunWith(JUnit4.class)
public final class Java8IterableSubjectTest {

  private static ImmutableList<BigInteger> bigIntegers =
      ImmutableList.of(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3));

  @Test
  public void testAnyMatchSuccess() throws Exception {
    Truth8.assertThat(bigIntegers).anyMatch(item -> item.bitCount() > 1, "item.bigCount > 1");
  }

  public static final Predicate<Object> predicateOnObject = item -> item.hashCode() != 0;

  @Test
  public void testAnyMatchPredicateOnSuperClass() throws Exception {
    // anyMatch's first argument Predicate<? super T> allows this.
    Truth8.assertThat(bigIntegers).anyMatch(predicateOnObject, "item.hashCode != 0");
  }

  @Test
  public void testCartesianProduct_unrelatedTypes() {
    // From https://github.com/google/truth/issues/192#issuecomment-338227336, which seems to come
    // from https://github.com/google/truth/issues/54
    Set<Integer> x = ImmutableSet.of(1, 2);
    Set<String> y = ImmutableSet.of("3", "4");

    List<Object> exp1 = ImmutableList.of(1, "3");
    List<Object> exp2 = ImmutableList.of(1, "4");
    List<Object> exp3 = ImmutableList.of(2, "3");
    List<Object> exp4 = ImmutableList.of(2, "4");

    // ContainsAtLeast does not have type parameters. That's fine.
    Truth8.assertThat(Sets.cartesianProduct(x, y)).containsAtLeast(exp1, exp2, exp3, exp4);
  }

  @Test
  public void testCorrespondence() {
    // comparingElementsUsing cannot be overridden because of difference in type parameter.
    Truth8.assertThat(bigIntegers)
        .comparingWith(Correspondence.transforming(item -> item.bitCount(), "has bitcount"))
        .contains(2);
  }

  @Ignore
  @Test
  public void testAnyMatchFail() throws Exception {
    // This test is incomplete and thus fails. Setting correct failure message is not easy.
    ImmutableList<BigInteger> list =
        ImmutableList.of(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3));

    AssertionError expected =
        expectFailure(
            whenTesting -> {
              @SuppressWarnings("unchecked")
              Java8IterableSubject<BigInteger> subject = whenTesting.that((Iterable) list);
              subject.anyMatch(item -> item.bitCount() > 3, "item.bitCount > 3");
            });
    ExpectFailure.assertThat(expected)
        .factKeys()
        .containsExactly("expected to contain: item.bitCount > 3");

    assertThat(list).anyMatch(item -> item.bitCount() > 3, "item.bitCount > 3");
  }

  private static <T> AssertionError expectFailure(
      ExpectFailure.SimpleSubjectBuilderCallback<Java8IterableSubject<T>, Iterable<T>>
          assertionCallback) {
    return ExpectFailure.expectFailureAbout(iterables(), assertionCallback);
  }
}
