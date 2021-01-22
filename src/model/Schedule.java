package model;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Schedule implements Comparable<Schedule> {
	private LocalDateTime scheduledDateTime;
	private LocalTime startTime;
	private LocalTime endTime;
	private int duration;

	public Schedule(LocalDateTime scheduledDateTime) {
		this.scheduledDateTime = scheduledDateTime;
		startTime = LocalTime.of(scheduledDateTime.getHour(), scheduledDateTime.getMinute());
		duration = 30;
		endTime = startTime.plusMinutes(duration);
	}

	public Schedule(LocalDateTime scheduledDateTime, LocalTime startTime, LocalTime endTime) {
		this.scheduledDateTime = scheduledDateTime;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public LocalDateTime getScheduledDateTime() {
		return scheduledDateTime;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public void setScheduledDateTime(LocalDateTime scheduledDateTime) {
		this.scheduledDateTime = scheduledDateTime;
	}

	@Override
	public String toString() {
		return "Schedule [scheduledDateTime=" + scheduledDateTime + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", duration=" + duration + "]";
	}

	@Override
	public int compareTo(Schedule obj) {
		if (this.getScheduledDateTime().getYear() > obj.getScheduledDateTime().getYear())
			return 1;
		if (this.getScheduledDateTime().getMonthValue() > obj.getScheduledDateTime().getMonthValue())
			return 1;
		if (this.getScheduledDateTime().getDayOfMonth() > obj.getScheduledDateTime().getDayOfMonth())
			return 1;
		return -1;
	}
}
