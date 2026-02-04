package com.cfecweb.leon.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.cfecweb.leon.dto.ArenewOtherpermitsId;

@Mapper
public interface ArenewOtherpermitsIdMapper {
    ArenewOtherpermitsIdMapper INSTANCE = Mappers.getMapper(ArenewOtherpermitsIdMapper.class);

    ArenewOtherpermitsId toDto(com.cfecweb.leon.shared.ArenewOtherpermitsId shared);
    com.cfecweb.leon.shared.ArenewOtherpermitsId toShared(ArenewOtherpermitsId dto);
}