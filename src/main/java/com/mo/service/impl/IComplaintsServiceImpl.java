package com.mo.service.impl;

import org.springframework.stereotype.Service;

import com.mo.exceptions.ComplaintsException;
import com.mo.model.BookingDetails;
import com.mo.model.Complaints;
import com.mo.model.Property;
import com.mo.model.User;
import com.mo.repository.BookingDetailsRepository;
import com.mo.repository.ComplaintsRepository;
import com.mo.repository.PropertyRepository;
import com.mo.requestDTO.ComplaintRequest;
import com.mo.service.IComplaintService;
import com.mo.service.IEmailService;
import com.mo.utils.HelperUtil;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IComplaintsServiceImpl implements IComplaintService {

	private final ComplaintsRepository compRepo;
	private final IEmailService emailService;
	private final BookingDetailsRepository bookingRepo;
	private final PropertyRepository propertyRepo;

	@Override
	public String createComplaint(ComplaintRequest request, User user, Property property)
			throws ComplaintsException, MessagingException {

		BookingDetails bookingDetails = bookingRepo.findByBookingId(request.getBookingId());
		if (bookingDetails.getUser().getId() != user.getId()) {
			throw new ComplaintsException("Booking Details not found with the provided booking Id.");
		}

		Complaints complaints = new Complaints();
		complaints.setUser(user);
		complaints.setProperty(property);
		complaints.setComplaintInfo(request.getComplaintText());
		complaints.setBookingId(request.getBookingId());
		String complaintId = HelperUtil.generateComplaintId();
		complaints.setComplaintNumber(complaintId);
		compRepo.save(complaints);

		property.getComplaints().add(complaints);
		propertyRepo.save(property);

		this.forwardComplaintEmail(complaintId, request, user, property);

		return complaintId;
	}

	@Override
	public void forwardComplaintEmail(String complaintId, ComplaintRequest request, User user, Property property)
			throws MessagingException {
		String subject = "Complaint raised regarding recent stay. ComplaintId :- ( " + complaintId + " ).";

		String text = "Dear, " + property.getName()
				+ " our user has raised a complaint regarding their stay at your property. \n" + "Booking Id :- ( "
				+ request.getBookingId() + " ). \nUser quote:- \n'" + request.getComplaintText() + "'."
				+ "We are looking forward to resolve the issue as early as possible.\n" + "Regards,\n -| Stakation |-";

		emailService.sendComplaintRaisedEmail(property.getContactEmail(), user.getEmail(), text, subject);

	}

}
