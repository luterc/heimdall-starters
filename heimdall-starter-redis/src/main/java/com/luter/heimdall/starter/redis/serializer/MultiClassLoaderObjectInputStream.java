/*
 *
 *  *
 *  *
 *  *      Copyright 2020-2021 Luter.me
 *  *
 *  *      Licensed under the Apache License, Version 2.0 (the "License");
 *  *      you may not use this file except in compliance with the License.
 *  *      You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *      Unless required by applicable law or agreed to in writing, software
 *  *      distributed under the License is distributed on an "AS IS" BASIS,
 *  *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *      See the License for the specific language governing permissions and
 *  *      limitations under the License.
 *  *
 *  *
 *
 */

package com.luter.heimdall.starter.redis.serializer;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

@Slf4j
public class MultiClassLoaderObjectInputStream extends ObjectInputStream {

    MultiClassLoaderObjectInputStream(InputStream str) throws IOException {
        super(str);
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String name = desc.getName();

        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            return Class.forName(name, false, cl);
        } catch (Throwable ex) {
            log.debug("Cannot access thread context ClassLoader!", ex);
        }

        try {
            ClassLoader cl = MultiClassLoaderObjectInputStream.class.getClassLoader();
            return Class.forName(name, false, cl);
        } catch (Throwable ex) {
            log.debug("Cannot access application ClassLoader", ex);
        }

        try {
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            return Class.forName(name, false, cl);
        } catch (Throwable ex) {
            log.debug("Cannot access system ClassLoader", ex);
        }

        return super.resolveClass(desc);
    }

}
