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

package com.luter.heimdall.starter.jpa.converters;

import javax.persistence.AttributeConverter;

public abstract class AbstractEnumConverter<ATTR extends Enum<ATTR> & PersistableEnum<DB>, DB> implements AttributeConverter<ATTR, DB> {

    private final Class<ATTR> clazz;

    public AbstractEnumConverter(Class<ATTR> clazz) {
        this.clazz = clazz;
    }

    @Override
    public DB convertToDatabaseColumn(ATTR attribute) {
        return attribute != null ? attribute.getData() : null;
    }

    @Override
    public ATTR convertToEntityAttribute(DB dbData) {
        if (dbData == null) {
            return null;
        }

        ATTR[] enums = clazz.getEnumConstants();

        for (ATTR e : enums) {
            if (e.getData().equals(dbData)) {
                return e;
            }
        }

        throw new UnsupportedOperationException("枚举转化异常。枚举【" + clazz.getSimpleName() + "】,数据库库中的值为：【" + dbData + "】");
    }

}
