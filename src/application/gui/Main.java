package application.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main extends Application {
	public static Clinic clinic = Clinic.getInstance();
	public static AppointmentNotifier notifier = new AppointmentNotifier();

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root, 1366, 768);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException, IOException, Exception {
		getData();
		EmailService service = new EmailService();
		notifier.attach(service);
		launch(args);
	}

	public static void getData() throws FileNotFoundException, InterruptedException {
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					readFromPatientsFile();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					readFromDoctorsFile();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		});

		t1.start();
		t2.start();
		t1.join();
		t2.join();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					readFromAppointmentsFile();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public static void readFromPatientsFile() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(System.getProperty("user.dir") + "\\data\\patients.txt"));
		Object lock = new Object();
		synchronized (lock) {
			Patient.setAutoIncrementId(0);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] splittedLine = line.split(",");
				Patient patient = new Patient.Builder().setName(splittedLine[0]).setPhoneNumber(splittedLine[1])
						.setEmail(splittedLine[2]).build();
				patient.setOwedBalance(Double.parseDouble(splittedLine[3]));
				patient.setTotalPaidBalance(Double.parseDouble(splittedLine[4]));
				if (splittedLine.length > 4)
					for (int i = 5; i < splittedLine.length; i += 4) {
						String[] date = splittedLine[i + 2].split("-");
						String[] time = splittedLine[i + 3].split(":");
						Visit v = new Visit.Builder().setCost(Double.parseDouble(splittedLine[i]))
								.setDescription(splittedLine[i + 1])
								.setAppointmentDate(LocalDateTime.of(Integer.parseInt(date[0]),
										Integer.parseInt(date[1]), Integer.parseInt(date[2]), Integer.parseInt(time[0]),
										Integer.parseInt(time[1])))
								.build();
						patient.addVisitDetails(v);
					}
				clinic.createPatient(patient);
			}
		}
		scanner.close();
	}

	public static void readFromDoctorsFile() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(System.getProperty("user.dir") + "\\data\\doctors.txt"));
		Object lock = new Object();
		synchronized (lock) {
			Doctor.setAutoIncrementId(0);
			while (scanner.hasNextLine()) {
				List<Schedule> workSchedule = new ArrayList<Schedule>();
				String line = scanner.nextLine();
				String[] splittedLine = line.split(",");
				Doctor doctor = new Doctor(splittedLine[0]);
				clinic.createDoctor(doctor);
				if (splittedLine.length > 1)
					for (int i = 1; i < splittedLine.length; i += 3) {
						String[] splitDate = splittedLine[i].split("-");
						String[] splitStartTime = splittedLine[i + 1].split(":");
						String[] splitEndTime = splittedLine[i + 2].split(":");
						LocalDateTime date = LocalDateTime.of(Integer.parseInt(splitDate[0]),
								Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[2]), 0, 0);
						LocalTime start = LocalTime.of(Integer.parseInt(splitStartTime[0]),
								Integer.parseInt(splitStartTime[1]));
						LocalTime end = LocalTime.of(Integer.parseInt(splitEndTime[0]),
								Integer.parseInt(splitEndTime[1]));
						Schedule schedule = new Schedule(date, start, end);
						workSchedule.add(schedule);
					}
				Collections.sort(workSchedule);
				doctor.addWorkSchedule(workSchedule);
			}
		}
		scanner.close();
	}

	public static void readFromAppointmentsFile() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(System.getProperty("user.dir") + "\\data\\appointments.txt"));
		Object lock = new Object();
		synchronized (lock) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] splittedLine = line.split(",");
				IPatient p = clinic.findPatientById(Integer.parseInt(splittedLine[0]));
				IDoctor d = clinic.findDoctorById(Integer.parseInt(splittedLine[1]));
				LocalDateTime time = splitDate(splittedLine);
				Status s = Status.valueOf(splittedLine[2]);
				clinic.loadAppointments(p, d, s, time);
			}
		}
		scanner.close();
	}

	public static void writeToPatientFile() throws IOException {
		Object lock = new Object();
		synchronized (lock) {
			FileWriter writer = new FileWriter(System.getProperty("user.dir") + "\\data\\patients.txt", false);
			StringBuilder sb = new StringBuilder();

			for (IPatient p : Main.clinic.getPatients())
				sb.append(p.toString() + "\n");

			writer.write(sb.toString());
			writer.close();
		}
	}

	public static void writeToDoctorFile() throws IOException {
		Object lock = new Object();
		synchronized (lock) {
			FileWriter writer = new FileWriter(System.getProperty("user.dir") + "\\data\\doctors.txt", false);
			StringBuilder sb = new StringBuilder();

			for (IDoctor d : clinic.getDoctors())
				sb.append(d.toString() + "\n");

			writer.write(sb.toString());
			writer.close();
		}
	}

	public static void writeToAppointmentFile() throws IOException {
		Object lock = new Object();
		synchronized (lock) {
			FileWriter writer = new FileWriter(System.getProperty("user.dir") + "\\data\\appointments.txt", false);
			for (Appointment a : clinic.getAppointments())
				writer.write(a + "\n");
			writer.close();
		}
	}

	public static LocalDateTime splitDate(String[] date) {
		String[] splitDate = date[3].split("-");
		String[] splitTime = date[4].split(":");

		return LocalDateTime.of(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]),
				Integer.parseInt(splitDate[2]), Integer.parseInt(splitTime[0]), Integer.parseInt(splitTime[1]));
	}
}
