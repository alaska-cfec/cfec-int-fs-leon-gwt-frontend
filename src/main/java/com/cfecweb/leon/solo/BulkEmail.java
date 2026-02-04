package com.cfecweb.leon.solo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.cfecweb.leon.config.HibernateSessionFactoryProvider;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.cfecweb.leon.server.Notify;
import com.cfecweb.leon.shared.ArenewEntity;

public class BulkEmail {
	final SessionFactory fact = HibernateSessionFactoryProvider.getSessionFactory();
	static Connection conn = null;
	Date sdate = null;
	Notify notify = new Notify();
	
	@SuppressWarnings({ "rawtypes", "unused" })
	public BulkEmail() {
		Session sess = null;
		List<ArenewEntity> elist = null;
		List mlist = null;		
		String lastid = "000000";
		String lastemail = "test@test.com";
		String lastname = "tempname";
		String lastyear = "1900";
		String[] nameArray;
		String[] finalArray = null;
		StringBuffer name = null;
		List<String[]> flist = new ArrayList<String[]>();
		/*
		 *  offset value is what record you start with
		 */
		String offset = "5000";
		/*
		 *  nextgrp value is how many records from offset you want to process
		 */
		String nextgrp = "3000";
		/*
		 *  So, if you start with 0 to 100, and your happy with the grouping of 100, then your next offset is 100 and 
		 *  your nextgrp will remain that same, given you records from 101 to 200, and so on.
		 */
		try {
			sess = fact.openSession();
			//sdate = (Date) sess.createSQLQuery ("SELECT CURRENT_TIMESTAMP").uniqueResult ();
			//mlist = sess.createSQLQuery("select distinct(e.cfecid), e.email, e.xname, e.ryear from arenew_entity e where e.ryear "
			//		+ "in('2020') and e.email is not null and e.cfecid = 'AAAAAA' order by cfecid, ryear desc").list();
			//mlist = sess.createSQLQuery("select distinct(e.cfecid), e.email, e.xname, e.ryear from arenew_entity e where e.ryear "
			//		+ "in('2020') and e.email is not null and e.email = 'ty.mcmichael@alaska.gov' order by cfecid, ryear desc offset "+offset+" rows fetch next "+nextgrp+" rows only").list();
			mlist = sess.createNativeQuery("select distinct(e.cfecid), e.email, e.xname, e.ryear from tier2.arenew_entity e where e.ryear "
					+ "in('2020') and e.cfecid not in(select distinct(x.cfecid) from tier2.arenew_entity x where x.ryear = '2021') " 
					+ "and e.email is not null order by cfecid, ryear desc offset "+offset+" rows fetch next "+nextgrp+" rows only").list();			
			//select distinct(e.cfecid), e.email, e.xname, e.ryear 
			//from arenew_entity e 
			//where e.ryear in('2020') and e.email is not null
			//order by cfecid, ryear desc
			//offset 10 rows fetch next 10 rows only
			
			System.out.println("offset value is " + offset);
			System.out.println("nextgrp size is " + nextgrp);
			System.out.println("mlist records " + mlist.size());
			for (Iterator i = mlist.iterator(); i.hasNext();) {
				Object[] rs1 = (Object[]) i.next();
				if (rs1[0].toString().equals(lastid)) {
					/*if (rs1[1].toString().equals(lastemail)) {
						if (rs1[2].toString().equalsIgnoreCase(lastname)) {
							 
						}
					}*/
					//skip
				} else {
					lastid = rs1[0].toString();
					finalArray = new String[4];
					nameArray = rs1[2].toString().split("\\s+");
					if (nameArray.length > 0) {
						name = new StringBuffer();
						if (nameArray.length == 1) {
							name = name.append(nameArray[0]);
						} else {
							name = name.append(nameArray[0]+", ");
							for (int x=1;x<nameArray.length;x++) {
								name = name.append(nameArray[x]+" ");
							}
						}
						finalArray[0] = rs1[0].toString();
						finalArray[1] = rs1[1].toString();
						finalArray[2] = name.toString();
						finalArray[3] = rs1[3].toString();
						flist.add(finalArray);
					}			
				}					
			}
			System.out.println("flist records " + flist.size());
			notify.bulkCustomNotice(flist, sdate);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("problem with " + lastid + " - " + lastemail + " - " + lastname + " - " + lastyear);
		} finally {
			if (sess.isOpen()) {
				sess.flush();
				sess.close();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		BulkEmail bulk = new BulkEmail();
	}

}
