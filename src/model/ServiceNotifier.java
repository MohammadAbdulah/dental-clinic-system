package model;

import java.util.ArrayList;
import java.util.List;

public abstract class ServiceNotifier {
	private List<ServiceEE> services = new ArrayList<ServiceEE>();
	
	public void attach(ServiceEE service) {
		services.add(service);
	};
	
	public void detach(ServiceEE service) {
		services.remove(service);
	};
	
	protected void notifyService(String message, String mailTo) {
		if(services.isEmpty())
			System.out.println("No attached services");
		
		for(ServiceEE service: services)
			service.update(message, mailTo);
	};
}
