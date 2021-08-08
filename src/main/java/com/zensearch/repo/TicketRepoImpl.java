package com.zensearch.repo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zensearch.pojo.Ticket;

public class TicketRepoImpl implements TicketRepoIfc {
	private List<Ticket> masterTicketList = new ArrayList<Ticket>();
	//Map<String, Ticket> masterTicketMap = new HashMap<String, Ticket>();
	
	private void loadTicketData() {
		InputStreamReader ticketReader = null;
		InputStream ticketInputStream = null;
		try {			
			ticketInputStream = TicketRepoImpl.class.getClassLoader().getResourceAsStream(ticketFileName);
			ticketReader = new InputStreamReader(ticketInputStream,encoding);
			masterTicketList  = new Gson().fromJson(ticketReader, new TypeToken<List<Ticket>>() {}.getType());
			ticketReader.close();			
			/** make the map unmodifiable **/
			Collections.unmodifiableList(masterTicketList);

		} catch (IOException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			IOUtils.closeQuietly(ticketInputStream);
		}
	}
	
	public List<Ticket> getAllTickets(){
		/** if data has not been loaded then load , otherwise simply return loaded list of Tickets **/
		if(null == masterTicketList || masterTicketList.size() <= 0) {
			loadTicketData();
		}
		return masterTicketList;
	}
}

