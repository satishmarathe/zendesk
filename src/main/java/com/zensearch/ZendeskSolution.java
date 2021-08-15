package com.zensearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zensearch.pojo.Ticket;
import com.zensearch.pojo.User;
import com.zensearch.repo.TicketRepoImpl;
import com.zensearch.repo.UserRepoImpl;
import com.zensearch.service.ServiceIfc;
import com.zensearch.service.ServiceImpl;



public class ZendeskSolution {

	
	private static final String MENU_SEARCH_ZENDESK = "1";
	private static final String MENU_SEARCH_USERS = "1";
	private static final String MENU_SEARCH_TICKETS = "2";
	
	private static final String MENU_TWO = "2";
	private static final String MENU_QUIT = "quit";
	
	enum UserSearchableFields { _id, name, created_at, verified };
	enum TicketSearchableFields { _id, type, created_at, subject , assignee_id , tags };
	
	
	public static void main(String[] args) {
				
		/** perform search based on user input **/
		String userInput;

		Scanner scan = new Scanner(System.in).useDelimiter("\n");
		;
		do {
			System.out.println("Welcome to Zendesk Search");
			System.out.println("Type 'quit' to exit at any time,Press 'Enter' to continue");
			userInput = scan.nextLine();
			if(userInput.trim().equals("")) {
				System.out.println("Select search options:");
				System.out.println( "* Press 1 to search Zendesk");
				System.out.println( "* Press 2 to view a list of searchable fields");
				System.out.println( "* Type 'quit' to exit");
				System.out.println();
				userInput = scan.nextLine();
				if(userInput.trim().equals(MENU_SEARCH_ZENDESK)) {
					System.out.println("Select 1) Users or 2) Tickets");
					userInput = scan.nextLine();
					if(userInput.trim().equals(MENU_SEARCH_USERS)) {
						System.out.println("Enter search term");
						String userKey = scan.nextLine();
						System.out.println("Enter search value");
						String userValue = scan.nextLine();
						
						/** check to ensure the end user is only entering allowed values in userKey and userValue **/
						if(!StringUtils.isBlank(userKey)&& !StringUtils.isBlank(userValue) &&
								isValidUserSearchCriteria(userKey)) {
							
							System.out.println("Searching users for " + userKey + " with a value of " + userValue);
							List <User> userResults = new ArrayList<User>();
							ServiceIfc service = new ServiceImpl(new TicketRepoImpl(),new UserRepoImpl());
							if(userKey.trim().equals("_id")) {								
								/** this will be a simpler search without having to do convoluted extraction so handle separately **/
								/**
								userResults = getUserDisplayResults(userKey,userValue,masterTicketMap,
										masterUserMap,masterSearchByTicketMap);
								**/
								userResults = service.getUserDisplayResults(userValue);
								
							}else {
								/** this involves going through various object graphs **/
								/**
								userResults = getUserDisplayResults(userKey,userValue,masterTicketMap,masterSearchByUserMap,
										masterUserMap,masterSearchByTicketMap);
								**/
								userResults = service.getUserDisplayResults(userKey,userValue);
							}

							if(null == userResults || userResults.size() <= 0) {
								/** no search results , let user know and terminate programme **/
								System.out.println(" No results found");
							}else {
								System.out.println(userResults);
							}
							//break;
						}else {
							System.out.println("invalid search criteria for User search ! ");
							System.out.println(" ");
						}
						
					}else if(userInput.trim().equals(MENU_SEARCH_TICKETS)) {
						System.out.println("Enter search term");
						String ticketKey = scan.nextLine();
						System.out.println("Enter search value");
						String ticketValue = scan.nextLine();
						
						/** check to ensure the end user is only entering allowed values in userKey and userValue **/
						if(!StringUtils.isBlank(ticketKey)&& !StringUtils.isBlank(ticketValue) &&
								isValidTicketSearchCriteria(ticketKey)) {
							
							System.out.println("Searching tickets for " + ticketKey + " with a value of " + ticketValue);
							
							List <Ticket> ticketResults = new ArrayList<Ticket>();
							ServiceIfc service = new ServiceImpl(new TicketRepoImpl(),new UserRepoImpl());
							
							if(ticketKey.trim().equals("_id")) {
								/** this will be a simpler search without having to do convoluted extraction so handle separately **/
								/**
								ticketResults = getTicketDisplayResults(ticketValue,masterTicketMap,
										masterUserMap);
								**/
								ticketResults = service.getTicketDisplayResults(ticketValue);
							}else {
								/** this involves going through various object graphs **/
								/**
								ticketResults = getTicketDisplayResults(ticketKey,ticketValue,masterTicketMap,masterSearchByTicketMap,
										masterUserMap);
								**/
								ticketResults = service.getTicketDisplayResults(ticketKey,ticketValue);
							}						
							if(null == ticketResults || ticketResults.size() <= 0) {
								/** no search results , let user know and terminate programme **/
								System.out.println(" No results found");
							}else {
								System.out.println(ticketResults);
							}	
							//break;
						}else {
							System.out.println("invalid search criteria for Ticket search! ");
							System.out.println(" ");
						}									
					}else if(userInput.trim().equals(MENU_QUIT)){
						break;
					}else {
						System.out.println("Invalid selection , valid options to search by user is 1 and by ticket 2 .\n");
					}
				}else if(userInput.trim().equals(MENU_TWO)) {
					System.out.println("----------------------------");
					System.out.println("Search users with");
					System.out.println(UserSearchableFields._id);
					System.out.println(UserSearchableFields.name);
					System.out.println(UserSearchableFields.created_at);
					System.out.println(UserSearchableFields.verified);
					System.out.println("----------------------------");
					System.out.println("Search Tickets with");
					System.out.println(TicketSearchableFields._id);
					System.out.println(TicketSearchableFields.created_at);
					System.out.println(TicketSearchableFields.type);
					System.out.println(TicketSearchableFields.subject);
					System.out.println(TicketSearchableFields.assignee_id);
					System.out.print(TicketSearchableFields.tags);
					System.out.println(" ");
					System.out.println(" ");
				}else if(userInput.trim().equals(MENU_QUIT)) {
					break;
				}else {
					System.out.println("Invalid command valid options were enter to continue or quit to exit.\n");
				}			
			}else if(userInput.trim().equals(MENU_QUIT)) {
				break;
			}else {
				System.out.println("Invalid command ! Please hit enter to continue or quit to exit.\n");
			}
		} while (!userInput.equals(MENU_QUIT));



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

	private static boolean isValidUserSearchCriteria(String searchableField) {

		for (UserSearchableFields field : UserSearchableFields.values()) {
			if (field.name().equals(searchableField)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isValidTicketSearchCriteria(String searchableField) {

		for (TicketSearchableFields field : TicketSearchableFields.values()) {
			if (field.name().equals(searchableField)) {
				return true;
			}
		}
		return false;
	}


}