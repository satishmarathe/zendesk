package com.zensearch.service;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zensearch.pojo.Ticket;
import com.zensearch.pojo.User;
import com.zensearch.repo.TicketRepoImpl;
import com.zensearch.repo.UserRepoImpl;

@ExtendWith(MockitoExtension.class)
class ServiceImplUserSearchTest {
	
	@Mock
	TicketRepoImpl ticketRepoImplMock; 
	
	@Mock
	UserRepoImpl userRepoImplMock; 
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testSearchUserById() {		
		/** Given : two tickets with same assignee  
		 *  When  : search User by id which is same as ticket assignee
		 *  Then  : return the User with two ticket details as assignment
		 */
		List<Ticket> ticketList = new ArrayList<Ticket>();
		Ticket t1 = new Ticket();
		t1.setId("436bf9b0-1147-4c0a-8439-6f79833bff5b");
		t1.setCreatedAt("2016-04-28T11:19:34-10:00");
		t1.setType("incident");
		t1.setSubject("This is a subject");
		t1.setAssigneeId("24");
		List <String> test = new ArrayList<String>();
		test.add("Ohio");
		t1.setTags(test);
		
		Ticket t2 = new Ticket();
		t2.setId("336bf9b0-1147-4c0a-8439-6f79833bff5b");
		t1.setCreatedAt("2016-05-28T11:19:34-10:00");
		t2.setType("incident");
		t2.setSubject("World War");
		t2.setAssigneeId("24");
		t2.setTags(test);
		
		ticketList.add(t1);
		ticketList.add(t2);
		
				
		List<User> userList = new ArrayList<User>();
		
		User u1 = new User();		
		u1.setId("24");
		u1.setName("Francisca Rasmussen");
		u1.setCreatedDate("2016-04-15T05:19:46-10:00");
		u1.setVerified("true");
		
		userList.add(u1);
		
		//assertNotNull(ticketRepoImplMock);
		
		when(ticketRepoImplMock.getAllTickets()).thenReturn(ticketList);
		when(userRepoImplMock.getAllUsers()).thenReturn(userList);
		//fail("Not yet implemented"); 
		ServiceIfc testService = new ServiceImpl(ticketRepoImplMock,userRepoImplMock);
		
		
		List<User> userResultList = testService.getUserDisplayResults("24");
		List <String> userTicketDetails = userResultList.get(0).getTickets();
		assertThat(userResultList.size(),is(1));
		assertThat(userResultList.get(0).getTickets().size(),is(2));
		assertThat(userTicketDetails,contains("This is a subject","World War"));
		
	}
	
	@Test
	void testSearchUserByNameNotFound() {		
		/** Given : two users 
		 *  When  : search user by type = name
		 *  Then  : return no users since user name not found
		 */
		List<Ticket> ticketList = new ArrayList<Ticket>();
		Ticket t1 = new Ticket();
		t1.setId("436bf9b0-1147-4c0a-8439-6f79833bff5b");
		t1.setCreatedAt("2016-04-28T11:19:34-10:00");
		t1.setType("incident");
		t1.setSubject("This is a subject");
		t1.setAssigneeId("25");
		List <String> test = new ArrayList<String>();
		test.add("Ohio");
		t1.setTags(test);
		
		ticketList.add(t1);
				
		List<User> userList = new ArrayList<User>();
		
		User u1 = new User();		
		u1.setId("24");
		u1.setName("Francisca Rasmussen");
		u1.setCreatedDate("2016-04-15T05:19:46-10:00");
		u1.setVerified("true");
		
		userList.add(u1);
		
		User u2 = new User();		
		u2.setId("25");
		u2.setName("Michael Jordan");
		u2.setCreatedDate("2016-04-15T05:19:46-10:00");
		u2.setVerified("true");
		
		userList.add(u2);
		
		//assertNotNull(ticketRepoImplMock);
		
		when(ticketRepoImplMock.getAllTickets()).thenReturn(ticketList);
		when(userRepoImplMock.getAllUsers()).thenReturn(userList);
		//fail("Not yet implemented"); 
		ServiceIfc testService = new ServiceImpl(ticketRepoImplMock,userRepoImplMock);
		
		
		List<User> userResultList = testService.getUserDisplayResults("name","436bf9b0-1147-4c0a-8439-6f79833bff5b");
		assertThat(userResultList.size(),is(0));		
	}
	
	@Test
	void testSearchUserByNameFoundOneUserNoTickets() {		
		/** Given : two users , One ticket
		 *  When  : search user by type = name
		 *  Then  : return single user with no associated tickets
		 */
		List<Ticket> ticketList = new ArrayList<Ticket>();
		Ticket t1 = new Ticket();
		t1.setId("436bf9b0-1147-4c0a-8439-6f79833bff5b");
		t1.setCreatedAt("2016-04-28T11:19:34-10:00");
		t1.setType("incident");
		t1.setSubject("This is a subject");
		t1.setAssigneeId("25");
		List <String> test = new ArrayList<String>();
		test.add("Ohio");
		t1.setTags(test);
		
		ticketList.add(t1);
				
		List<User> userList = new ArrayList<User>();
		
		User u1 = new User();		
		u1.setId("24");
		u1.setName("Francisca Rasmussen");
		u1.setCreatedDate("2016-04-15T05:19:46-10:00");
		u1.setVerified("true");
		
		userList.add(u1);
		
		User u2 = new User();		
		u2.setId("25");
		u2.setName("Michael Jordan");
		u2.setCreatedDate("2016-04-15T05:19:46-10:00");
		u2.setVerified("true");
		
		userList.add(u2);
		
		//assertNotNull(ticketRepoImplMock);
		
		when(ticketRepoImplMock.getAllTickets()).thenReturn(ticketList);
		when(userRepoImplMock.getAllUsers()).thenReturn(userList);
		//fail("Not yet implemented"); 
		ServiceIfc testService = new ServiceImpl(ticketRepoImplMock,userRepoImplMock);
		
		List<User> userResultList = testService.getUserDisplayResults("name","Francisca Rasmussen");
		assertThat(userResultList.size(),is(1));		
	}
	
	@Test
	void testSearchUserByNameFoundOneUserOneTicket() {		
		/** Given : two users 
		 *  When  : search user by type = name
		 *  Then  : return single user with 1 associated ticket
		 */
		List<Ticket> ticketList = new ArrayList<Ticket>();
		Ticket t1 = new Ticket();
		t1.setId("436bf9b0-1147-4c0a-8439-6f79833bff5b");
		t1.setCreatedAt("2016-04-28T11:19:34-10:00");
		t1.setType("incident");
		t1.setSubject("This is a subject");
		t1.setAssigneeId("25");
		List <String> test = new ArrayList<String>();
		test.add("Ohio");
		t1.setTags(test);
		
		ticketList.add(t1);
				
		List<User> userList = new ArrayList<User>();
		
		User u1 = new User();		
		u1.setId("24");
		u1.setName("Francisca Rasmussen");
		u1.setCreatedDate("2016-04-15T05:19:46-10:00");
		u1.setVerified("true");
		
		userList.add(u1);
		
		User u2 = new User();		
		u2.setId("25");
		u2.setName("Michael Jordan");
		u2.setCreatedDate("2016-04-15T05:19:46-10:00");
		u2.setVerified("true");
		
		userList.add(u2);
		
		//assertNotNull(ticketRepoImplMock);
		
		when(ticketRepoImplMock.getAllTickets()).thenReturn(ticketList);
		when(userRepoImplMock.getAllUsers()).thenReturn(userList);
		//fail("Not yet implemented"); 
		ServiceIfc testService = new ServiceImpl(ticketRepoImplMock,userRepoImplMock);
		
		List<User> userResultList = testService.getUserDisplayResults("name","Michael Jordan");
		assertThat(userResultList.size(),is(1));	
		//userResultList.get(0).getTickets().contains("This is a subject");
		assertThat(userResultList.get(0).getTickets(),contains("This is a subject"));
	}
	
	@Test
	void testSearchUserByNameFoundOneUserTwoTickets() {		
		/** Given : two users 
		 *  When  : search user by type = name
		 *  Then  : return single user with 1 associated ticket
		 */
		List<Ticket> ticketList = new ArrayList<Ticket>();
		Ticket t1 = new Ticket();
		t1.setId("436bf9b0-1147-4c0a-8439-6f79833bff5b");
		t1.setCreatedAt("2016-04-28T11:19:34-10:00");
		t1.setType("incident");
		t1.setSubject("Olympics");
		t1.setAssigneeId("25");
		List <String> test = new ArrayList<String>();
		test.add("Ohio");
		t1.setTags(test);
		
		ticketList.add(t1);
		
		
		Ticket t2 = new Ticket();
		t2.setId("xyzbf9b0-1147-4c0a-8439-6f79833bfabc");
		t2.setCreatedAt("2016-04-28T11:19:34-10:00");
		t2.setType("sports");
		t2.setSubject("Athletics");
		t2.setAssigneeId("25");
		List <String> test2 = new ArrayList<String>();
		test2.add("Ohio");
		t2.setTags(test2);
		
		ticketList.add(t2);
				
		List<User> userList = new ArrayList<User>();
		
		User u1 = new User();		
		u1.setId("24");
		u1.setName("Francisca Rasmussen");
		u1.setCreatedDate("2016-04-15T05:19:46-10:00");
		u1.setVerified("true");
		
		userList.add(u1);
		
		User u2 = new User();		
		u2.setId("25");
		u2.setName("Michael Jordan");
		u2.setCreatedDate("2016-04-15T05:19:46-10:00");
		u2.setVerified("true");
		
		userList.add(u2);
		
		//assertNotNull(ticketRepoImplMock);
		
		when(ticketRepoImplMock.getAllTickets()).thenReturn(ticketList);
		when(userRepoImplMock.getAllUsers()).thenReturn(userList);
		//fail("Not yet implemented"); 
		ServiceIfc testService = new ServiceImpl(ticketRepoImplMock,userRepoImplMock);
		
		List<User> userResultList = testService.getUserDisplayResults("name","Michael Jordan");
		assertThat(userResultList.size(),is(1));	
		assertThat(userResultList.get(0).getTickets().size(),is(2));
		assertThat(userResultList.get(0).getTickets(),contains("Olympics","Athletics"));
	}
	
	@Test
	void testSearchUserByNameFoundManyUsersNoTickets() {		
		/** Given : two users with same name
		 *  When  : search user by type = name
		 *  Then  : return both users BUT 0 associated ticket
		 */
		List<Ticket> ticketList = new ArrayList<Ticket>();
		Ticket t1 = new Ticket();
		t1.setId("436bf9b0-1147-4c0a-8439-6f79833bff5b");
		t1.setCreatedAt("2016-04-28T11:19:34-10:00");
		t1.setType("incident");
		t1.setSubject("Olympics");
		t1.setAssigneeId("25");
		List <String> test = new ArrayList<String>();
		test.add("Ohio");
		t1.setTags(test);
		
		ticketList.add(t1);
		
		
		Ticket t2 = new Ticket();
		t2.setId("xyzbf9b0-1147-4c0a-8439-6f79833bfabc");
		t2.setCreatedAt("2016-04-28T11:19:34-10:00");
		t2.setType("sports");
		t2.setSubject("Athletics");
		t2.setAssigneeId("25");
		List <String> test2 = new ArrayList<String>();
		test2.add("Ohio");
		t2.setTags(test2);
		
		ticketList.add(t2);
				
		List<User> userList = new ArrayList<User>();
		
		User u1 = new User();		
		u1.setId("24");
		u1.setName("Francisca");
		u1.setCreatedDate("2016-04-15T05:19:46-10:00");
		u1.setVerified("true");
		
		userList.add(u1);
		
		User u2 = new User();		
		u2.setId("23");
		u2.setName("Francisca");
		u2.setCreatedDate("2016-04-15T05:19:46-10:00");
		u2.setVerified("true");
		
		userList.add(u2);
		
		//assertNotNull(ticketRepoImplMock);
		
		when(ticketRepoImplMock.getAllTickets()).thenReturn(ticketList);
		when(userRepoImplMock.getAllUsers()).thenReturn(userList);
		//fail("Not yet implemented"); 
		ServiceIfc testService = new ServiceImpl(ticketRepoImplMock,userRepoImplMock);
		
		List<User> userResultList = testService.getUserDisplayResults("name","Francisca");
		assertThat(userResultList.size(),is(2));	
		assertThat(userResultList.get(0).getTickets().size(),is(0));
		assertThat(userResultList.get(1).getTickets().size(),is(0));
	}
	
	@Test
	void testSearchUserByNameFoundManyUsersOneTicket() {		
		/** Given : two users with same name
		 *  When  : search user by type = name
		 *  Then  : return both users AND 1 associated ticket
		 */
		List<Ticket> ticketList = new ArrayList<Ticket>();
		Ticket t1 = new Ticket();
		t1.setId("436bf9b0-1147-4c0a-8439-6f79833bff5b");
		t1.setCreatedAt("2016-04-28T11:19:34-10:00");
		t1.setType("incident");
		t1.setSubject("Olympics");
		t1.setAssigneeId("25");
		List <String> test = new ArrayList<String>();
		test.add("Ohio");
		t1.setTags(test);
		
		ticketList.add(t1);
		
		
		Ticket t2 = new Ticket();
		t2.setId("xyzbf9b0-1147-4c0a-8439-6f79833bfabc");
		t2.setCreatedAt("2016-04-28T11:19:34-10:00");
		t2.setType("sports");
		t2.setSubject("Athletics");
		t2.setAssigneeId("23");
		List <String> test2 = new ArrayList<String>();
		test2.add("Ohio");
		t2.setTags(test2);
		
		ticketList.add(t2);
				
		List<User> userList = new ArrayList<User>();
		
		User u1 = new User();		
		u1.setId("24");
		u1.setName("Francisca");
		u1.setCreatedDate("2016-04-15T05:19:46-10:00");
		u1.setVerified("true");
		
		userList.add(u1);
		
		User u2 = new User();		
		u2.setId("23");
		u2.setName("Francisca");
		u2.setCreatedDate("2016-04-15T05:19:46-10:00");
		u2.setVerified("true");
		
		userList.add(u2);
		
		//assertNotNull(ticketRepoImplMock);
		
		when(ticketRepoImplMock.getAllTickets()).thenReturn(ticketList);
		when(userRepoImplMock.getAllUsers()).thenReturn(userList);
		//fail("Not yet implemented"); 
		ServiceIfc testService = new ServiceImpl(ticketRepoImplMock,userRepoImplMock);
		
		List<User> userResultList = testService.getUserDisplayResults("name","Francisca");
		assertThat(userResultList.size(),is(2));	
		assertThat(userResultList.get(0).getTickets().size(),is(0));
		assertThat(userResultList.get(1).getTickets().size(),is(1));
	}
	
	@Test
	void testSearchUserByNameFoundManyUsersManyTickets() {		
		/** Given : two users with same name
		 *  When  : search user by type = name
		 *  Then  : return both users AND multiple associated ticket
		 */
		List<Ticket> ticketList = new ArrayList<Ticket>();
		Ticket t1 = new Ticket();
		t1.setId("436bf9b0-1147-4c0a-8439-6f79833bff5b");
		t1.setCreatedAt("2016-04-28T11:19:34-10:00");
		t1.setType("incident");
		t1.setSubject("Olympics");
		t1.setAssigneeId("24");
		List <String> test = new ArrayList<String>();
		test.add("Ohio");
		t1.setTags(test);
		
		
		
		
		Ticket t2 = new Ticket();
		t2.setId("axyzbf9b0-1147-4c0a-8439-6f79833bfabc");
		t2.setCreatedAt("2016-04-28T11:19:34-10:00");
		t2.setType("sports");
		t2.setSubject("Athletics");
		t2.setAssigneeId("23");
		List <String> test2 = new ArrayList<String>();
		test2.add("Ohio");
		t2.setTags(test2);
		
		Ticket t3 = new Ticket();
		t3.setId("b436bf9b0-1147-4c0a-8439-6f79833bff5b");
		t3.setCreatedAt("2016-04-28T11:19:34-10:00");
		t3.setType("incident");
		t3.setSubject("Swimming");
		t3.setAssigneeId("24");
		List <String> test3 = new ArrayList<String>();
		test3.add("Ohio");
		t3.setTags(test3);
		
		Ticket t4 = new Ticket();
		t4.setId("cxyzbf9b0-1147-4c0a-8439-6f79833bfabc");
		t4.setCreatedAt("2016-04-28T11:19:34-10:00");
		t4.setType("sports");
		t4.setSubject("Chess");
		t4.setAssigneeId("23");
		List <String> test4 = new ArrayList<String>();
		test4.add("Ohio");
		t4.setTags(test4);
		
		
		ticketList.add(t1);
		ticketList.add(t2);
		ticketList.add(t3);
		ticketList.add(t4);
				
		List<User> userList = new ArrayList<User>();
		
		User u1 = new User();		
		u1.setId("24");
		u1.setName("Francisca");
		u1.setCreatedDate("2016-04-15T05:19:46-10:00");
		u1.setVerified("true");
		
		userList.add(u1);
		
		User u2 = new User();		
		u2.setId("23");
		u2.setName("Francisca");
		u2.setCreatedDate("2016-04-15T05:19:46-10:00");
		u2.setVerified("true");
		
		userList.add(u2);
		
		//assertNotNull(ticketRepoImplMock);
		
		when(ticketRepoImplMock.getAllTickets()).thenReturn(ticketList);
		when(userRepoImplMock.getAllUsers()).thenReturn(userList);
		//fail("Not yet implemented"); 
		ServiceIfc testService = new ServiceImpl(ticketRepoImplMock,userRepoImplMock);
		
		List<User> userResultList = testService.getUserDisplayResults("name","Francisca");
		assertThat(userResultList.size(),is(2));	
		assertThat(userResultList.get(0).getTickets().size(),is(2));
		assertThat(userResultList.get(1).getTickets().size(),is(2));
	}

}
