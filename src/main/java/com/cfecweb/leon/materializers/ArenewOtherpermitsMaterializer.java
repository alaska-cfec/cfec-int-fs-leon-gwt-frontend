package com.cfecweb.leon.materializers;

import com.cfecweb.leon.mappers.ArenewOtherpermitsIdMapper;
import com.cfecweb.leon.shared.ArenewOtherpermits;
import com.cfecweb.leon.shared.ArenewOtherpermitsId;
import org.hibernate.Session;

public interface ArenewOtherpermitsMaterializer {
    static ArenewOtherpermits getArenewOtherpermitsById(Session session, com.cfecweb.leon.dto.ArenewOtherpermitsId id) {
        if (id == null) { return null; }
        return getArenewOtherpermitsById(session, ArenewOtherpermitsIdMapper.INSTANCE.toShared(id));
    }

    static ArenewOtherpermits getArenewOtherpermitsById(Session session, ArenewOtherpermitsId id) {
        if (id == null) { return null; }
        return session.get(ArenewOtherpermits.class, id);
    }

    static ArenewOtherpermits saveOrUpdate(Session session, ArenewOtherpermits update) {
        if (getArenewOtherpermitsById(session, update.getId()) != null) {
            return session.merge(update);
        } else {
            session.persist(update);
            return update;
        }
    }
}
