package com.cfecweb.leon.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.cfecweb.leon.dto.ArenewPayment;

@Mapper
/*
@Mapper(uses = {
    ArenewEntityMapper.class,
    ArenewVesselsMapper.class,
    ArenewPermitsMapper.class,
    ArenewChangesMapper.class
})
*/
public interface ArenewPaymentMapper {
    ArenewPaymentMapper INSTANCE = Mappers.getMapper(ArenewPaymentMapper.class);

    ArenewPayment toDto(com.cfecweb.leon.shared.ArenewPayment shared);
    com.cfecweb.leon.shared.ArenewPayment toShared(ArenewPayment dto);
}
