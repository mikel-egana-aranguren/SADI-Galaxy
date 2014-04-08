package info.wilkinsonlab.sadi.galaxy.GalaxyToolGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.sadiframework.client.Service;

public class ToolXMLFileCreator {

	public ToolXMLFileCreator(Service service, String tool_dir_path)
			throws IOException {
		init(service, tool_dir_path);
	}

	private void init(Service service, String tool_dir_path) throws IOException {

		// Printing XML directly is wrong but I couldn't care less, since it's only a few lines
		
		File file = new File(tool_dir_path + service.getName() + ".xml");
		BufferedWriter output = new BufferedWriter(new FileWriter(file));
		output.write("<tool id=\"" + service.getName() + "\" name=\""
				+ service.getName() + "\">\n");
		output.write("    <description>" + service.getDescription()
				+ "</description>\n");
		output.write("    <command>${__tool_data_path__}/shared/errwrap.sh java -Xmx2000M -Xms250M -jar ${__tool_data_path__}/shared/jars/sadi_generic_client.jar "
				+ service.getURI() + " $input > $output </command>\n");
		output.write("    <inputs>\n");
		output.write("    	  <param name=\"input\" type=\"data\" format=\"rdf\" label=\"RDF input for SADI service\"/>\n");
		output.write("    </inputs>\n");
		output.write("    <outputs>\n");
		output.write("        <data format=\"rdf\" name=\"output\"/>\n");
		output.write("    </outputs>\n");
		output.write("    <help>\n\n");
		output.write("    **What it does**\n\n");
		output.write("        Service URI: " + service.getURI() + "\n\n");					
		output.write("        Service name: " + service.getName() + "\n\n");
		output.write("        Service description: " + service.getDescription() + "\n\n");
		output.write("        Input OWL Class: " + service.getInputClassURI() + "\n\n");
		output.write("        Output OWL Class: " +service.getOutputClassURI() + "\n\n");
		output.write("    **About**\n\n");
		output.write("        Service provider: " + service.getServiceProvider() + "\n\n");
		output.write("        Contact email: " + service.getContactEmail() + "\n\n");
		output.write("    </help>\n");
		output.write("</tool>\n");
		output.close();
	}
}
