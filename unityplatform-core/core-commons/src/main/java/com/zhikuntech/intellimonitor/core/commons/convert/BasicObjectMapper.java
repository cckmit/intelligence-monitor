package com.zhikuntech.intellimonitor.core.commons.convert;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mappings;

import java.util.Collection;
import java.util.List;

/**
 * @author liukai
 */
public interface BasicObjectMapper<SOURCE, TARGET> {


    @Mappings({})
    @InheritConfiguration
    TARGET to(SOURCE source);

    @InheritConfiguration
    List<TARGET> to(Collection<SOURCE> sources);

    @InheritInverseConfiguration
    SOURCE from(TARGET target);

    @InheritInverseConfiguration
    List<SOURCE> from(Collection<TARGET> targets);

}
