package com.cfecweb.leon.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.cfecweb.leon.dto.ArenewEntityId;

@Mapper
public interface ArenewEntityIdMapper {
    ArenewEntityIdMapper INSTANCE = Mappers.getMapper(ArenewEntityIdMapper.class);

    ArenewEntityId toDto(com.cfecweb.leon.shared.ArenewEntityId shared);
    com.cfecweb.leon.shared.ArenewEntityId toShared(ArenewEntityId dto);
}