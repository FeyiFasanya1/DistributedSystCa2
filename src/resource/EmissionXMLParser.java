	package resource;
	
	import javax.xml.parsers.DocumentBuilderFactory;
	import javax.xml.parsers.DocumentBuilder;
	import org.w3c.dom.Document;
	import org.w3c.dom.NodeList;
	import org.w3c.dom.Node;
	import org.w3c.dom.Element;
	import java.io.File;
	import java.io.IOException;
	import java.util.ArrayList;
	import java.util.List;

	import javax.xml.parsers.ParserConfigurationException;
	import org.xml.sax.SAXException;
	import entity.Emission;
	
	public class EmissionXMLParser {
		
		public static void main(String argv[]) throws
	    IOException, ParserConfigurationException, SAXException {
			
	    List<Emission> emissions = new ArrayList<>();
	
	
		File xmlFile = new File("GreenhouseGasEmissions.xml");
	
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder = factory.newDocumentBuilder();
	Document doc = builder.parse(xmlFile);  
	
	
	 NodeList rowNodes = doc.getElementsByTagName("Row");

     for (int i = 0; i < rowNodes.getLength(); i++) {
         Node node = rowNodes.item(i);

         if (node.getNodeType() == Node.ELEMENT_NODE) {
             Element element = (Element) node;

             String category = element.getElementsByTagName("Category__1_3").item(0).getTextContent();
             String scenario = element.getElementsByTagName("Scenario").item(0).getTextContent();
             int year = Integer.parseInt(element.getElementsByTagName("Year").item(0).getTextContent());
             String gasUnits = element.getElementsByTagName("Gas___Units").item(0).getTextContent();
             String valueStr = element.getElementsByTagName("Value").item(0).getTextContent();

             // Ensuring Value is present and valid
             if (!valueStr.isEmpty()) {
                 double value = Double.parseDouble(valueStr);

                 // parsing rules
                 if ("WEM".equalsIgnoreCase(scenario) && year == 2023 && value > 0) {
                     Emission emission = new Emission();
                     emission.setCategory(category);
                     emission.setScenario(scenario);
                     emission.setYear(year);
                     emission.setValue(value);
                     emission.setDescription(gasUnits.trim());

                     emissions.add(emission);
                 }
             }
         }
     }

     for (Emission emission : emissions) {
         System.out.println(emission.getCategory() + " - " + emission.getValue() + " " + emission.getDescription()); }

		
	
	}
		
	}
		
	
