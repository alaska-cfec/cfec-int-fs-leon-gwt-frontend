package com.cfecweb.leon.server;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import com.cfecweb.leon.shared.ArenewPermits;
import com.pervasive.jdbc.v2.Connection;
import com.pervasive.jdbc.v2.ResultSet;
import com.pervasive.jdbc.v2.Statement;

public class PervasiveVesCheck {
	
	public static void main(String[] args) {
		//boolean isLicensed = getVessel("2019", "23574");
		//System.out.println(isLicensed);
	}
	
	public static List<ArenewPermits> getVessel(String year, List<ArenewPermits> plist, String btreiveHost, getDataImpl dataImpl) {
		com.pervasive.jdbc.v2.Connection conn = null;
		com.pervasive.jdbc.v2.Statement stmt = null;
		com.pervasive.jdbc.v2.ResultSet rs = null;
		System.out.println("btrieveHost is " + btreiveHost);
		try {
			Class.forName("com.pervasive.jdbc.v2.Driver");
			conn = (Connection) PervasiveConnection.getit(btreiveHost);
			stmt = (Statement) PervasiveStatement.getit(conn);
		} catch (Exception e) {
			System.err.print("ClassNotFoundException: ");
			System.out.println(e.toString());
			System.err.println(e.getMessage());
		}
		try {
			for (Iterator<ArenewPermits> it = plist.iterator(); it.hasNext();) {
    			ArenewPermits permit = (ArenewPermits) it.next();
    			System.out.println(permit.getId().getCfecid() + " Permit vlicense for " + permit.getId().getSerial() + " is " + permit.getVlicensed());
    			if (!(permit.getVlicensed()==null)) {
    				if (permit.getVlicensed().equalsIgnoreCase("remote")) {
    					System.out.println(permit.getId().getCfecid() + " ADFG " + permit.getAdfg() + " for " + permit.getId().getSerial() +
    							" is NOT in their inventory, check for license status");
    					String query = "select count(*) from ves"+year+" where v_adfg = '"+permit.getAdfg()+"'";
    					rs = (ResultSet) stmt.executeQuery(query);
    					while (rs.next()) {
    						if (Integer.parseInt(rs.getString(1))> 0) {
    							permit.setVlicensed("YES");
    						} else {
    							String vfnd = inLeon(dataImpl, permit.getAdfg(), year);
    							permit.setVlicensed(vfnd);
    						}
    					}
    					System.out.println(permit.getId().getCfecid() + " Looked at Licensing and LEON orders, vessel "
    							+ "licensing status for " + permit.getAdfg() + ", serial " + permit.getId().getSerial() + " is " + permit.getVlicensed());
    				}
    			} else {
    				System.out.println(permit.getId().getCfecid() + " ADFG " + permit.getAdfg() + " for " + permit.getId().getSerial() +
							" is NOT in their inventory, check for license status (WHY IS VLICENSED VALUE NULL?)");
					String query = "select count(*) from ves"+year+" where v_adfg = '"+permit.getAdfg()+"'";
					rs = (ResultSet) stmt.executeQuery(query);
					while (rs.next()) {
						if (Integer.parseInt(rs.getString(1))> 0) {
							permit.setVlicensed("YES");
						} else {
							String vfnd = inLeon(dataImpl, permit.getAdfg(), year);
							permit.setVlicensed(vfnd);
						}
					}
					System.out.println(permit.getId().getCfecid() + " Looked at Licensing and LEON orders, vessel "
							+ "licensing status for " + permit.getAdfg() + ", serial " + permit.getId().getSerial() + " is " + permit.getVlicensed());
    			}				
			};			
		} catch (SQLException e) {
			// put logger statement here
			e.printStackTrace();
			try{
				 if(conn!=null)
		            conn.rollback();
		      }catch(SQLException se2){
		         se2.printStackTrace();
		      }
		} catch (Exception e) {
			// put logger statement here
			e.printStackTrace();
		} finally {
			try {
				if (rs!=null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}// nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}		
		return plist;		
	}
	
	public static String inLeon(getDataImpl dataImpl, String adfg, String year) {
		String found = "NO";
		Session session = dataImpl.fact.openSession();
		Long vcnt = (Long) session.createQuery("select count(*) from ArenewVessels where adfg = '"+adfg+"' and ryear = '"+year+"'").uniqueResult();
		if (vcnt > 0) {
			found = "YES";
		}
		return found;		
	}
}
