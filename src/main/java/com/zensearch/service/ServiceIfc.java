package com.zensearch.service;

import java.util.List;

import com.zensearch.pojo.Ticket;

public interface ServiceIfc {
	
	/**
	List<User> getUserDisplayResults(String key,String value,Map<String, Ticket> masterTicketMap,
			Map<String, User> masterUserMap,
			Map<String,Map<String,List<String>>> masterSearchByTicketAttributesMap);
	
	List<User> getUserDisplayResults(String key,String value,Map<String, Ticket> masterTicketMap,
			Map<String,Map<String,List<String>>> masterSearchByUserAttributesMap,Map<String, User> masterUserMap,
			Map<String,Map<String,List<String>>> masterSearchByTicketAttributesMap);
	
	List<Ticket> getTicketDisplayResults(String value,Map<String, Ticket> masterTicketMap,
			Map<String, User> masterUserMap);
	**/
	List<Ticket> getTicketDisplayResults(String key,String value) ;
	
	List<Ticket> getTicketDisplayResults(String value) ;
}
