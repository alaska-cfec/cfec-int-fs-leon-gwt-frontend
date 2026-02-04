package com.cfecweb.leon.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.cfecweb.leon.dto.ArenewPermits;

import java.util.List;

@Mapper
/*
@Mapper(uses = {
        ArenewPermitsIdMapper.class,
        ArenewEntityMapper.class,
        ArenewPaymentMapper.class
})
*/
public interface ArenewPermitsMapper {
    ArenewPermitsMapper INSTANCE = Mappers.getMapper(ArenewPermitsMapper.class);

    ArenewPermits toDto(com.cfecweb.leon.shared.ArenewPermits shared);
    com.cfecweb.leon.shared.ArenewPermits toShared(ArenewPermits dto);

    List<ArenewPermits> toDtoList(List<com.cfecweb.leon.shared.ArenewPermits> sharedList);
    List<com.cfecweb.leon.shared.ArenewPermits> toSharedList(List<ArenewPermits> dtoList);
}
