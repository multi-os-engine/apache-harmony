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

import java.lang.ref.WeakReference;

/**
 * A class that allows one to observe GCs.
 */
public class GcMarker {
  private Object marker;
  private WeakReference<Object> markerRef;

  public GcMarker() {
    reset();
  }

  private boolean isLive() {
    return markerRef.get() != null;
  }

  private void allowCollection() {
    marker = null;
  }

  private void reset() {
    marker = new Object();
    markerRef = new WeakReference<Object>(marker);
  }

  public void waitForGc() {
    allowCollection();
    // Requests GC and finalization of objects.
    do {
        try { Thread.sleep(10); } catch (Exception e) {}
        System.gc();
        System.runFinalization();
        System.gc();
    } while (isLive());
    reset();
  }
}
