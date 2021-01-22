package model;

import java.time.LocalDateTime;

public class Appointment {
	private Schedule date;
	private IPatient patient;
	private IDoctor doctor;
	private Status status;
	
	public Appointment(IPatient patient, IDoctor doctor, LocalDateTime date) {
		this.patient = patient;
		this.doctor = doctor;
		this.date = new Schedule(date);
		this.status = Status.ONGOING;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Schedule getDate() {
		return date;
	}
	
	public void setDate(Schedule date) {
		this.date = date;
	}
	
	public IPatient getPatient() {
		return patient;
	}
	
	public void setPatient(IPatient patient) {
		this.patient = patient;
	}
	
	public IDoctor getDoctor() {
		return doctor;
	}
	
	public void setDoctor(IDoctor doctor) {
		this.doctor = doctor;
	}

	@Override
	public String toString() {
		return ((Patient) patient).getId() + "," + ((Doctor) doctor).getId() + "," + status + "," + date.getScheduledDateTime().getYear() 
				+ "-" + date.getScheduledDateTime().getMonthValue() + "-" + date.getScheduledDateTime().getDayOfMonth() + "," + date.getStartTime().getHour() + ":" + date.getStartTime().getMinute();
	}
}
