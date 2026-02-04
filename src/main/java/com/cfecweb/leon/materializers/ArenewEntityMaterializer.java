package com.cfecweb.leon.materializers;

import com.cfecweb.leon.mappers.ArenewEntityIdMapper;
import com.cfecweb.leon.shared.ArenewEntityId;
import com.cfecweb.leon.mappers.ArenewEntityMapper;
import com.cfecweb.leon.shared.ArenewEntity;
import org.hibernate.Session;

public interface ArenewEntityMaterializer {
    static ArenewEntity getArenewEntityById(Session session, com.cfecweb.leon.dto.ArenewEntityId id) {
        if (id == null) { return null; }
        return getArenewEntityById(session, ArenewEntityIdMapper.INSTANCE.toShared(id));
    }

    static ArenewEntity getArenewEntityById(Session session, ArenewEntityId id) {
        if (id == null) { return null; }
        return session.get(ArenewEntity.class, id);
    }

    static ArenewEntity materialize(Session session, com.cfecweb.leon.dto.ArenewEntity dtoEntity) {
        ArenewEntity arenewEntity = getArenewEntityById(session, dtoEntity.getId());
        if (arenewEntity != null) {
            arenewEntity.setDiffamountp2year(dtoEntity.getDiffamountp2year());
            arenewEntity.setDifferentialp2(dtoEntity.getDifferentialp2());
            arenewEntity.setPoverty(dtoEntity.getPoverty());
            arenewEntity.setResidency(dtoEntity.getResidency());
            arenewEntity.setCitizen(dtoEntity.getCitizen());
            arenewEntity.setPaddress(dtoEntity.getPaddress());
            arenewEntity.setPcity(dtoEntity.getPcity());
            arenewEntity.setPstate(dtoEntity.getPstate());
            arenewEntity.setPzip(dtoEntity.getPzip());
            arenewEntity.setYears(dtoEntity.getYears());
            arenewEntity.setMonths(dtoEntity.getMonths());
            arenewEntity.setArea(dtoEntity.getArea());
            arenewEntity.setPhone(dtoEntity.getPhone());
            arenewEntity.setEmail(dtoEntity.getEmail());
            arenewEntity.setAlienreg(dtoEntity.getAlienreg());
            arenewEntity.setReducedHalibut(dtoEntity.getReducedHalibut());
            arenewEntity.setReducedSablefish(dtoEntity.getReducedSablefish());
            arenewEntity.setMsnarea(dtoEntity.getMsnarea());
            arenewEntity.setXname(dtoEntity.getXname());
            arenewEntity.setAgent(dtoEntity.getAgent());
            arenewEntity.setDifferentialc(dtoEntity.getDifferentialc());
            arenewEntity.setAgentsub(dtoEntity.getAgentsub());
            arenewEntity.setDiffamount(dtoEntity.getDiffamount());
            arenewEntity.setCreatedate(dtoEntity.getCreatedate());
            arenewEntity.setDifferentialp(dtoEntity.getDifferentialp());
            arenewEntity.setUpdatedate(dtoEntity.getUpdatedate());
            arenewEntity.setPcountry(dtoEntity.getPcountry());
            arenewEntity.setFirsttime(dtoEntity.getFirsttime());
            arenewEntity.setCompany(dtoEntity.getCompany());
            arenewEntity.setPmtcount(dtoEntity.getPmtcount());
            arenewEntity.setVescount(dtoEntity.getVescount());
            arenewEntity.setTotcount(dtoEntity.getTotcount());
            arenewEntity.setRaddress(dtoEntity.getRaddress());
            arenewEntity.setRcity(dtoEntity.getRcity());
            arenewEntity.setRstate(dtoEntity.getRstate());
            arenewEntity.setRzip(dtoEntity.getRzip());
            arenewEntity.setRaddress2(dtoEntity.getRaddress2());
            arenewEntity.setRcountry(dtoEntity.getRcountry());
            arenewEntity.setPaddress2(dtoEntity.getPaddress2());
            arenewEntity.setRyear(dtoEntity.getRyear());
            arenewEntity.setForeign(dtoEntity.isForeign());
            arenewEntity.setIllegal(dtoEntity.getIllegal());
            arenewEntity.setDiffamountcyear(dtoEntity.getDiffamountcyear());
            arenewEntity.setDiffamountpyear(dtoEntity.getDiffamountpyear());
            arenewEntity.setPhonepub(dtoEntity.getPhonepub());
            arenewEntity.setEmailpub(dtoEntity.getEmailpub());
            arenewEntity.setManual(dtoEntity.isManual());
            arenewEntity.setAutoHALreduced(dtoEntity.isAutoHALreduced());
            arenewEntity.setAutoSABreduced(dtoEntity.isAutoSABreduced());
            arenewEntity.setPyearabsent(dtoEntity.getPyearabsent());
            arenewEntity.setPyearabsenttext(dtoEntity.getPyearabsenttext());
            arenewEntity.setAlaskaid(dtoEntity.getAlaskaid());
            arenewEntity.setAlaskaidtext(dtoEntity.getAlaskaidtext());
            arenewEntity.setAlaskavote(dtoEntity.getAlaskavote());
            arenewEntity.setOthertext(dtoEntity.getOthertext());

            return arenewEntity;
        } else {
            ArenewEntity updated = ArenewEntityMapper.INSTANCE.toShared(dtoEntity);
            return updated;
        }
    }

    static ArenewEntity saveOrUpdate(Session session, ArenewEntity update) {
        if (getArenewEntityById(session, update.getId()) != null) {
            return session.merge(update);
        } else {
            session.persist(update);
            return update;
        }
    }
}
