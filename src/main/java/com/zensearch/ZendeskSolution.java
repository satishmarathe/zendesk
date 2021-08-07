package com.zensearch;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zensearch.pojo.Ticket;
import com.zensearch.pojo.User;



public class ZendeskSolution {

	private static final Logger logger = LogManager.getLogger(ZendeskSolution.class);



	public static void main(String[] args) {
		logger.info("start");

		/** First we need to read both files i.e. tickets and users **/
		/** Second Take user inputs and handle them **/
		/** In case of an empty file or file not found - log errors to log and let user know something is wrong **/
		/** Handle invalid commands by letting them know list of allowed / valid commands **/
		/** do not exit unless user has explicitly hit quit **/
		InputStreamReader ticketReader = null;
		InputStreamReader userReader = null;
		List<Ticket> ticketList = null;
		List<User> userList = null;
		try {			
			InputStream userInputStream = ZendeskSolution.class.getClassLoader().getResourceAsStream("users.json");
			InputStream ticketInputStream = ZendeskSolution.class.getClassLoader().getResourceAsStream("tickets.json");

			try {

				ticketReader = new InputStreamReader(ticketInputStream,"UTF-8");
				userReader = new InputStreamReader(userInputStream,"UTF-8");

				ticketList = new Gson().fromJson(ticketReader, new TypeToken<List<Ticket>>() {}.getType());
				userList = new Gson().fromJson(userReader, new TypeToken<List<User>>() {}.getType());
				//ticketList.forEach(System.out::println);

				for(int i = 0;i<ticketList.size();i++) {
					//System.out.println(ticketList.get(i).getType());
				}

				ticketReader.close();
				userReader.close();

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				//IOUtils.closeQuietly(userInputStream);
				IOUtils.closeQuietly(ticketInputStream);
				IOUtils.closeQuietly(userInputStream);
			}

		}catch(Exception e) {
			e.printStackTrace();
		}

		/** if we have the list of tickets lets organise the data to help make search scale **/

		/** store the tickets from List into a Map , using java 8 rather than iterate over the entire list **/
		Map<String, Ticket> masterTicketMap =
				ticketList.stream().collect(Collectors.toMap(Ticket::getId,
						Function.identity()));
		System.out.println("size of map is " + masterTicketMap.size());
		
		Map<String, User> masterUserMap =
				userList.stream().collect(Collectors.toMap(User::getId,
						Function.identity()));

		/** store the tickets from List into a Map , key = 'type' and value will be the ticket id  no point 
		 *  in storing an object again **/	
		
		/** Mapping each attribute of ticket as a key and storing in separate maps **/
		/** map data with key as 'type' **/
		Map<String, List<String>> ticketMapByType =  transformListToMapOfTypes(ticketList);
		Map<String, List<String>> ticketMapByCreatedDate =  transformListToMapOfCreatedDate(ticketList);
		Map<String, List<String>> ticketMapBySubject =  transformListToMapOfSubject(ticketList);
		Map<String, List<String>> ticketMapByAssignee =  transformListToMapOfAssignee(ticketList);

		/** tags is an array will need to be handled differently **/
		Map<String, List<String>> ticketMapByTags =  transformListToMapOfTags(ticketList);
		
		/** finally store all these maps into a master map for Tickets **/
		Map<String,Map<String,List<String>>> masterSearchByTicketMap = new HashMap<String, Map<String,List<String>>>();
		masterSearchByTicketMap.put("created_at", ticketMapByCreatedDate);
		masterSearchByTicketMap.put("type", ticketMapByType);
		masterSearchByTicketMap.put("subject", ticketMapBySubject);
		masterSearchByTicketMap.put("assignee_id", ticketMapByAssignee);
		masterSearchByTicketMap.put("tags", ticketMapByTags);
		
		ticketMapByType.forEach((key, value) -> {
			//System.out.println("size of the key: " + key + " is =  " + value.size());
		});
		
		masterSearchByTicketMap.forEach((key, value) -> {
			//System.out.println("masterSearchMap size of the key: " + key + " is =  " + value.size());
		});
		
		/** Mapping each attribute of User as a key and storing in separate maps **/
		Map<String, List<String>> userMapByName =  transformUserListToMapOfNames(userList);
		Map<String, List<String>> userMapByCreationAt =  transformUserListToMapOfCreatedAt(userList);
		Map<String, List<String>> userMapByVerification =  transformUserListToMapOfVerification(userList);
		
		/** finally store all these maps into a master map for Users **/
		Map<String,Map<String,List<String>>> masterSearchByUserMap = new HashMap<String, Map<String,List<String>>>();
		masterSearchByUserMap.put("name", userMapByName);
		masterSearchByUserMap.put("created_at", userMapByCreationAt);
		masterSearchByUserMap.put("verified", userMapByVerification);
		
	
	    
		//System.out.println("size of type map is " + ticketMapByType.size());

		/** perform search based on user input **/
		String userInput;

		Scanner scan = new Scanner(System.in).useDelimiter("\n");
		;
		do {
			System.out.println("welcome to zendesk type quit to exit Enter to continue");
			userInput = scan.nextLine();
			System.out.println("user entered " + userInput);
			if(userInput.trim().equals("")) {
				System.out.println(" user has hit enter ");
				System.out.println("select 1 to search 2 to view list of searchable fields and quit to exit");
				userInput = scan.nextLine();
				if(userInput.trim().equals("1")) {
					System.out.println("user wants to search Zendesk");
					System.out.println(" select 1 to search users and 2 to search tickets");
					userInput = scan.nextLine();
					if(userInput.trim().equals("1")) {
						System.out.println("user wants to search by users");
						System.out.println("Enter search term");
						String userKey = scan.nextLine();
						System.out.println("Enter search value");
						String userValue = scan.nextLine();
						System.out.println("what did the user enter for users = " + userKey + " and " + userValue);
						//Console.displayResults(zendeskSearch.searchByTicket(term, value));
						List <User> userResults = getUserDisplayResults(userKey,userValue,masterTicketMap,masterSearchByUserMap,
								masterUserMap,masterSearchByTicketMap);
						if(null == userResults || userResults.size() <= 0) {
							/** no search results , let user know and terminate programme **/
							System.out.println(" No search results for " + userKey + " = " + userValue);
						}else {
							System.out.println(userResults);
						}
						break;
						

					}else if(userInput.trim().equals("2")) {
						System.out.println("user wants to search by tickets");
						System.out.println("Enter search term");
						String ticketKey = scan.nextLine();
						System.out.println("Enter search value");
						String ticketValue = scan.nextLine();
						System.out.println("what did the user enter = " + ticketKey + ticketValue);
						//Console.displayResults(zendeskSearch.searchByTicket(term, value));
						List <Ticket> ticketResults = getTicketDisplayResults(ticketKey,ticketValue,masterTicketMap,masterSearchByTicketMap,masterUserMap);
						if(null == ticketResults || ticketResults.size() <= 0) {
							/** no search results , let user know and terminate programme **/
							System.out.println(" No search results for " + ticketKey + " = " + ticketValue);
						}else {
							System.out.println(ticketResults);
						}	
						break;
					}else {
						System.out.println("Please enter a valid option either 1 or 2 only.\n");
					}
				}else if(userInput.trim().equals("2")) {
					System.out.println("user wants to know what fields can be used in search ...");
				}else if(userInput.trim().equals("quit")) {
					System.out.println("quit inner so exiting ...");
					//System.exit(1);
					break;
				}else {
					System.out.println("Invalid command ! Please hit enter to continue or quit to exit.\n");
				}			
			}else if(userInput.trim().equals("quit")) {
				System.out.println("quit outer so exiting ...");
				//System.exit(1);
				break;
			}else {
				System.out.println("Invalid command ! Please hit enter to continue or quit to exit.\n");
			}
		} while (!userInput.equals("quit"));



		/** if the search is based on  ticket attribute :
		 *  #1 check which attribute is the search based on - this will decide which map to look at 
		 *  #2 retrieve the Value from appropriate map based on search param >> which is the key of the map
		 *  #3 we may get a result as an arrayList or we may get null
		 *  #4 if null this means there are no search results - let user know 
		 *  #5 if not null we have results 
		 *  #6 for each ticket in List get corresponding Ticket object from Master Map 
		 *  #7 extract assigneeId from ticket Object and then from master Map of Assignees retrieve Assignee object
		 *  #8 extract assignee name and set it to Ticket object
		 *  #9 print the output
		 *   
		 **/




	}	
	
	private static List<User> getUserDisplayResults(String key,String value,Map<String, Ticket> masterTicketMap,
			Map<String,Map<String,List<String>>> masterSearchByUserAttributesMap,Map<String, User> masterUserMap,
			Map<String,Map<String,List<String>>> masterSearchByTicketAttributesMap) {
		
		/** if we find something return this list **/
		List<User> results = new ArrayList<User>();
		
		System.out.println(">>>>>>> key = " + key + " and value is " + value);
		masterSearchByUserAttributesMap.forEach((key1, value1) -> {
			//System.out.println("masterSearchByUserAttributesMap again size of the key: " + key1 + " is =  " + value1.size());
		});
		
		/** #1 using search key determine which map needs to be searched  */
		Map<String,List<String>> mapToSearch = masterSearchByUserAttributesMap.get(key);
		if(null == mapToSearch || mapToSearch.size() <= 0 ) {
			System.out.println("!!!!! map is null !!!!!");
		}
		mapToSearch.forEach((key2, value2) -> {
			//System.out.println("mapToSearch size of the key: " + key2 + " is =  " + value2.size());
		});
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
			System.out.println("the Users which matched are " + userIdList);
			userIdList.forEach((userId) -> {
				System.out.println("user id >>> " + userId);
				/** get the user object from which we will get user details as needed **/
				User user = masterUserMap.get(userId);
				/** for each of these users we now need to find if they have been assigned tickets 
				 *  to do this we get the ticket list against each user which is already available with us
				 **/
				Map<String,List<String>> mapAssigneeToTickets = masterSearchByTicketAttributesMap.get("assignee_id");
				if(null != mapAssigneeToTickets) {
					/** extract ticket list for each assigneeId **/
					List<String> listOfTicketsAssignedToUser = mapAssigneeToTickets.get(user.getId());
					if(null != listOfTicketsAssignedToUser && listOfTicketsAssignedToUser.size() > 0) {
						System.out.println("good $$$ listOfTicketsAssignedToUser is NOT null for user id " + user.getId());
						listOfTicketsAssignedToUser.forEach((eachTicket) -> System.out.println("boom " + eachTicket)); 
					}else {
						System.out.println("beware !!! listOfTicketsAssignedToUser is null for user id " + user.getId());
					}
					
					/** we can have a scenario where a user does not have any tickets **/
					if(null != listOfTicketsAssignedToUser && listOfTicketsAssignedToUser.size() > 0) {
						/** this means this user has some tickets , now extract ticket metadata **/
						listOfTicketsAssignedToUser.forEach((assignedTicketId) -> {
							System.out.println(" we are here for " + assignedTicketId);
							/** get the ticket details for this ticket id **/
							Ticket ticket = masterTicketMap.get(assignedTicketId);
							if(null != ticket) {
								System.out.println("yea ticket is not null");
								System.out.println("ticket subject is " + ticket.getSubject());
							}else {
								System.out.println("whoops ticket is null");
							}
							String ticketSubject = ticket.getSubject();
							/** we have what we want - populate subject of each assigned ticket into users ticket list **/
							user.getTickets().add(ticketSubject);							
						});
						/** once the user is populated with ticket details - store this user in the result list to be returned **/
						results.add(user);
					}
				}							
			});
			System.out.println("<<<< we have something >>>");
			
		}
		return results;
	}
	
	private static List<Ticket> getTicketDisplayResults(String key,String value,Map<String, Ticket> masterTicketMap,
			Map<String,Map<String,List<String>>> masterSearchMap,Map<String, User> masterUserMap) {
		
		/** if we find something return this list **/
		List<Ticket> results = new ArrayList<Ticket>();
		
		//System.out.println(">>>>>>> key = " + key + " and value is " + value);
		masterSearchMap.forEach((key1, value1) -> {
			//System.out.println("masterSearchMap again size of the key: " + key1 + " is =  " + value1.size());
		});
		
		/** #1 using search key determine which map needs to be searched  */
		Map<String,List<String>> mapToSearch = masterSearchMap.get(key);
		
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
			System.out.println("the tickets which matched are " + ticketIdList);
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
			System.out.println("<<<< we have something >>>");
			
		}
		return results;
	}
	
	
    private static Map<String, List<String>> transformUserListToMapOfVerification(List<User> userList) {

		Map<String, List<String>> mappedInfo = 
				userList.stream()
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
		return mappedInfo;
	}
	
    private static Map<String, List<String>> transformUserListToMapOfCreatedAt(List<User> userList) {

		Map<String, List<String>> mappedInfo = 
				userList.stream()
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
		return mappedInfo;
	}
	
    private static Map<String, List<String>> transformUserListToMapOfNames(List<User> userList) {

		Map<String, List<String>> mappedInfo = 
				userList.stream()
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
		return mappedInfo;
	}
	
    
	private static Map<String, List<String>> transformListToMapOfTypes(List<Ticket> ticketList) {

		Map<String, List<String>> mappedInfo = 
				ticketList.stream()
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
		return mappedInfo;
	}

	private static Map<String, List<String>> transformListToMapOfCreatedDate(List<Ticket> ticketList) {

		Map<String, List<String>> mappedInfo = 
				ticketList.stream()
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
		return mappedInfo;
	}

	private static Map<String, List<String>> transformListToMapOfSubject(List<Ticket> ticketList) {

		Map<String, List<String>> mappedInfo = 
				ticketList.stream()
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
		return mappedInfo;
	}

	private static Map<String, List<String>> transformListToMapOfAssignee(List<Ticket> ticketList) {

		Map<String, List<String>> mappedInfo = 
				ticketList.stream()
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
		return mappedInfo;
	}

	private static Map<String, List<String>> transformListToMapOfTags(List<Ticket> ticketList) {

		Map<String, List<String>> mappedInfo = 
				ticketList.stream()
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
		return mappedInfo;
	}

	
}