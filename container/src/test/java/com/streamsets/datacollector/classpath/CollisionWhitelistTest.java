/*
 * Copyright 2017 StreamSets Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsets.datacollector.classpath;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CollisionWhitelistTest {

  private final static Dependency NETTY_2_9_8 = new Dependency("netty", "2.9.8");
  private final static Dependency NETTY_3_1_2 = new Dependency("netty", "3.1.2");
  private final static Dependency NETTY_3_5_3 = new Dependency("netty", "3.5.3");
  private final static Dependency NETTY_4_8_0 = new Dependency("netty", "4.8.0");

  @Test
  public void testNetty() {
    // Two different major versions should be whitelisted
    assertTrue(CollisionWhitelist.isWhitelisted("netty", ImmutableMap.of(
      "3.1.2", ImmutableList.of(NETTY_3_1_2),
      "4.8.0", ImmutableList.of(NETTY_4_8_0)
    )));

    // Different minor on the same major should be an error
    assertFalse(CollisionWhitelist.isWhitelisted("netty", ImmutableMap.of(
      "3.1.2", ImmutableList.of(NETTY_3_1_2),
      "3.5.3", ImmutableList.of(NETTY_3_5_3)
    )));

    // Major "2" is not whitelisted at all
    assertFalse(CollisionWhitelist.isWhitelisted("netty", ImmutableMap.of(
      "2.9.8", ImmutableList.of(NETTY_2_9_8),
      "3.5.3", ImmutableList.of(NETTY_3_5_3)
    )));

    // Two different minor are wrong regardless how many major versions are whitelisted
    assertFalse(CollisionWhitelist.isWhitelisted("netty", ImmutableMap.of(
      "3.1.2", ImmutableList.of(NETTY_3_1_2),
      "3.5.3", ImmutableList.of(NETTY_3_5_3),
      "4.8.0", ImmutableList.of(NETTY_4_8_0)
    )));
  }
}