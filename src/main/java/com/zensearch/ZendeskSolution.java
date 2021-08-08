package com.zensearch;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zensearch.pojo.Ticket;
import com.zensearch.pojo.User;
import com.zensearch.repo.TicketRepoImpl;
import com.zensearch.repo.UserRepoImpl;
import com.zensearch.service.ServiceIfc;
import com.zensearch.service.ServiceImpl;



public class ZendeskSolution {

	private static final Logger logger = LogManager.getLogger(ZendeskSolution.class);

	enum UserSearchableFields { _id, name, created_at, verified };
	enum TicketSearchableFields { _id, type, created_at, subject , assignee_id , tags };
	


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

		

		
		
		



		
		/** perform search based on user input **/
		String userInput;

		Scanner scan = new Scanner(System.in).useDelimiter("\n");
		;
		do {
			System.out.println("welcome to zendesk type quit to exit Enter to continue");
			userInput = scan.nextLine();
			if(userInput.trim().equals("")) {
				System.out.println("select 1 to search 2 to view list of searchable fields and quit to exit");
				userInput = scan.nextLine();
				if(userInput.trim().equals("1")) {
					System.out.println(" select 1 to search users and 2 to search tickets");
					userInput = scan.nextLine();
					if(userInput.trim().equals("1")) {
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
						}else {
							System.out.println("invalid search criteria for User search ! ");
						}
						break;
					}else if(userInput.trim().equals("2")) {
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
						}else {
							System.out.println("invalid search criteria for Ticket search! ");
						}						
						break;
					}else {
						System.out.println("Invalid selection , valid options to search by user is 1 and by ticket 2 .\n");
						break;
					}
				}else if(userInput.trim().equals("2")) {
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
					break;
				}else if(userInput.trim().equals("quit")) {
					break;
				}else {
					System.out.println("Invalid command valid options were enter to continue or quit to exit.\n");
					break;
				}			
			}else if(userInput.trim().equals("quit")) {
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