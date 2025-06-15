package com.zero.messages.protobuf.codec;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import org.apache.kafka.common.serialization.Serializer;

/**
 * @author Zero.
 * <p> Created on 2025/6/10 14:54 </p>
 */
public class ProtobufSerializer implements Serializer<MessageLiteOrBuilder> {
    @Override
    public byte[] serialize(String s, MessageLiteOrBuilder message) {
        if (message instanceof MessageLite messageLite) {
            return messageLite.toByteArray();
        }else if (message instanceof MessageLite.Builder builder) {
            return builder.build().toByteArray();
        }
        throw new RuntimeException("Unexpected message type: " + message.getClass());
    }
}
