/*
 *
 *  *
 *  *  *    Copyright 2020-2021 Luter.me
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *    See the License for the specific language governing permissions and
 *  *  *    limitations under the License.
 *  *
 *
 */

package com.luter.heimdall.starter.redis.serializer;

import org.springframework.data.redis.serializer.SerializationException;

import java.io.*;

public class ObjectSerializer implements RedisSerializer<Object> {

    public static final int BYTE_ARRAY_OUTPUT_STREAM_SIZE = 128;

    @Override
    public byte[] serialize(Object object) throws SerializationException {
        byte[] result = new byte[0];

        if (object == null) {
            return result;
        }
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(BYTE_ARRAY_OUTPUT_STREAM_SIZE);
        if (!(object instanceof Serializable)) {
            throw new SerializationException("requires a Serializable payload "
                    + "but received an object of type [" + object.getClass().getName() + "]");
        }
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            result = byteStream.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("serialize error, object=" + object, e);
        }

        return result;
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        Object result = null;

        if (bytes == null || bytes.length == 0) {
            return result;
        }

        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new MultiClassLoaderObjectInputStream(byteStream);
            result = objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializationException("deserialize error", e);
        }

        return result;
    }
}
