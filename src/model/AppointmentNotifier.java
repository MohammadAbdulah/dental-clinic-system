package model;

public class AppointmentNotifier extends ServiceNotifier{

	public void alert(String message, String mailTo) {
		this.notifyService(message, mailTo);
	}
}
