package com.zensearch.service;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
class ServiceImplTest {
	
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
	void testSearchTicketsByType() {		
		/** Given : two tickets with same type ( "Incident' ) and same assignee 
		 *  When  : search tickets by type = Incident
		 *  Then  : return the two tickets with the same assignee
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
		
		
		List<Ticket> ticketResultList = testService.getTicketDisplayResults("type", "incident");
		assertThat(ticketResultList.size(),is(2));
		assertThat(ticketResultList.get(0).getAssigneeName(),is("Francisca Rasmussen"));
		System.out.println(ticketResultList);
	}
	
	@Test
	void testSearchTicketsById() {		
		/** Given : two tickets with different _id 
		 *  When  : search tickets by type = _id
		 *  Then  : return one ticket which matches _id
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
		
		
		List<Ticket> ticketResultList = testService.getTicketDisplayResults("436bf9b0-1147-4c0a-8439-6f79833bff5b");
		assertThat(ticketResultList.size(),is(1));
		assertThat(ticketResultList.get(0).getAssigneeName(),is("Michael Jordan"));
		assertThat(ticketResultList.get(0).getId(),is("436bf9b0-1147-4c0a-8439-6f79833bff5b"));
		
	}

}
