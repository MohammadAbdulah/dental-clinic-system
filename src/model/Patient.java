package model;

import java.util.ArrayList;
import java.util.List;

public class Patient implements IPatient{
	private static int autoIncrementId = 0;
	private final int id;
	private double owedBalance = 0.0;
	private double totalPaidBalance = 0.0;
	private List<Visit> previousVisits;
	private Attribute attributes = new Attribute();
	
	public Patient(Builder builder) {
		attributes.name = builder.attributes.name;
		attributes.email = builder.attributes.email;
		attributes.phoneNumber = builder.attributes.phoneNumber;
		id = autoIncrementId++;
		previousVisits = new ArrayList<Visit>();
	}
	
	public static int getAutoIncrementId() {
		return autoIncrementId;
	}
	
	public static void setAutoIncrementId(int autoIncrementId) {
		Patient.autoIncrementId = autoIncrementId;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return attributes.name;
	}
	
	public String getPhoneNumber() {
		return attributes.phoneNumber;
	}
	
	public String getEmail() {
		return attributes.email;
	}
	
	public double getOwedBalance() {
		return owedBalance;
	}
	
	public void setOwedBalance(double owedBalance) {
		this.owedBalance = owedBalance;
	}
	
	public double getTotalPaidBalance() {
		return totalPaidBalance;
	}
	
	public void setTotalPaidBalance(double totalPaidBalance) {
		this.totalPaidBalance = totalPaidBalance;
	}
	
	public List<Visit> getPreviousVisits() {
		return previousVisits;
	}
	
	public void setPreviousVisits(List<Visit> previousVisits) {
		this.previousVisits = previousVisits;
	}
	
	@Override
	public String printVisitDetails(Visit visit) {
		return "Cost: " + visit.getCost() + "\nDate: " + visit.getAppointmentDate().getYear() 
				+ "-" + visit.getAppointmentDate().getMonth().toString().substring(0,3) + "-" + visit.getAppointmentDate().getDayOfMonth()
				+"\nTime: " + visit.getAppointmentDate().getHour() + ":" + visit.getAppointmentDate().getMinute()
				+"\nDescription: " + visit.getDescription();
	}
	
	@Override
	public void chargePatient(Double amount) {
		this.owedBalance += amount;
	}

	@Override
	public void addVisitDetails(Visit visit) {
		previousVisits.add(visit);
	}
	
	@Override
	public Visit findVisitById(int id) {
		 return previousVisits.get(id);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(attributes.toString() + "," + getOwedBalance() + "," + getTotalPaidBalance() + ",");
		
		for(Visit v : previousVisits) 
			sb.append(v.toString());
		
		return sb.toString();
	}

	public static class Builder{
		private Attribute attributes = new Attribute();
		
		public Builder setPhoneNumber(String phoneNumber) {
			attributes.phoneNumber = phoneNumber;
			return this;
		}
		
		public Builder setEmail(String email) {
			attributes.email = email;
			return this;
		}
		
		public Builder setName(String name) {
			attributes.name = name;
			return this;
		}
		
		public Patient build() {
			return new Patient(this);
		}
	}
	
	public static class Attribute{
		private String name;
		private String phoneNumber = "no-email";
		private String email;
		@Override
		public String toString() {
			return name + "," + phoneNumber + "," + email;
		}
	}
}
