	package resource;
	
	import entity.Emission;
	import com.fasterxml.jackson.databind.JsonNode;
	import com.fasterxml.jackson.databind.ObjectMapper;
	import entity.Emission;
	import javax.ws.rs.*;
	import javax.ws.rs.core.*;
	import java.io.File;
	import java.io.IOException;
	import java.util.ArrayList;
	import java.util.List;
	import javax.persistence.*;
	
	@Path("/emissions")
	public class EmissionResource {
	
	    // Endpoint to upload the XML data
	    @POST
	    @Path("/emissions")
	    @Consumes(MediaType.APPLICATION_XML)
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response uploadEmissionXML(File xmlFile) {
	        try {
	            List<Emission> emissions = parseXML(xmlFile);
	            EmissionData.saveToDatabase(emissions);
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
	    @Path("/emissions")
	    @Consumes(MediaType.APPLICATION_JSON)
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response uploadEmissionJSON(File jsonFile) {
	        try {
	            List<Emission> emissions = parseJSON(jsonFile);
	            EmissionData.saveToDatabase(emissions);
	            return Response.status(Response.Status.CREATED)
	                           .entity("Emissions uploaded and saved to database successfully")
	                           .build();
	        } catch (IOException e) {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                           .entity("Error processing JSON file: " + e.getMessage())
	                           .build();
	        }
	    }
	
	    // Helper method to parse the XML file
	    private List<Emission> parseXML(File xmlFile) throws Exception {
	        List<Emission> emissions = new ArrayList<>();
	        // Add your XML parsing logic here as per your XML format.
	        return emissions;
	    }
	
	    // Helper method to parse the JSON file
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
	}
