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

package com.luter.heimdall.starter.model.mapper;

import java.util.List;

public interface BaseExtendMapper<D, E, V> {
    /////vo 与entity

    E voToEntity(V vo);

    V entityToVO(E entity);

    List<E> voListToEntityList(List<V> voList);

    List<V> entityListToVOList(List<E> entityList);

    /////vo 与 dto

    D voToDTO(V vo);

    V dtoToVO(D dto);

    List<D> voListToDTOList(List<V> voList);

    List<V> dtoListToVOList(List<D> dtoList);

    /////dto 与实体类

    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntity(List<D> dtoList);

    List<D> toDto(List<E> entityList);
}
