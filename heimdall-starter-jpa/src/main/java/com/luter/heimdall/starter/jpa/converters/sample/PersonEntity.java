package com.luter.heimdall.starter.jpa.converters.sample;

import javax.persistence.Convert;

public class PersonEntity {

    @Convert(converter = GenderEnum.Converter.class)
    private GenderEnum gender;
}
