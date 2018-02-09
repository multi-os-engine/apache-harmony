/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.harmony.jpda.tests.share;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * A class that allows one to observe GCs and finalization.
 */
public class GcMarker {

  private static int mCount = 0;

  /**
   * Sentinel object with explicit finalizer
   * to avoid optimizations around empty finalizer.
   */
  private static class Sentinel {
    Sentinel() { mCount++; }
    protected void finalize() { mCount--; }
  }

  private final ReferenceQueue mQueue;

  private Sentinel marker;
  private PhantomReference<Sentinel> markerRef;

  public GcMarker() {
    mQueue = new ReferenceQueue();
    reset();
  }

  private boolean isLive() {
    // Pedantically check mCount as well once the phantom reference
    // link is enqueued to make sure we have really finalized (since
    // there is only a single finalizer thread, this means all other
    // finalizers are finished too).
    return !markerRef.isEnqueued() || mCount > 0;
  }

  private void allowCollection() {
    marker = null;
  }

  private void reset() {
    marker = new Sentinel();
    markerRef = new PhantomReference<Sentinel>(marker, mQueue);
  }

  public void waitForGc() {
    // Start with a full collection to discourage minor collections in the middle of this.
    // Minor collections could cause us to collect the Sentinel but not older objects.
    Runtime.getRuntime().gc();

    // Release the sentinel.
    allowCollection();

    // Another collection.
    Runtime.getRuntime().gc();

    // Request finalization of objects, and subsequent reference enqueueing.
    do {
        System.runFinalization();
        Runtime.getRuntime().gc();
        try { Thread.sleep(10); } catch (Exception e) {}
    } while (isLive());

    // Remove the phantom reference from the queue and reset the only
    // remaining reference to the phantom reference to break the
    // sentinel's "phantom reachability".
    try { mQueue.remove(); } catch (Exception e) {}
    markerRef = null;

    // Prepare for the next waitForGc().
    reset();
  }
}
