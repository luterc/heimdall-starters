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
