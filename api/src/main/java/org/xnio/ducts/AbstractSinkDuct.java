/*
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
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

package org.xnio.ducts;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.xnio.XnioExecutor;

/**
 * An abstract base class for output ducts.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public abstract class AbstractSinkDuct<D extends SinkDuct> extends AbstractDuct<D> implements SinkDuct {

    /**
     * Construct a new instance.
     *
     * @param next the delegate duct to set
     */
    protected AbstractSinkDuct(final D next) {
        super(next);
    }

    public void terminateWrites() throws IOException {
        next.terminateWrites();
    }

    public boolean isWriteShutdown() {
        return next.isWriteShutdown();
    }

    public void resumeWrites() {
        next.resumeWrites();
    }

    public void suspendWrites() {
        next.suspendWrites();
    }

    public void wakeupWrites() {
        next.wakeupWrites();
    }

    public boolean isWriteResumed() {
        return next.isWriteResumed();
    }

    public void awaitWritable() throws IOException {
        next.awaitWritable();
    }

    public void awaitWritable(final long time, final TimeUnit timeUnit) throws IOException {
        next.awaitWritable(time, timeUnit);
    }

    public XnioExecutor getWriteThread() {
        return next.getWriteThread();
    }

    public void setWriteReadyHandler(final WriteReadyHandler handler) {
        next.setWriteReadyHandler(handler);
    }

    public void truncateWrites() throws IOException {
        next.truncateWrites();
    }

    public boolean flush() throws IOException {
        return next.flush();
    }
}
