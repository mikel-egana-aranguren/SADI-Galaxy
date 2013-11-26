package info.wilkinsonlab.sadi.galaxy.GalaxyToolGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.sadiframework.client.Service;
import org.sadiframework.client.ServiceFactory;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class SADIGalaxyToolGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String query_file_path = args[0];
		String serviceEndpoint = args[1];
		String tool_conf = args[2];
		String tool_dir = args[3];

		SPARQLEngine engine = new SPARQLEngine(serviceEndpoint, query_file_path);

		int proper_services = 0;
		ArrayList<RDFNode> results = null;
		try {
			results = engine.exec_query("?s");

			FileWriter tool_conf_file = null;

			tool_conf_file = new FileWriter(tool_conf, true);
//			tool_conf_file
//					.write("\t<section name=\"SADI services\" id=\"SADI\">\n");
//			tool_conf_file
//					.write("\t\t<label text=\"SADI common utilities\" id=\"SADI-common-utilities\"/>\n");
//			tool_conf_file
//					.write("\t\t\t<tool file=\"SADI/sadi_generic.xml\"/>\n");
//			tool_conf_file.write("\t\t\t<tool file=\"SADI/RDF2TAB.xml\"/>\n");
//			tool_conf_file
//					.write("\t\t\t<tool file=\"SADI/RDFSyntaxConverter.xml\"/>\n");
//			tool_conf_file
//					.write("\t\t<label text=\"SADI services\" id=\"SADI-services\"/>\n");

			Iterator results_iterator = results.iterator();
			while (results_iterator.hasNext()) {
				String service_URL = results_iterator.next().toString();
				System.out.println("INFO: Trying to create service " + service_URL);

				Service service = null;
				try {
					service = ServiceFactory.createService(service_URL);
				} catch (Exception e) {
					System.out.println("INFO: Unable to create service "
							+ service_URL);
					e.printStackTrace();
					System.out
							.println("INFO: No Galaxy tool will be generated for service "
									+ service_URL);
				}
				if (service != null) {
					System.out
							.println("INFO: Service generation succesful - Galaxy tool will be generated for "
									+ service_URL);
					new ToolXMLFileCreator(service, tool_dir);
					proper_services = proper_services + 1;
					tool_conf_file.write("\t\t\t<tool file=\"SADI/"
							+ service.getName() + ".xml\"/>\n");
				}
			}

//			tool_conf_file.write("\t</section>\n");
//			tool_conf_file.write("</toolbox>\n");
			tool_conf_file.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		System.out.println("INFO: " + results.size() + " SADI services retrieved");
		System.out.println("INFO: " + proper_services + " Galaxy tools created");
	}
}
