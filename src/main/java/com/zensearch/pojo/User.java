package com.zensearch.pojo;

import java.util.ArrayList;
import java.util.List;

public class User {
	private String _id;
	private String name;
	private String created_at;
	private String verified;
	List<String> tickets = new ArrayList<String>();
	
	public String getId() {
		return _id;
	}
	public void setId(String id) {
		this._id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreatedDate() {
		return created_at;
	}
	public void setCreatedDate(String createdDate) {
		this.created_at = createdDate;
	}
	public String getVerified() {
		return verified;
	}
	public void setVerified(String verified) {
		this.verified = verified;
	}
	
	public List<String> getTickets() {
		return tickets;
	}
	public void setTickets(List<String> tickets) {
		this.tickets = tickets;
	}
	
	@Override
	public String toString() {
		return "User [_id=" + _id + ", name=" + name + ", created_at=" + created_at + ", verified=" + verified
				+ ", tickets=" + tickets + "]";
	}
	
}
