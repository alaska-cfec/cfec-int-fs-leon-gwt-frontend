package com.cfecweb.leon.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.cfecweb.leon.dto.ArenewChanges;

import java.util.List;

@Mapper
/*
@Mapper(uses = {
    ArenewChangesIdMapper.class,
    ArenewEntityMapper.class,
    ArenewPaymentMapper.class
})
*/
public interface ArenewChangesMapper {
    ArenewChangesMapper INSTANCE = Mappers.getMapper(ArenewChangesMapper.class);

    ArenewChanges toDto(com.cfecweb.leon.shared.ArenewChanges shared);
    com.cfecweb.leon.shared.ArenewChanges toShared(ArenewChanges dto);

    List<ArenewChanges> toDtoList(List<com.cfecweb.leon.shared.ArenewChanges> sharedList);
    List<com.cfecweb.leon.shared.ArenewChanges> toSharedList(List<ArenewChanges> dtoList);

    default List<com.cfecweb.leon.shared.ArenewChanges> toSharedListWithEntity(List<ArenewChanges> dtoList, com.cfecweb.leon.shared.ArenewEntity sEnt) {
        List<com.cfecweb.leon.shared.ArenewChanges> changes = toSharedList(dtoList);
        for (com.cfecweb.leon.shared.ArenewChanges change : changes) {
            change.setArenewEntity(sEnt);
        }
        return changes;
    }
}

