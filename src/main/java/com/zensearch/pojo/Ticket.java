package com.zensearch.pojo;

import java.util.List;

public class Ticket {
	private String _id;
	private String type;
	private String subject;
	private String assignee_id;
	private List<String>  tags;
	private String created_at;
	private String assigneeName;
	
	
	public String getId() {
		return _id;
	}
	public void setId(String id) {
		this._id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getAssigneeId() {
		return assignee_id;
	}
	public void setAssigneeId(String assigneeId) {
		this.assignee_id = assigneeId;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public String getCreatedAt() {
		return created_at;
	}
	public void setCreatedAt(String createdAt) {
		this.created_at = createdAt;
	}
	
	public String getAssigneeName() {
		return assigneeName;
	}
	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}
	
	@Override
	public String toString() {
		return "Ticket [_id=" + _id + ", type=" + type + ", subject=" + subject + ", assigneeId=" + assignee_id
				+ ", tags=" + tags + ", created_at=" + created_at + ", assigneeName=" + assigneeName + "]";
	}

}
