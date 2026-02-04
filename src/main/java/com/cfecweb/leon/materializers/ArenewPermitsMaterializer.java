package com.cfecweb.leon.materializers;

import com.cfecweb.leon.mappers.ArenewPermitsIdMapper;
import com.cfecweb.leon.mappers.ArenewPermitsMapper;
import com.cfecweb.leon.shared.ArenewPermits;
import com.cfecweb.leon.shared.ArenewPermitsId;
import org.hibernate.Session;

import java.util.Collection;
import java.util.List;

public interface ArenewPermitsMaterializer {
    static ArenewPermits getArenewPermitsById(Session session, com.cfecweb.leon.dto.ArenewPermitsId id) {
        if (id == null) { return null; }
        return getArenewPermitsById(session, ArenewPermitsIdMapper.INSTANCE.toShared(id));
    }

    static ArenewPermits getArenewPermitsById(Session session, ArenewPermitsId id) {
        if (id == null) { return null; }
        return session.get(ArenewPermits.class, id);
    }

    static List<ArenewPermits> materializeList(Session session, Collection<com.cfecweb.leon.dto.ArenewPermits> dtoPlist) {
        return dtoPlist.stream().map(p -> materialize(session, p)).toList();
    }

    static ArenewPermits materialize(Session session, com.cfecweb.leon.dto.ArenewPermits permit) {
        ArenewPermits arenewPermits = getArenewPermitsById(session, permit.getId());
        if (arenewPermits != null) {
            arenewPermits.setAdfg(permit.getAdfg());
            arenewPermits.setMsna(permit.getMsna());
            arenewPermits.setReceiveddate(permit.getReceiveddate());
            arenewPermits.setDescription(permit.getDescription());
            arenewPermits.setStatus(permit.getStatus());
            arenewPermits.setType(permit.getType());
            arenewPermits.setLastupdated(permit.getLastupdated());
            arenewPermits.setOfee(permit.getOfee());
            arenewPermits.setFee(permit.getFee());
            arenewPermits.setConfirmcode(permit.getConfirmcode());
            arenewPermits.setNewpermit(permit.isNewpermit());
            arenewPermits.setRenewed(permit.isRenewed());
            arenewPermits.setPovertyfee(permit.isPovertyfee());
            arenewPermits.setReducedfee(permit.isReducedfee());
            arenewPermits.setHalibut(permit.isHalibut());
            arenewPermits.setSablefish(permit.isSablefish());
            arenewPermits.setAlready(permit.isAlready());
            arenewPermits.setIntend(permit.isIntend());
            arenewPermits.setNointend(permit.isNointend());
            arenewPermits.setIntends(permit.getIntends());
            arenewPermits.setNointends(permit.getNointends());
            arenewPermits.setNotes(permit.getNotes());
            arenewPermits.setMpmt(permit.getMpmt());
            arenewPermits.setPfee(permit.getPfee());
            arenewPermits.setOpfee(permit.getOpfee());
            arenewPermits.setIntendString(permit.getIntendString());
            arenewPermits.setNewrenew(permit.isNewrenew());
            arenewPermits.setVlicensed(permit.getVlicensed());

            return arenewPermits;
        } else {
            ArenewPermits updated = ArenewPermitsMapper.INSTANCE.toShared(permit);
            return updated;
        }
    }

    static ArenewPermits saveOrUpdate(Session session, ArenewPermits update) {
        if (getArenewPermitsById(session, update.getId()) != null) {
            return session.merge(update);
        } else {
            session.persist(update);
            return update;
        }
    }
}
