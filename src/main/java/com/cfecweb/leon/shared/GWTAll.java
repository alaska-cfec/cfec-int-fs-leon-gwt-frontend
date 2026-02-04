package com.cfecweb.leon.shared;

import java.io.Serializable;
import java.util.List;

public class GWTAll implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GWTEntity entity;
	private List<GWTPermits> permits;
	private List<GWTVessels> vessels;
	private List<GWTPayment> payment;
	private List<GWTChanges> changes;
	private String combo;
	private String lastupdated;
	
	public GWTAll() {
		super();
	}

	public GWTAll(GWTEntity entity, List<GWTPermits> permits, List<GWTVessels> vessels,
			List<GWTPayment> payment, List<GWTChanges> changes, String combo, String lastupdated) {
		super();
		this.entity = entity;
		this.permits = permits;
		this.vessels = vessels;
		this.payment = payment;
		this.changes = changes;
		this.combo = combo;
		this.lastupdated = lastupdated;
	}

	public String getLastupdated() {
		return lastupdated;
	}

	public void setLastupdated(String lastupdated) {
		this.lastupdated = lastupdated;
	}

	public String getCombo() {
		return combo;
	}

	public void setCombo(String combo) {
		this.combo = combo;
	}

	public GWTEntity getEntity() {
		return entity;
	}

	public void setEntity(GWTEntity ent) {
		this.entity = ent;
	}

	public List<GWTPermits> getPermits() {
		return permits;
	}

	public void setPermits(List<GWTPermits> perList) {
		this.permits = perList;
	}

	public List<GWTVessels> getVessels() {
		return vessels;
	}

	public void setVessels(List<GWTVessels> vesList) {
		this.vessels = vesList;
	}

	public List<GWTPayment> getPayment() {
		return payment;
	}

	public void setPayment(List<GWTPayment> payList) {
		this.payment = payList;
	}

	public List<GWTChanges> getChanges() {
		return changes;
	}

	public void setChanges(List<GWTChanges> chgList) {
		this.changes = chgList;
	}	
}
