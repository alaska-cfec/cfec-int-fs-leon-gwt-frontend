package com.cfecweb.leon.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.cfecweb.leon.dto.GWTfisheryTable;

import java.util.List;

@Mapper
public interface GWTfisheryTableMapper {
    GWTfisheryTableMapper INSTANCE = Mappers.getMapper(GWTfisheryTableMapper.class);

    GWTfisheryTable toDto(com.cfecweb.leon.shared.GWTfisheryTable shared);
    com.cfecweb.leon.shared.GWTfisheryTable toShared(GWTfisheryTable dto);

    List<GWTfisheryTable> toDtoList(List<com.cfecweb.leon.shared.GWTfisheryTable> sharedList);
    List<com.cfecweb.leon.shared.GWTfisheryTable> toSharedList(List<GWTfisheryTable> dtoList);
}