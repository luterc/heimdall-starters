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

package com.luter.heimdall.starter.utils.exception;


public class LuterIllegalParameterException extends RuntimeException {

    public LuterIllegalParameterException(String message) {

        super(message);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private int code = 400;

    public LuterIllegalParameterException(int code) {
        this.code = code;
    }

    public LuterIllegalParameterException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public LuterIllegalParameterException(int code, String message) {
        super(message);
        this.code = code;
    }

    public LuterIllegalParameterException(Throwable throwable) {
        super(throwable);
    }

    public LuterIllegalParameterException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
