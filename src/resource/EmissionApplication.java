	package resource;
	
	import javax.ws.rs.ApplicationPath;
	import javax.ws.rs.core.Application;
	
	@ApplicationPath("/api")
	public class EmissionApplication extends Application {
	    // JAX-RS discovers resources automatically
	}
