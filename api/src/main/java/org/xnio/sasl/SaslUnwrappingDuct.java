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

package org.xnio.sasl;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.xnio.Buffers;
import org.xnio.ducts.AbstractMessageSinkDuct;
import org.xnio.ducts.MessageSinkDuct;
import org.xnio.sasl.SaslWrapper;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class SaslUnwrappingDuct extends AbstractMessageSinkDuct<MessageSinkDuct> implements MessageSinkDuct {
    private final SaslWrapper wrapper;
    private ByteBuffer buffer;

    public SaslUnwrappingDuct(final MessageSinkDuct next, final SaslWrapper wrapper) {
        super(next);
        this.wrapper = wrapper;
    }

    public int send(final ByteBuffer src) throws IOException {
        if (! doSend()) {
            return 0;
        }
        final int remaining = src.remaining();
        ByteBuffer wrapped = ByteBuffer.wrap(wrapper.unwrap(src));
        if (next.send(wrapped) == 0) {
            buffer = wrapped;
        }
        return remaining;
    }

    public long send(final ByteBuffer[] srcs, final int offs, final int len) throws IOException {
        if (! doSend()) {
            return 0;
        }
        final byte[] bytes = Buffers.take(srcs, offs, len);
        final ByteBuffer wrapped = ByteBuffer.wrap(wrapper.unwrap(bytes));
        if (next.send(wrapped) == 0) {
            this.buffer = wrapped;
        }
        return bytes.length;
    }

    private boolean doSend() throws IOException {
        final ByteBuffer buffer = this.buffer;
        if (buffer != null && next.send(buffer) != 0) {
            this.buffer = null;
            return true;
        }
        return false;
    }

    public boolean flush() throws IOException {
        return doSend() && next.flush();
    }
}
