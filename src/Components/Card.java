package Components;

import application.gui.Fxmloader;
import application.gui.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.Appointment;
import model.Doctor;
import model.Patient;
import model.Visit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Card {
	public static int doctorId;
	public static int patientId;
	public static int appointmentId;
	public int id;

	public void getAppointmentCard(Pane view, String day, String date, String start, String end, String status, int i, AnchorPane root) {
		int id = i;
		CheckBox select = new CheckBox();
		select.setLayoutX(23);
		select.setLayoutY(12);

		Button cancel = new Button("CANCEL");
		cancel.setFont(Font.font("Arial Bold", 12));
		cancel.setPrefSize(80, 30);
		cancel.setLayoutX(670);
		cancel.setLayoutY(5);
		cancel.setStyle("-fx-background-color: #9D9D9D;-fx-text-fill: #FFFFFF;");
		if (status.equals("ONGOING")) {
			cancel.setStyle("-fx-background-color: #0067EE;-fx-text-fill: #FFFFFF;");
			cancel.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					appointmentId = i;
					Main.clinic.cancelAppointment(id);
					loadNewScene("Appointment", root);
					String mailTo = ((Patient)Main.clinic.findAppointmentById(i).getPatient()).getEmail();
					if(mailTo != null)
						Main.notifier.alert("Your appointment has been cancelled", mailTo);
				}
			});
		}

		Button endBtn = new Button("END");
		endBtn.setFont(Font.font("Arial Bold", 12));
		endBtn.setPrefSize(60, 30);
		endBtn.setLayoutX(760);
		endBtn.setLayoutY(5);
		endBtn.setStyle("-fx-background-color: #9D9D9D;-fx-text-fill: #FFFFFF;");
		if (status.equals("ONGOING")) {
			endBtn.setStyle("-fx-background-color: #0067EE;-fx-text-fill: #FFFFFF;");
			endBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					appointmentId = i;
					Main.clinic.closeAppointment(id);
					try {
						Main.writeToAppointmentFile();
						Main.clinic.getAppointments().clear();
						Main.readFromAppointmentsFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
					loadNewScene("Appointment", root);
				}
			});
		}

		Text patientName = new Text(day);
		patientName.setLayoutX(67);
		patientName.setLayoutY(25);// 35
		patientName.setFont(Font.font("Arial", 14));

		Text doctorName = new Text(date);
		doctorName.setLayoutX(225);
		doctorName.setLayoutY(25);// 35
		doctorName.setFont(Font.font("Arial", 14));

		Text appointmentDate = new Text(start);
		appointmentDate.setLayoutX(357);
		appointmentDate.setLayoutY(25);// 35
		appointmentDate.setFont(Font.font("Arial", 14));

		Text endDate = new Text(end);
		endDate.setLayoutX(460);
		endDate.setLayoutY(25);// 35
		endDate.setFont(Font.font("Arial", 14));

		Text statusText = new Text(status);
		statusText.setLayoutX(565);
		statusText.setLayoutY(25);// 35
		statusText.setFont(Font.font("Arial", 14));
		if(status.equals("ONGOING"))
			statusText.setStyle("-fx-text-fill: #0067EE;");
		else if(status.equals("CANCELED"))
			statusText.setStyle("-fx-text-fill: #FF2020;");
		else
			statusText.setStyle("-fx-text-fill: #29FF00;");

		view.getChildren().addAll(patientName, doctorName, appointmentDate, endDate, select, endBtn, cancel,
				statusText);
		view.setPrefSize(842, 43);// 61
		view.setMaxSize(842, 43);// 61
		view.setMinSize(842, 43);// 61
		view.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-margin: 20 3 3 0;");
	}

	public void getDashboardCard(Pane view, Appointment a) {
		Text patientName = new Text(((Patient) a.getPatient()).getName());
		patientName.setLayoutX(67);
		patientName.setLayoutY(25);// 35
		patientName.setFont(Font.font("Arial", 14));

		Text doctorName = new Text(((Doctor) a.getDoctor()).getName());
		doctorName.setLayoutX(299);
		doctorName.setLayoutY(25);// 35
		doctorName.setFont(Font.font("Arial", 14));

		Text appointmentDate = new Text(a.getDate().getScheduledDateTime().getDayOfMonth() + "-"
				+ a.getDate().getScheduledDateTime().getMonth().toString().substring(0, 3));
		appointmentDate.setLayoutX(508);
		appointmentDate.setLayoutY(25);// 35
		appointmentDate.setFont(Font.font("Arial", 14));

		Text status = new Text(a.getStatus() + "");
		status.setLayoutX(690);
		status.setLayoutY(25);// 35
		status.setFont(Font.font("Arial", 14));

		view.getChildren().addAll(patientName, doctorName, appointmentDate, status);
		view.setPrefSize(842, 43);// 61
		view.setMaxSize(842, 43);// 61
		view.setMinSize(842, 43);// 61
		view.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-margin: 20 3 3 0;");
	}

	public void getPatientCard(Pane view, String patient, String email, String phoneNumber, double owed, double paid,
			int idNum, AnchorPane root) {
		CheckBox select = new CheckBox();
		select.setLayoutX(23);
		select.setLayoutY(12);

		Text patientName = new Text(patient);
		patientName.setLayoutX(67);
		patientName.setLayoutY(25);// 35
		patientName.setFont(Font.font("Arial", 14));

		Text doctorName = new Text(email);
		doctorName.setLayoutX(290);
		doctorName.setLayoutY(25);// 35
		doctorName.setFont(Font.font("Arial", 14));

		Text appointmentDate = new Text(phoneNumber);
		appointmentDate.setLayoutX(533);
		appointmentDate.setLayoutY(25);// 53
		appointmentDate.setFont(Font.font("Arial", 14));

		Text owedText = new Text(owed + "");
		owedText.setLayoutX(700);
		owedText.setLayoutY(25);// 53
		owedText.setFont(Font.font("Arial", 14));

		Text paidText = new Text(paid + "");
		paidText.setLayoutX(785);
		paidText.setLayoutY(25);// 53
		paidText.setFont(Font.font("Arial", 14));

		view.getChildren().addAll(patientName, doctorName, appointmentDate, select, owedText, paidText);
		view.setPrefSize(842, 43);// 61
		view.setMaxSize(842, 43);// 61
		view.setMinSize(842, 43);// 61
		view.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-margin: 20 3 3 0;");
		view.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				patientId = idNum;
				loadNewScene("PatientDetails", root);
			}
		});
	}

	public void getPatientDetailsCard(Pane view, Visit v, int id) throws FileNotFoundException {
		Button print = new Button();
		print.setPrefSize(37, 32);
		print.setStyle("-fx-background-color: #0067EE;");
		FileInputStream input = new FileInputStream(System.getProperty("user.dir") + "\\images\\print.png");
		ImageView image = new ImageView(new Image(input));
		image.setFitWidth(22);
		image.setFitHeight(19);
		print.setGraphic(image);
		print.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Patient p = (Patient) Main.clinic.findPatientById(patientId);
				System.out.println(p.printVisitDetails(p.findVisitById(id)));
			}
		});
		print.setLayoutX(750);
		print.setLayoutY(14);

		Text cost = new Text("Cost");
		cost.setLayoutX(48);
		cost.setLayoutY(25);
		cost.setFont(Font.font("Arial", 14));

		Text costText = new Text(v.getCost() + "");
		costText.setLayoutX(144);
		costText.setLayoutY(25);
		costText.setFont(Font.font("Arial", 14));

		Text date = new Text("Date");
		date.setLayoutX(48);
		date.setLayoutY(47);
		date.setFont(Font.font("Arial", 14));

		Text time = new Text("Time");
		time.setLayoutX(48);
		time.setLayoutY(69);
		time.setFont(Font.font("Arial", 14));

		Text desc = new Text("Description");
		desc.setLayoutX(48);
		desc.setLayoutY(89);
		desc.setFont(Font.font("Arial", 14));

		Text descText = new Text(v.getDescription());
		descText.setLayoutX(144);
		descText.setLayoutY(89);
		descText.setFont(Font.font("Arial", 14));

		Text timeText = new Text(v.getAppointmentDate().getHour() + ":" + v.getAppointmentDate().getMinute());
		timeText.setLayoutX(144);
		timeText.setLayoutY(69);
		timeText.setFont(Font.font("Arial", 14));

		Text dateText = new Text(
				v.getAppointmentDate().getYear() + "-" + v.getAppointmentDate().getMonth().toString().substring(0, 3)
						+ "-" + v.getAppointmentDate().getDayOfMonth());
		dateText.setLayoutX(144);
		dateText.setLayoutY(47);
		dateText.setFont(Font.font("Arial", 14));

		view.getChildren().addAll(date, time, desc, cost, costText, print, descText, dateText, timeText);
		view.setPrefSize(842, 106);
		view.setMaxSize(842, 106);
		view.setMinSize(842, 106);
		view.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-margin: 20 3 3 0;");
	}

	public void getDoctorCard(Pane view, String doctor, String num, AnchorPane root, int i, int numOfApp) {
		Button viewBtn = new Button("VIEW");
		viewBtn.setPrefSize(115, 34);
		viewBtn.setStyle("-fx-background-color: #0067EE;-fx-text-fill: #FFFFFF;");
		viewBtn.setLayoutX(650);
		viewBtn.setLayoutY(5);
		viewBtn.setFont(Font.font("Arial Bold", 14));
		viewBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				doctorId = i;
				loadNewScene("DoctorWorkSchedule", root);
			}
		});

		CheckBox select = new CheckBox();
		select.setLayoutX(23);
		select.setLayoutY(12);

		Text patientName = new Text(doctor);
		patientName.setLayoutX(67);
		patientName.setLayoutY(25);// 35
		patientName.setFont(Font.font("Arial", 14));

		Text doctorName = new Text(num);
		doctorName.setLayoutX(325);// 299
		doctorName.setLayoutY(25);// 35
		doctorName.setFont(Font.font("Arial", 14));

		Text numOfAppointment = new Text(numOfApp + "");
		numOfAppointment.setLayoutX(500);// 299
		numOfAppointment.setLayoutY(25);// 35
		numOfAppointment.setFont(Font.font("Arial", 14));

		view.getChildren().addAll(patientName, doctorName, viewBtn, select, numOfAppointment);
		view.setPrefSize(842, 43);// 61
		view.setMaxSize(842, 43);// 61
		view.setMinSize(842, 43);// 61
		view.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-margin: 20 3 3 0;");
	}

	public void getWorkScheduleCard(Pane view, String day, String date, String start, String end, int idNum) {
		CheckBox select = new CheckBox();
		select.setLayoutX(23);
		select.setLayoutY(12);

		Text dayName = new Text(day);
		dayName.setLayoutX(75);
		dayName.setLayoutY(25);// 35
		dayName.setFont(Font.font("Arial", 14));

		Text dateName = new Text(date);
		dateName.setLayoutX(285);
		dateName.setLayoutY(25);// 35
		dateName.setFont(Font.font("Arial", 14));

		Text appointmentDate = new Text(start);
		appointmentDate.setLayoutX(520);
		appointmentDate.setLayoutY(25);// 53
		appointmentDate.setFont(Font.font("Arial", 14));

		Text endDate = new Text(end);
		endDate.setLayoutX(725);
		endDate.setLayoutY(25);// 53
		endDate.setFont(Font.font("Arial", 14));

		view.getChildren().addAll(dayName, dateName, appointmentDate, endDate, select);
		view.setPrefSize(842, 43);// 61
		view.setMaxSize(842, 43);// 61
		view.setMinSize(842, 43);// 61
		view.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-margin: 20 3 3 0;");
	}

	private void loadNewScene(String fileName, AnchorPane root) {
		Fxmloader newScene = new Fxmloader();
		Pane view = newScene.loadPage(fileName);
		root.getChildren().removeAll(root.getChildren());
		root.getChildren().add(view);
	}
}
