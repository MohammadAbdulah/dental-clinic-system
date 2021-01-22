package model;

import java.time.LocalDateTime;

public class Visit {
	private LocalDateTime appointment;
	private double cost;
	private String description;
	
	private Visit(Builder builder) {
		appointment = builder.appointment;
		cost = builder.cost;
		description = builder.description;
	}
	
	public LocalDateTime getAppointmentDate() {
		return appointment;
	}

	public double getCost() {
		return cost;
	}
	
	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return cost + "," + description + "," + appointment.getYear() + "-" + appointment.getMonthValue() +
				 "-" + appointment.getDayOfMonth() + "," + appointment.getHour() + ":" + appointment.getMinute();
	}

	public static class Builder{
		private LocalDateTime appointment;
		private double cost;
		private String description;
		
		public Builder setDescription(String description) {
			this.description = description;
			return this;
		}
		
		public Builder setCost(double cost) {
			this.cost = cost;
			return this;
		}
		
		public Builder setAppointmentDate(LocalDateTime appointment) {
			this.appointment = appointment;
			return this;
		}
		
		public Visit build() {
			return new Visit(this);
		}
	}
}
