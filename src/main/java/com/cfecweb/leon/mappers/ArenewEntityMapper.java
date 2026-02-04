package com.cfecweb.leon.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.cfecweb.leon.dto.ArenewEntity;

@Mapper
/*
@Mapper(uses ={
    ArenewEntityIdMapper.class,
    ArenewVesselsMapper.class,
    ArenewPermitsMapper.class,
    ArenewPaymentMapper.class,
    ArenewChangesMapper.class,
    ArenewOtherpermitsMapper.class
})
*/
public interface ArenewEntityMapper {
    ArenewEntityMapper INSTANCE = Mappers.getMapper(ArenewEntityMapper.class);

    ArenewEntity toDto(com.cfecweb.leon.shared.ArenewEntity shared);
    com.cfecweb.leon.shared.ArenewEntity toShared(ArenewEntity dto);
}
