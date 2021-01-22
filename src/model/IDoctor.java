package model;

import java.time.LocalDateTime;
import java.util.List;

public interface IDoctor {
	void addWorkSchedule(List<Schedule> work);
	boolean isWorkingHours(LocalDateTime date);
}
