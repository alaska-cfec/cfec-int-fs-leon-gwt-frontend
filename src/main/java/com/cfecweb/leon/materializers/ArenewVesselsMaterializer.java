package com.cfecweb.leon.materializers;

import com.cfecweb.leon.mappers.ArenewVesselsIdMapper;
import com.cfecweb.leon.mappers.ArenewVesselsMapper;
import com.cfecweb.leon.shared.ArenewVessels;
import com.cfecweb.leon.shared.ArenewVesselsId;
import org.hibernate.Session;

import java.util.Collection;
import java.util.List;

public interface ArenewVesselsMaterializer {
    static ArenewVessels getArenewVesselsById(Session session, com.cfecweb.leon.dto.ArenewVesselsId id) {
        if (id == null) { return null; }
        return getArenewVesselsById(session, ArenewVesselsIdMapper.INSTANCE.toShared(id));
    }

    static ArenewVessels getArenewVesselsById(Session session, ArenewVesselsId id) {
        if (id == null) { return null; }
        return session.get(ArenewVessels.class, id);
    }

    static List<ArenewVessels> materializeList(Session session, Collection<com.cfecweb.leon.dto.ArenewVessels> dtoVlist) {
        return dtoVlist.stream().map(v -> materialize(session, v)).toList();
    }

    static ArenewVessels materialize(Session session, com.cfecweb.leon.dto.ArenewVessels vessel) {
        ArenewVessels arenewVessels = getArenewVesselsById(session, vessel.getId());
        if (arenewVessels != null) {
            arenewVessels.setName(vessel.getName());
            arenewVessels.setRegNum(vessel.getRegNum());
            arenewVessels.setYearBuilt(vessel.getYearBuilt());
            arenewVessels.setMakeModel(vessel.getMakeModel());
            arenewVessels.setLength(vessel.getLength());
            arenewVessels.setGrossTons(vessel.getGrossTons());
            arenewVessels.setNetTons(vessel.getNetTons());
            arenewVessels.setHomeportCity(vessel.getHomeportCity());
            arenewVessels.setEngineType(vessel.getEngineType());
            arenewVessels.setHorsepower(vessel.getHorsepower());
            arenewVessels.setEstValue(vessel.getEstValue());
            arenewVessels.setHullType(vessel.getHullType());
            arenewVessels.setHullId(vessel.getHullId());
            arenewVessels.setFuel(vessel.getFuel());
            arenewVessels.setHoldTank(vessel.getHoldTank());
            arenewVessels.setLiveTank(vessel.getLiveTank());
            arenewVessels.setRefrigeration(vessel.getRefrigeration());
            arenewVessels.setFreezerCanner(vessel.getFreezerCanner());
            arenewVessels.setTenderPacker(vessel.getTenderPacker());
            arenewVessels.setFishingboat(vessel.getFishingboat());
            arenewVessels.setPurseseine(vessel.getPurseseine());
            arenewVessels.setBeachseine(vessel.getBeachseine());
            arenewVessels.setDriftgillnet(vessel.getDriftgillnet());
            arenewVessels.setSetgillnet(vessel.getSetgillnet());
            arenewVessels.setHandtroll(vessel.getHandtroll());
            arenewVessels.setLongline(vessel.getLongline());
            arenewVessels.setSingleottertrawl(vessel.getSingleottertrawl());
            arenewVessels.setFishwheel(vessel.getFishwheel());
            arenewVessels.setPotgear(vessel.getPotgear());
            arenewVessels.setRingnet(vessel.getRingnet());
            arenewVessels.setDivegear(vessel.getDivegear());
            arenewVessels.setPowertroll(vessel.getPowertroll());
            arenewVessels.setBeamtrawl(vessel.getBeamtrawl());
            arenewVessels.setDredge(vessel.getDredge());
            arenewVessels.setDinglebar(vessel.getDinglebar());
            arenewVessels.setJig(vessel.getJig());
            arenewVessels.setDoubleottertrawl(vessel.getDoubleottertrawl());
            arenewVessels.setHearinggillnet(vessel.getHearinggillnet());
            arenewVessels.setPairtrawl(vessel.getPairtrawl());
            arenewVessels.setReceiveddate(vessel.getReceiveddate());
            arenewVessels.setHomeportState(vessel.getHomeportState());
            arenewVessels.setFoerignFlag(vessel.getFoerignFlag());
            arenewVessels.setTransporter(vessel.getTransporter());
            arenewVessels.setOthergear(vessel.getOthergear());
            arenewVessels.setStatus(vessel.getStatus());
            arenewVessels.setVyear(vessel.getVyear());
            arenewVessels.setRegistrationType(vessel.getRegistrationType());
            arenewVessels.setSalmontrollReg(vessel.getSalmontrollReg());
            arenewVessels.setSalmonregArea(vessel.getSalmonregArea());
            arenewVessels.setPermitSerial1(vessel.getPermitSerial1());
            arenewVessels.setPermitSerial2(vessel.getPermitSerial2());
            arenewVessels.setLastupdated(vessel.getLastupdated());
            arenewVessels.setConfirmcode(vessel.getConfirmcode());
            arenewVessels.setLengthFeet(vessel.getLengthFeet());
            arenewVessels.setLengthInches(vessel.getLengthInches());
            arenewVessels.setFee(vessel.getFee());
            arenewVessels.setNewVessel(vessel.isNewVessel());
            arenewVessels.setRenewed(vessel.isRenewed());
            arenewVessels.setDetails(vessel.getDetails());
            arenewVessels.setSalmontrollDate(vessel.getSalmontrollDate());
            arenewVessels.setQ1(vessel.getQ1());
            arenewVessels.setQ2(vessel.getQ2());
            arenewVessels.setQ3(vessel.getQ3());
            arenewVessels.setQ4(vessel.getQ4());
            arenewVessels.setQ5(vessel.getQ5());
            arenewVessels.setQ6(vessel.getQ6());
            arenewVessels.setQ7(vessel.getQ7());
            arenewVessels.setQ8(vessel.getQ8());
            arenewVessels.setQ9(vessel.getQ9());
            arenewVessels.setQ10(vessel.getQ10());
            arenewVessels.setQ11(vessel.getQ11());
            arenewVessels.setIsnew(vessel.getIsnew());
            arenewVessels.setDelorig(vessel.getDelorig());
            arenewVessels.setInches(vessel.getInches());
            arenewVessels.setNewrenew(vessel.isNewrenew());

            return arenewVessels;
        } else {
            ArenewVessels updated = ArenewVesselsMapper.INSTANCE.toShared(vessel);
            return updated;
        }
    }

    static ArenewVessels saveOrUpdate(Session session, ArenewVessels update) {
        if (getArenewVesselsById(session, update.getId()) != null) {
            return session.merge(update);
        } else {
            session.persist(update);
            return update;
        }
    }
}
