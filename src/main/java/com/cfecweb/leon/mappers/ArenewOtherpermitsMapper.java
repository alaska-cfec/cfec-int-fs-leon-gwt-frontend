package com.cfecweb.leon.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.cfecweb.leon.dto.ArenewOtherpermits;

@Mapper
/*
@Mapper(uses = {
    ArenewOtherpermitsIdMapper.class,
    ArenewEntityMapper.class
})
*/
public interface ArenewOtherpermitsMapper {
    ArenewOtherpermitsMapper INSTANCE = Mappers.getMapper(ArenewOtherpermitsMapper.class);

    ArenewOtherpermits toDto(com.cfecweb.leon.shared.ArenewOtherpermits shared);
    com.cfecweb.leon.shared.ArenewOtherpermits toShared(ArenewOtherpermits dto);
}
