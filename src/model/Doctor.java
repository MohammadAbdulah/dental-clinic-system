package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import application.gui.Main;

public class Doctor implements IDoctor {
	private String name;
	private static int autoIncrementId = 0;
	private int id;
	private List<Schedule> workSchedule = new ArrayList<Schedule>();

	public Doctor(String name) {
		this.name = name;
		id = autoIncrementId++;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static int getAutoIncrementId() {
		return autoIncrementId;
	}

	public static void setAutoIncrementId(int autoIncrementId) {
		Doctor.autoIncrementId = autoIncrementId;
	}

	public int getId() {
		return id;
	}

	public List<Schedule> getWorkSchedule() {
		Collections.sort(workSchedule);
		return workSchedule;
	}

	public void setWorkSchedule(List<Schedule> workSchedule) {
		this.workSchedule = workSchedule;
	}

	@Override
	public void addWorkSchedule(List<Schedule> work) {
		for (Schedule s : work)
			if (!workSchedule.contains(s))
				workSchedule.add(s);
	}

	@Override
	public boolean isWorkingHours(LocalDateTime date) {
		String flag = "#";
		LocalTime t = LocalTime.of(date.getHour(), date.getMinute());
		LocalDate d = LocalDate.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
		boolean validDate = false;
		Schedule s1 = null;
		for (Schedule s : workSchedule) {
			LocalDate t1 = LocalDate.of(s.getScheduledDateTime().getYear(), s.getScheduledDateTime().getMonthValue(),
					s.getScheduledDateTime().getDayOfMonth());
			if (t1.equals(d)) {
				flag = "dateFound";
				validDate = true;
				s1 = s;
				break;
			}
		}

		if (validDate) {
			if (s1.getStartTime().compareTo(t) <= 0 && s1.getEndTime().compareTo(t) >= 0) {
				for (Appointment a : Main.clinic.getAppointments()) {

					LocalTime t2 = LocalTime.of(a.getDate().getStartTime().getHour(),
							a.getDate().getStartTime().getMinute());
					LocalDate d2 = LocalDate.of(a.getDate().getScheduledDateTime().getYear(),
							a.getDate().getScheduledDateTime().getMonth(),
							a.getDate().getScheduledDateTime().getDayOfMonth());
					if ((t2.equals(t) && d2.equals(d))) 
						flag = "false";
					else
						flag = "true";
				}
			}
		}

		if (flag.equals("true"))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name + ",");

		for (Schedule s : workSchedule) {
			sb.append(s.getScheduledDateTime().getYear() + "-" + s.getScheduledDateTime().getMonthValue() + "-"
					+ s.getScheduledDateTime().getDayOfMonth() + "," + s.getStartTime().getHour() + ":"
					+ s.getStartTime().getMinute() + "," + s.getEndTime().getHour() + ":" + s.getEndTime().getMinute()
					+ ",");
		}

		return sb.toString();
	}
}
