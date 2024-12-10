	package resource;
	
	import entity.Emission;
	import com.fasterxml.jackson.databind.JsonNode;
	import com.fasterxml.jackson.databind.ObjectMapper;
	import javax.ws.rs.*;
	import javax.ws.rs.core.*;
	import javax.persistence.*;
	import java.io.File;
	import java.io.IOException;
	import java.util.ArrayList;
	import java.util.List;
	
	@Path("/emissions")
	public class EmissionResource {
	
	    @PersistenceContext
	    private EntityManager em;
	
	    // Endpoint to upload the XML data
	    @POST
	    @Path("/upload/xml")
	    @Consumes(MediaType.APPLICATION_XML)
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response uploadEmissionXML(File xmlFile) {
	        try {
	            List<Emission> emissions = parseXML(xmlFile);
	            saveToDatabase(emissions);
	            return Response.status(Response.Status.CREATED)
	                    .entity("Emissions uploaded and saved to database successfully")
	                    .build();
	        } catch (Exception e) {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                    .entity("Error processing XML file: " + e.getMessage())
	                    .build();
	        }
	    }
	
	    // Endpoint to upload the JSON data
	    @POST
	    @Path("/upload/json")
	    @Consumes(MediaType.APPLICATION_JSON)
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response uploadEmissionJSON(File jsonFile) {
	        try {
	            List<Emission> emissions = parseJSON(jsonFile);
	            saveToDatabase(emissions);
	            return Response.status(Response.Status.CREATED)
	                    .entity("Emissions uploaded and saved to database successfully")
	                    .build();
	        } catch (IOException e) {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                    .entity("Error processing JSON file: " + e.getMessage())
	                    .build();
	        }
	    }
	
	    @POST
	    @Consumes(MediaType.APPLICATION_JSON)
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response createEmission(Emission emission) {
	        try {
	            em.getTransaction().begin();
	            em.persist(emission);
	            em.getTransaction().commit();
	            return Response.status(Response.Status.CREATED).entity(emission).build();
	        } catch (Exception e) {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                    .entity("Error saving emission: " + e.getMessage())
	                    .build();
	        }
	    }
	
	    @GET
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response getAllEmissions() {
	        try {
	            List<Emission> emissions = em.createQuery("SELECT e FROM Emission e", Emission.class).getResultList();
	            return Response.ok(emissions).build();
	        } catch (Exception e) {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                    .entity("Error retrieving emissions: " + e.getMessage())
	                    .build();
	        }
	    }
	
	    @GET
	    @Path("/{id}")
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response getEmissionById(@PathParam("id") int id) {
	        Emission emission = em.find(Emission.class, id);
	        if (emission == null) {
	            return Response.status(Response.Status.NOT_FOUND).entity("Emission not found for ID: " + id).build();
	        }
	        return Response.ok(emission).build();
	    }
	
	    @PUT
	    @Path("/{id}")
	    @Consumes(MediaType.APPLICATION_JSON)
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response updateEmission(@PathParam("id") int id, Emission updatedEmission) {
	        Emission existingEmission = em.find(Emission.class, id);
	        if (existingEmission == null) {
	            return Response.status(Response.Status.NOT_FOUND).entity("Emission not found for ID: " + id).build();
	        }
	        try {
	            em.getTransaction().begin();
	            existingEmission.setCategory(updatedEmission.getCategory());
	            existingEmission.setDescription(updatedEmission.getDescription());
	            existingEmission.setValue(updatedEmission.getValue());
	            existingEmission.setYear(updatedEmission.getYear());
	            existingEmission.setScenario(updatedEmission.getScenario());
	            em.getTransaction().commit();
	            return Response.ok(existingEmission).build();
	        } catch (Exception e) {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                    .entity("Error updating emission: " + e.getMessage())
	                    .build();
	        }
	    }
	
	    @DELETE
	    @Path("/{id}")
	    @Produces(MediaType.TEXT_PLAIN)
	    public Response deleteEmission(@PathParam("id") int id) {
	        Emission emission = em.find(Emission.class, id);
	        if (emission == null) {
	            return Response.status(Response.Status.NOT_FOUND).entity("Emission not found for ID: " + id).build();
	        }
	        try {
	            em.getTransaction().begin();
	            em.remove(emission);
	            em.getTransaction().commit();
	            return Response.ok("Emission deleted successfully").build();
	        } catch (Exception e) {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                    .entity("Error deleting emission: " + e.getMessage())
	                    .build();
	        }
	    }
	
	    // Helper to parse the XML file
	    private List<Emission> parseXML(File xmlFile) throws Exception {
	        List<Emission> emissions = new ArrayList<>();
	        // Add your XML parsing logic here as per your XML format.
	        return emissions;
	    }
	
	    // Helper to parse the JSON file
	    private List<Emission> parseJSON(File jsonFile) throws IOException {
	        List<Emission> emissions = new ArrayList<>();
	        ObjectMapper objectMapper = new ObjectMapper();
	        JsonNode rootNode = objectMapper.readTree(jsonFile);
	        JsonNode emissionsNode = rootNode.get("Emissions");
	
	        if (emissionsNode != null && emissionsNode.isArray()) {
	            for (JsonNode emissionNode : emissionsNode) {
	                String category = emissionNode.get("Category").asText();
	                String gasUnits = emissionNode.get("Gas Units").asText();
	                double value = emissionNode.get("Value").asDouble();
	
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
	        }
	        return emissions;
	    }
	
	    // Helper to save to the database
	    private void saveToDatabase(List<Emission> emissions) {
	        em.getTransaction().begin();
	        for (Emission emission : emissions) {
	            em.persist(emission);
	        }
	        em.getTransaction().commit();
	    }
	}
