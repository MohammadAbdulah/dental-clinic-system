package model;

import application.gui.Main;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Clinic {
    private List<IPatient> patients;
    private List<IDoctor> doctors;
    private List<Appointment> appointments;
    private static Clinic instance;
    private static Object lock = new Object();

    private Clinic() {
        patients = new ArrayList<IPatient>();
        doctors = new ArrayList<IDoctor>();
        appointments = new ArrayList<Appointment>();
    }

    public static Clinic getInstance() {
        if (instance == null)
            synchronized (lock) {
                if (instance == null)
                    instance = new Clinic();
            }
        return instance;
    }

    public void createPatient(IPatient patient) {
        patients.add(patient);
    }

    public void createDoctor(IDoctor doctor) {
        doctors.add(doctor);
    }

    public void deletePatient(int id) {
        patients.remove(id);
    }

    public void deleteDoctor(int id) {
        doctors.remove(id);
    }

    public void deleteDoctorWorkSchedule(int id, int index) {
        Doctor d = (Doctor) findDoctorById(id);
        Schedule toDelete = d.getWorkSchedule().get(index);
        generateAffectedPatients(id, toDelete);
        d.getWorkSchedule().remove(index);
    }

    public void generateAffectedPatients(int doctorId, Schedule toDelete) {
        List<IPatient> affectedPatients = new ArrayList<IPatient>();

        int i = 0;
        for (Appointment a : getAppointments()) {
            if (((Doctor) a.getDoctor()).getId() == doctorId)
                if (a.getDate().getScheduledDateTime().getYear() == toDelete.getScheduledDateTime().getYear()
                        && a.getDate().getScheduledDateTime().getMonthValue() == toDelete.getScheduledDateTime()
                        .getMonthValue()
                        && a.getDate().getScheduledDateTime().getDayOfMonth() == toDelete.getScheduledDateTime()
                        .getDayOfMonth()) {
                    affectedPatients.add(a.getPatient());
                    a.setStatus(Status.CANCELED);

                    String mailTo = ((Patient) Main.clinic.findAppointmentById(i).getPatient()).getEmail();
                    if (mailTo != null)
                        Main.notifier.alert("Your appointment has been cancelled, because doctor's work schedule has been canceled", mailTo);
                }
            i++;
        }

        for (IPatient p : affectedPatients)
            System.out.println(((Patient) p).getName());
    }

    public IPatient findPatientById(int id) {
        for (IPatient p : patients)
            if (((Patient) p).getId() == id)
                return p;

        return null;
    }

    public IDoctor findDoctorById(int id) {
        for (IDoctor d : doctors)
            if (((Doctor) d).getId() == id)
                return d;

        return null;
    }

    public Appointment findAppointmentById(int id) {
        for (int i = 0; i < appointments.size(); i++)
            if (i == id)
                return appointments.get(i);

        return null;
    }

    public void scheduleAppointment(IPatient patient, IDoctor doctor, Status status, LocalDateTime date) {
        if (doctor.isWorkingHours(date) == false) {
            Appointment a = new Appointment(patient, doctor, date);
            a.setStatus(status);
            appointments.add(a);
        }
    }

    public void loadAppointments(IPatient patient, IDoctor doctor, Status status, LocalDateTime date) {
        Appointment a = new Appointment(patient, doctor, date);
        a.setStatus(status);
        appointments.add(a);
    }

    public void closeAppointment(int id) {
        appointments.get(id).setStatus(Status.ENDED);
    }

    public void cancelAppointment(int id) {
        appointments.get(id).setStatus(Status.CANCELED);
    }

    public List<IPatient> getPatients() {
        return patients;
    }

    public void setPatients(List<IPatient> patients) {
        this.patients = patients;
    }

    public List<IDoctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<IDoctor> doctors) {
        this.doctors = doctors;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
