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
