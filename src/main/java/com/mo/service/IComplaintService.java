package com.mo.service;

import com.mo.exceptions.ComplaintsException;
import com.mo.model.Property;
import com.mo.model.User;
import com.mo.requestDTO.ComplaintRequest;

import jakarta.mail.MessagingException;

public interface IComplaintService {

	public String createComplaint(ComplaintRequest request, User user, Property property) throws ComplaintsException, MessagingException;
	
	public void forwardComplaintEmail(String complaintId, ComplaintRequest request, User user, Property property) throws MessagingException;
	
}
