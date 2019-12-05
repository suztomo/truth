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
import java.math.BigInteger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for Java 8 IterableSubject */
@RunWith(JUnit4.class)
public final class Java8IterableSubjectTest {

  @Test
  public void testAnyMatchSuccess() throws Exception {
    ImmutableList<BigInteger> list =
        ImmutableList.of(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3));

    Truth8.assertThat(list).anyMatch(item -> item.bitCount() > 1, "item.bigCount > 1");
  }

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
