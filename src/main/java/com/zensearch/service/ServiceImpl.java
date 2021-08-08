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
	
	public List<User> getUserDisplayResults(String value) {

		/** if we find something return this list **/
		List<User> results = new ArrayList<User>();

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

		/** group tickets based on assigneeId **/
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

		/** this search is based on userId so simply get the user object from master map **/
		User user = masterUserMap.get(value);

		/** search may not yield a result so handle it **/
		if(null != user) {
			List<String> listOfTicketsAssignedToUser = ticketMapByAssignee.get(user.getId());
			/** we can have a scenario where a user does not have any tickets **/
			if(null != listOfTicketsAssignedToUser && listOfTicketsAssignedToUser.size() > 0) {
				/** this means this user has some tickets , now extract ticket metadata **/
				listOfTicketsAssignedToUser.forEach((assignedTicketId) -> {
					/** get the ticket details for this ticket id **/
					Ticket ticket = masterTicketMap.get(assignedTicketId);

					String ticketSubject = ticket.getSubject();
					/** we have what we want - populate subject of each assigned ticket into users ticket list **/
					user.getTickets().add(ticketSubject);							
				});
				/** once the user is populated with ticket details - store this user in the result list to be returned **/
				results.add(user);
			}else {
				/** this user may not have any tickets assigned so simply return the user **/
				results.add(user);
			}

		}else {

		}
		return results;
	}
	
	
	
	public List<User> getUserDisplayResults(String key,String value) {

		/** if we find something return this list **/
		List<User> results = new ArrayList<User>();
		
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
		
		/** Mapping each attribute of User as a key and storing in separate maps **/
		Map<String, List<String>> userMapByName =  
				masterUserList.stream()
				.collect(
						Collectors.toMap(
								User::getName,
								user -> {
									List list = new ArrayList<String>();
									list.add(user.getId());
									return list;
								},
								(s, a) -> {
									s.add(a.get(0));
									return s;
								}
								)
						);
		Map<String, List<String>> userMapByCreationAt =  masterUserList.stream()
				.collect(
						Collectors.toMap(
								User::getCreatedDate,
								user -> {
									List list = new ArrayList<String>();
									list.add(user.getId());
									return list;
								},
								(s, a) -> {
									s.add(a.get(0));
									return s;
								}
								)
						);
		
		Map<String, List<String>> userMapByVerification =  masterUserList.stream()
				.collect(
						Collectors.toMap(
								User::getVerified,
								user -> {
									List list = new ArrayList<String>();
									list.add(user.getId());
									return list;
								},
								(s, a) -> {
									s.add(a.get(0));
									return s;
								}
								)
						);

		/** finally store all these maps into a master map for Users **/
		Map<String,Map<String,List<String>>> masterSearchByUserMap = new HashMap<String, Map<String,List<String>>>();
		masterSearchByUserMap.put("name", userMapByName);
		masterSearchByUserMap.put("created_at", userMapByCreationAt);
		masterSearchByUserMap.put("verified", userMapByVerification);
		
		/** start **/
		Map<String, List<String>> ticketMapByAssignee = masterTicketList.stream()
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

		
		/** finally store all these maps into a master map for Tickets **/
		Map<String,Map<String,List<String>>> masterSearchByTicketMap = new HashMap<String, Map<String,List<String>>>();
		masterSearchByTicketMap.put("assignee_id", ticketMapByAssignee);
		/** end **/
		

		/** #1 using search key determine which map needs to be searched  */
		Map<String,List<String>> mapToSearch = masterSearchByUserMap.get(key);
		
		/** #2 extract using search value as key of this map  , to get List of users from users file**/
		List<String> userIdList = mapToSearch.get(value);

		/** #3 we may or may not get any search results **/
		if(null != userIdList && userIdList.size() > 0) {

			/** #4 	so at this moment we have the list of user ids that match the search criteria  
			 *  	Get all the User  for given user ids 
			 *      Get assigneeId for each ticketid
			 *      Extract assignee name using assigneeId 
			 *      Populate this assignee name 
			 *      return results
			 **/
			userIdList.forEach((userId) -> {
				/** get the user object from which we will get user details as needed **/
				User user = masterUserMap.get(userId);
				/** for each of these users we now need to find if they have been assigned tickets 
				 *  to do this we get the ticket list against each user which is already available with us
				 **/
				Map<String,List<String>> mapAssigneeToTickets = masterSearchByTicketMap.get("assignee_id");
				if(null != mapAssigneeToTickets) {
					/** extract ticket list for each assigneeId **/
					List<String> listOfTicketsAssignedToUser = mapAssigneeToTickets.get(user.getId());
					

					/** we can have a scenario where a user does not have any tickets **/
					if(null != listOfTicketsAssignedToUser && listOfTicketsAssignedToUser.size() > 0) {
						/** this means this user has some tickets , now extract ticket metadata **/
						listOfTicketsAssignedToUser.forEach((assignedTicketId) -> {
							/** get the ticket details for this ticket id **/
							Ticket ticket = masterTicketMap.get(assignedTicketId);
							
							String ticketSubject = ticket.getSubject();
							/** we have what we want - populate subject of each assigned ticket into users ticket list **/
							user.getTickets().add(ticketSubject);							
						});
						/** once the user is populated with ticket details - store this user in the result list to be returned **/
						results.add(user);
					}else {
						results.add(user);
					}
				}else {
					/** remote possibility but if no tickets are assigned to any user **/
					results.add(user);
				}
			});
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
