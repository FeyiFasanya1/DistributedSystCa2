	package resource;
	
	import com.fasterxml.jackson.databind.JsonNode;
	import com.fasterxml.jackson.databind.ObjectMapper;
	import entity.Emission;
	
	import javax.persistence.EntityManager;
	import javax.persistence.EntityManagerFactory;
	import javax.persistence.Persistence;
	import java.io.File;
	import java.io.IOException;
	import java.util.ArrayList;
	import java.util.List;
	
	public class EmissionJSONParser {
	
	    public static void main(String[] args) {
	        try {
	            
	            File jsonFile = new File("GreenhouseGasEmissions.json");
	
	            // Create ObjectMapper for parsing JSON
	            ObjectMapper objectMapper = new ObjectMapper();
	
	            // Parsing JSON file into a JsonNode tree
	            JsonNode rootNode = objectMapper.readTree(jsonFile);
	
	            // Get the "Emissions" array from the JSON
	            JsonNode emissionsNode = rootNode.get("Emissions");
	
	            if (emissionsNode == null || !emissionsNode.isArray()) {
	                System.out.println("Invalid JSON structure: 'Emissions' array not found.");
	                return;
	            }
	
	            // List to hold valid emissions
	            List<Emission> emissions = new ArrayList<>();
	
	            // Looping through array
	            for (JsonNode emissionNode : emissionsNode) {
	                String category = emissionNode.get("Category").asText();
	                String gasUnits = emissionNode.get("Gas Units").asText();
	                double value = emissionNode.get("Value").asDouble(0);
	
	                // Validate and add emission data
	                if (value > 0) {
	                    Emission emission = new Emission();
	                    emission.setCategory(category);
	                    emission.setDescription(gasUnits.trim());
	                    emission.setValue(value);
	                    emission.setScenario("WEM"); 
	                    emission.setYear(2023);
	
	                    emissions.add(emission);
	                }
	            }
	
	            // Save to the database
	            saveEmissionsToDatabase(emissions);
	
	        } catch (IOException e) {
	            System.err.println("Error parsing JSON file: " + e.getMessage());
	        }
	    }
	
	    /**
	     * Saves the list of emissions to the database using Hibernate.
	     *
	     * @param emissions List of valid Emission objects.
	     */
	    private static void saveEmissionsToDatabase(List<Emission> emissions) {
	        EntityManagerFactory emf = Persistence.createEntityManagerFactory("emission_persistence_unit");
	        EntityManager em = emf.createEntityManager();
	
	        em.getTransaction().begin();
	        try {
	            for (Emission emission : emissions) {
	                em.persist(emission);
	            }
	            em.getTransaction().commit();
	            System.out.println("Successfully saved " + emissions.size() + " emissions to the database.");
	        } catch (Exception e) {
	            em.getTransaction().rollback();
	            System.err.println("Error saving emissions to database: " + e.getMessage());
	        } finally {
	            em.close();
	            emf.close();
	        }
	    }
	}
