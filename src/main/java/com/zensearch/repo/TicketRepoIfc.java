package com.zensearch.repo;

import java.util.List;

import com.zensearch.pojo.Ticket;

public interface TicketRepoIfc {
	
	//List<Ticket> ticketList = new ArrayList<Ticket>();
	String ticketFileName = "tickets.json";
	String encoding = "UTF-8";
	
	
	
	 List<Ticket> getAllTickets();
}
