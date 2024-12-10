	package resource;
	
	import entity.Emission;
	import org.hibernate.Session;
	import org.hibernate.SessionFactory;
	import org.hibernate.Transaction;
	import org.hibernate.cfg.Configuration;
	
	import java.util.List;
	
	//DAO
	
	public class EmissionData {
	
	    public static void saveToDatabase(List<Emission> emissions) {
	        // Create a SessionFactory using Hibernate configuration file 
	        SessionFactory sessionFactory = new Configuration()
	                .configure("persistence.xml")  // Loads the configuration from hibernate.cfg.xml
	                .addAnnotatedClass(Emission.class)  // Add the annotated Emission class
	                .buildSessionFactory();  // Build the SessionFactory
	
	        // Open a session + begin the transaction
	        try (Session session = sessionFactory.openSession()) {
	            Transaction transaction = session.beginTransaction();
	            try {
	                // Iterate over the list of emissions and save each one
	                for (Emission emission : emissions) {
	                    session.save(emission);  // Save each emission to the database
	                }
	                transaction.commit();  // Commit the transaction
	                System.out.println("Successfully saved " + emissions.size() + " emissions to the database.");
	            } catch (Exception e) {
	                transaction.rollback();  // go back if there's an error
	                System.err.println("Error saving emissions: " + e.getMessage());
	                e.printStackTrace();
	            }
	        } catch (Exception e) {
	            System.err.println("Error establishing database connection: " + e.getMessage());
	            e.printStackTrace();
	        } finally {
	        	
	            // Close the SessionFactory after 
	            sessionFactory.close();
	        }
	    }
	}
