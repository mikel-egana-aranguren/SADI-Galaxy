package info.wilkinsonlab.sadi.galaxy;

import java.io.InputStream;
import java.util.Collection;

import org.apache.log4j.Logger;

import org.sadiframework.SADIException;
import org.sadiframework.client.Service;
import org.sadiframework.client.ServiceFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;

public class SADIGenericClient {

	/**
	 * @param service_URL
	 * @param input_RDF
	 * 
	 * Given an RDF input and a service URL, infers whether the RDF 
	 * complies with the service and if so executes it
	 * 
	 */
	
//	private static final Logger log = Logger.getLogger(SADIGenericClient.class);
	public static void main(String[] args)  {
		String service_URL = args[0];
		String input_RDF = args[1];

		Service service;
		try {			
			service = ServiceFactory.createService(service_URL.trim());
		} 
		catch (SADIException e) {
			System.out.println("[ERROR] Error creating service object: ");
			System.out.println(e.getMessage());
			e.printStackTrace();
			service = null;
		}
		
		if (service != null) {
			
 	 		Model model = ModelFactory.createOntologyModel();
 	 		InputStream in = FileManager.get().open(input_RDF);
 	 		model.read(in, null);
 			
			try {
				Collection<Resource> inputs = service.discoverInputInstances(model);
				Model output = service.invokeService(inputs);
				output.write(System.out, "RDF/XML");
		 		output.close();
			} 
			catch (SADIException e) {
				System.out.println("[ERROR] Error invoking service: ");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
 		}
	}
}
