package com.cfecweb.leon.materializers;

import com.cfecweb.leon.mappers.ArenewPaymentMapper;
import com.cfecweb.leon.shared.ArenewPayment;
import org.hibernate.Session;

public interface ArenewPaymentMaterializer {
    static ArenewPayment getArenewPaymentById(Session session, String confirmcode) {
        if (confirmcode == null) { return null; }
        return session.get(ArenewPayment.class, confirmcode);
    }

    static ArenewPayment materialize(Session session, com.cfecweb.leon.dto.ArenewPayment dtoPay) {
        ArenewPayment arenewPayment = getArenewPaymentById(session, dtoPay.getConfirmcode());
        if (arenewPayment != null) {
            arenewPayment.setBaddress(dtoPay.getBaddress());
            arenewPayment.setBcity(dtoPay.getBcity());
            arenewPayment.setBstate(dtoPay.getBstate());
            arenewPayment.setBzip(dtoPay.getBzip());
            arenewPayment.setCcnumber(dtoPay.getCcnumber());
            arenewPayment.setCcname(dtoPay.getCcname());
            arenewPayment.setCcmonth(dtoPay.getCcmonth());
            arenewPayment.setCcyear(dtoPay.getCcyear());
            arenewPayment.setCurtx(dtoPay.getCurtx());
            arenewPayment.setReceiveddate(dtoPay.getReceiveddate());
            arenewPayment.setCctype(dtoPay.getCctype());
            arenewPayment.setIndicator(dtoPay.getIndicator());
            arenewPayment.setTotalamount(dtoPay.getTotalamount());
            arenewPayment.setCcsec(dtoPay.getCcsec());
            arenewPayment.setShiptype(dtoPay.getShiptype());
            arenewPayment.setBcountry(dtoPay.getBcountry());
            arenewPayment.setBaddress2(dtoPay.getBaddress2());
            arenewPayment.setNotes(dtoPay.getNotes());
            arenewPayment.setRjnumber(dtoPay.getRjnumber());
            arenewPayment.setRevoperator(dtoPay.getRevoperator());
            arenewPayment.setPrnoperator(dtoPay.getPrnoperator());
            arenewPayment.setRevcomplete(dtoPay.getRevcomplete());
            arenewPayment.setPrncomplete(dtoPay.getPrncomplete());
            arenewPayment.setAuthcode(dtoPay.getAuthcode());
            arenewPayment.setRevdate(dtoPay.getRevdate());
            arenewPayment.setPrndate(dtoPay.getPrndate());
            arenewPayment.setCcowner(dtoPay.getCcowner());
            arenewPayment.setCcmname(dtoPay.getCcmname());
            arenewPayment.setCclname(dtoPay.getCclname());
            arenewPayment.setDfee1(dtoPay.getDfee1());
            arenewPayment.setDfee2(dtoPay.getDfee2());
            arenewPayment.setDfee3(dtoPay.getDfee3());
            arenewPayment.setSerfee(dtoPay.getSerfee());
            arenewPayment.setShipfee(dtoPay.getShipfee());

            return arenewPayment;
        } else {
            ArenewPayment updated = ArenewPaymentMapper.INSTANCE.toShared(dtoPay);
            return updated;
        }
    }

    static ArenewPayment saveOrUpdate(Session session, ArenewPayment update) {
        if (getArenewPaymentById(session, update.getConfirmcode()) != null) {
            return session.merge(update);
        } else {
            session.persist(update);
            return update;
        }
    }
}
