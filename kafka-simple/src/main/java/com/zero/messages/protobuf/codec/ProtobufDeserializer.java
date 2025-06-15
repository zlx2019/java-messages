package com.zero.messages.protobuf.codec;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import org.apache.kafka.common.serialization.Deserializer;

/**
 * @author Zero.
 * <p> Created on 2025/6/10 14:55 </p>
 */
@SuppressWarnings("all")
public class ProtobufDeserializer<T extends MessageLite> implements Deserializer<T> {
    private final T prototype;

    public ProtobufDeserializer(T prototype) {
        this.prototype = prototype;
    }

    @Override
    public T deserialize(String s, byte[] bytes) {
        try {
            return (T)prototype.getParserForType().parseFrom(bytes);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException( prototype.getClass().getName() + "deserializer fail", e);
        }
    }


//    @Override
//    public Message.EasyMessage deserialize(String s, byte[] bytes) {
//        try {
//            return Message.EasyMessage.parseFrom(bytes);
//        } catch (InvalidProtocolBufferException e) {
//            throw new RuntimeException("EasyMessage deserializer", e);
//        }
//    }
}
