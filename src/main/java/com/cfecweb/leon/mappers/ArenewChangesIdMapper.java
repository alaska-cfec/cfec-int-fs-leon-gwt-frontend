package com.cfecweb.leon.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.cfecweb.leon.dto.ArenewChangesId;

@Mapper
public interface ArenewChangesIdMapper {
    ArenewChangesIdMapper INSTANCE = Mappers.getMapper(ArenewChangesIdMapper.class);

    ArenewChangesId toDto(com.cfecweb.leon.shared.ArenewChangesId shared);
    com.cfecweb.leon.shared.ArenewChangesId toShared(ArenewChangesId dto);
}