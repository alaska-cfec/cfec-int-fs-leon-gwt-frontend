package com.cfecweb.leon.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.cfecweb.leon.dto.ArenewVesselsId;

@Mapper
public interface ArenewVesselsIdMapper {
    ArenewVesselsIdMapper INSTANCE = Mappers.getMapper(ArenewVesselsIdMapper.class);

    ArenewVesselsId toDto(com.cfecweb.leon.shared.ArenewVesselsId shared);
    com.cfecweb.leon.shared.ArenewVesselsId toShared(ArenewVesselsId dto);
}