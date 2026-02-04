package com.cfecweb.leon.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.cfecweb.leon.dto.ArenewPermitsId;

@Mapper
public interface ArenewPermitsIdMapper {
    ArenewPermitsIdMapper INSTANCE = Mappers.getMapper(ArenewPermitsIdMapper.class);

    ArenewPermitsId toDto(com.cfecweb.leon.shared.ArenewPermitsId shared);
    com.cfecweb.leon.shared.ArenewPermitsId toShared(ArenewPermitsId dto);
}
