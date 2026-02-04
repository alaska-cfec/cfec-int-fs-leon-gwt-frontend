package com.cfecweb.leon.materializers;

import com.cfecweb.leon.mappers.ArenewChangesIdMapper;
import com.cfecweb.leon.mappers.ArenewChangesMapper;
import com.cfecweb.leon.shared.ArenewChanges;
import com.cfecweb.leon.shared.ArenewChangesId;
import com.cfecweb.leon.shared.ArenewEntity;
import org.hibernate.Session;

import java.util.Collection;
import java.util.List;

public interface ArenewChangesMaterializer {
    static ArenewChanges getArenewChangesById(Session session, com.cfecweb.leon.dto.ArenewChangesId id) {
        if (id == null) { return null; }
        return getArenewChangesById(session, ArenewChangesIdMapper.INSTANCE.toShared(id));
    }

    static ArenewChanges getArenewChangesById(Session session, ArenewChangesId id) {
        if (id == null) { return null; }
        return session.get(ArenewChanges.class, id);
    }

    static List<ArenewChanges> materializeListWithEntity(Session session, Collection<com.cfecweb.leon.dto.ArenewChanges> dtoChg, ArenewEntity entity) {
        return dtoChg.stream()
                .map(c -> materialize(session, c))
                .peek(c -> { if (entity != null) { c.setArenewEntity(entity); } })
                .toList();
    }

    static ArenewChanges materialize(Session session, com.cfecweb.leon.dto.ArenewChanges chg) {
        ArenewChanges arenewChanges = getArenewChangesById(session, chg.getId());
        if (arenewChanges != null) {
            arenewChanges.setType(chg.getType());
            arenewChanges.setOldvalue(chg.getOldvalue());
            arenewChanges.setNewvalue(chg.getNewvalue());
            arenewChanges.setUpdatedate(chg.getUpdatedate());
            arenewChanges.setObject(chg.getObject());
            return arenewChanges;
        } else {
            ArenewChanges updated = ArenewChangesMapper.INSTANCE.toShared(chg);
            return updated;
        }
    }

    static ArenewChanges saveOrUpdate(Session session, ArenewChanges update) {
        if (getArenewChangesById(session, update.getId()) != null) {
            return session.merge(update);
        } else {
            session.persist(update);
            return update;
        }
    }
}
