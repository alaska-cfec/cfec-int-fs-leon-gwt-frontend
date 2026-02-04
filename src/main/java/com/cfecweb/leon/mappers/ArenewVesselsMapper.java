package com.cfecweb.leon.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.cfecweb.leon.dto.ArenewVessels;

import java.util.List;

@Mapper
/*
@Mapper(uses = {
    ArenewVesselsIdMapper.class,
    ArenewEntityMapper.class,
    ArenewPaymentMapper.class
})
*/
public interface ArenewVesselsMapper {
    ArenewVesselsMapper INSTANCE = Mappers.getMapper(ArenewVesselsMapper.class);

    ArenewVessels toDto(com.cfecweb.leon.shared.ArenewVessels shared);
    com.cfecweb.leon.shared.ArenewVessels toShared(ArenewVessels dto);

    List<ArenewVessels> toDtoList(List<com.cfecweb.leon.shared.ArenewVessels> sharedList);
    List<com.cfecweb.leon.shared.ArenewVessels> toSharedList(List<ArenewVessels> dtoList);
}
