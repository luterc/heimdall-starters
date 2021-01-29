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

package com.luter.heimdall.starter.jpa.converters.sample;

import com.luter.heimdall.starter.jpa.converters.AbstractEnumConverter;
import com.luter.heimdall.starter.jpa.converters.IBaseEnum;
import com.luter.heimdall.starter.jpa.converters.PersistableEnum;
import com.luter.heimdall.starter.utils.exception.LuterIllegalParameterException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;


@Getter
@AllArgsConstructor
public enum GenderEnum implements PersistableEnum<Integer>, IBaseEnum {


    MALE(1, "男"),

    FEMALE(2, "女");

    private final int value;
    private final String label;

    @Override
    public Integer getData() {
        return value;
    }

    public String getName() {
        return label;
    }

    public static GenderEnum valueOf(int value) {
        for (GenderEnum statusEnum : GenderEnum.values()) {
            if (Objects.equals(value, statusEnum.getValue())) {
                return statusEnum;
            }
        }
        throw new LuterIllegalParameterException("性别参数错误,请检查");
    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    public int value() {
        return value;
    }

    @Override
    public String label() {
        return label;
    }

    public static class Converter extends AbstractEnumConverter<GenderEnum, Integer> {
        public Converter() {
            super(GenderEnum.class);
        }
    }
}
