package com.zensearch.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.zensearch.pojo.Ticket;
import com.zensearch.pojo.User;
import com.zensearch.repo.TicketRepoIfc;
import com.zensearch.repo.UserRepoIfc;

public class ServiceImpl implements ServiceIfc{
	
	/** preferably DI **/
	private TicketRepoIfc ticketRepoIfc;
	private UserRepoIfc userRepoIfc;
	
	public ServiceImpl() {
	}
	
	public ServiceImpl(TicketRepoIfc ticketRepoIfc,UserRepoIfc userRepoIfc) {
		this.ticketRepoIfc = ticketRepoIfc;
		this.userRepoIfc = userRepoIfc;
	}
	
	
	
	public List<Ticket> getTicketDisplayResults(String key,String value) {
		
		/** if we find something return this list **/
		List<Ticket> results = new ArrayList<Ticket>();
		
		/** load list of tickets from repository**/
		List<Ticket> masterTicketList = ticketRepoIfc.getAllTickets();
		
		/** generate or get 'masterTicketMap' from List **/ 
		Map<String, Ticket> masterTicketMap =
				masterTicketList.stream().collect(Collectors.toMap(Ticket::getId,
						Function.identity()));
		
		/** load list of users from repository**/
		List<User> masterUserList = userRepoIfc.getAllUsers();
		
		/** generate or get 'masterUserMap' from List **/ 
		Map<String, User> masterUserMap = masterUserList.stream().collect(Collectors.toMap(User::getId,
				Function.identity()));
		
		/** Group TicketIds by each attribute of Ticket  **/
		/** map data with key as 'type' **/
		Map<String, List<String>> ticketMapByType =  masterTicketList.stream()
				.collect(
						Collectors.toMap(
								Ticket::getType,
								ticket -> {
									List list = new ArrayList<String>();
									list.add(ticket.getId());
									return list;
								},
								(s, a) -> {
									s.add(a.get(0));
									return s;
								}
								)
						);
		Map<String, List<String>> ticketMapByCreatedDate = masterTicketList.stream()
				.collect(
						Collectors.toMap(
								Ticket::getCreatedAt,
								ticket -> {
									List list = new ArrayList<String>();
									list.add(ticket.getId());
									return list;
								},
								(s, a) -> {
									s.add(a.get(0));
									return s;
								}
								)
						);
		Map<String, List<String>> ticketMapBySubject =  masterTicketList.stream()
				.collect(
						Collectors.toMap(
								Ticket::getSubject,
								ticket -> {
									List list = new ArrayList<String>();
									list.add(ticket.getId());
									return list;
								},
								(s, a) -> {
									s.add(a.get(0));
									return s;
								}
								)
						);
		Map<String, List<String>> ticketMapByAssignee =  masterTicketList.stream()
				.collect(
						Collectors.toMap(
								Ticket::getAssigneeId,
								ticket -> {
									List list = new ArrayList<String>();
									list.add(ticket.getId());
									return list;
								},
								(s, a) -> {
									s.add(a.get(0));
									return s;
								}
								)
						);
		/** tags is an array will need to be handled differently **/
		Map<String, List<String>> ticketMapByTags =  transformListToMapOfTags(masterTicketList);
		
		/** finally store all these maps into a master map for Tickets **/
		Map<String,Map<String,List<String>>> masterSearchByTicketMap = new HashMap<String, Map<String,List<String>>>();
		masterSearchByTicketMap.put("created_at", ticketMapByCreatedDate);
		masterSearchByTicketMap.put("type", ticketMapByType);
		masterSearchByTicketMap.put("subject", ticketMapBySubject);
		masterSearchByTicketMap.put("assignee_id", ticketMapByAssignee);
		masterSearchByTicketMap.put("tags", ticketMapByTags);
		
		/** #1 using search key determine which map needs to be searched  */
		Map<String,List<String>> mapToSearch = masterSearchByTicketMap.get(key);

		/** #2 extract from this map using the search value as key of this map **/
		List<String> ticketIdList = mapToSearch.get(value);

		/** #3 we may or may not get any search results **/
		if(null != ticketIdList && ticketIdList.size() > 0) {

			/** #4 	so at this moment we have the list of ticket ids that match the search criteria  
			 *  	Get all the tickets  for given ticket ids 
			 *      Get assigneeId for each ticketid
			 *      Extract assignee name using assigneeId 
			 *      Populate this assignee name 
			 *      return results
			 **/
			ticketIdList.forEach((ticketId) -> {
				Ticket ticket = masterTicketMap.get(ticketId);
				/** now get the assigneeId - this can be null / empty **/
				if(!StringUtils.isBlank(ticket.getAssigneeId())){
					/** to populate assignee name **/
					User user = masterUserMap.get(ticket.getAssigneeId());
					if(null != user) {
						ticket.setAssigneeName(user.getName());
					}					
				}
				results.add(ticket);				
			});
		}
		return results;
	}
	
	public List<Ticket> getTicketDisplayResults(String value) {

		/** if we find something return this list **/
		List<Ticket> results = new ArrayList<Ticket>();
		
		/** load list of tickets from repository**/
		List<Ticket> masterTicketList = ticketRepoIfc.getAllTickets();
		
		/** generate or get 'masterTicketMap' from List **/ 
		Map<String, Ticket> masterTicketMap =
				masterTicketList.stream().collect(Collectors.toMap(Ticket::getId,
						Function.identity()));
		
		/** load list of users from repository**/
		List<User> masterUserList = userRepoIfc.getAllUsers();
		
		/** generate or get 'masterUserMap' from List **/ 
		Map<String, User> masterUserMap = masterUserList.stream().collect(Collectors.toMap(User::getId,
				Function.identity()));

		/** get Ticket details for the ticketId entered by user **/
		Ticket ticket = masterTicketMap.get(value);

		/** we may not get results if no such ticket exists handle it **/
		if(null != ticket) {
			/** now get the assigneeId - this can be null / empty **/
			if(!StringUtils.isBlank(ticket.getAssigneeId())){
				/** to populate assignee name **/
				User user = masterUserMap.get(ticket.getAssigneeId());
				if(null != user) {
					ticket.setAssigneeName(user.getName());
				}					
			}else {				
			}
			/** now add the found ticket to results **/
			results.add(ticket);
		}else {			
		}
		return results;
	}
	
	private Map<String, List<String>> transformListToMapOfTags(List<Ticket> ticketList) {

		Map < String, List< String > > mappedInfo = new HashMap <>();
		
        for ( Ticket ticket : ticketList ) {
        	 List<String> ticketTags = ticket.getTags();
            for ( String tag : ticketTags ) {
            	mappedInfo.computeIfAbsent( tag , k -> new ArrayList < String >() ).add( ticket.getId());
            }
        }		
		return mappedInfo;
	}
}
