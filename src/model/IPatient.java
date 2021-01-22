package model;

public interface IPatient {
	String printVisitDetails(Visit visit);
	void addVisitDetails(Visit visit);
	void chargePatient(Double amount);
	Visit findVisitById(int id);
}
