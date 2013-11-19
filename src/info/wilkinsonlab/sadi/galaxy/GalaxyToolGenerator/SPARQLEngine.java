package info.wilkinsonlab.sadi.galaxy.GalaxyToolGenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class SPARQLEngine {
	private String endpoint;
	private String query_file_path;
	public SPARQLEngine (String endpoint, String query_file_path){
		this.endpoint = endpoint;
		this.query_file_path = query_file_path;
	}
		
	public ArrayList<RDFNode> exec_query (String var_name) throws Exception{
		ArrayList<RDFNode> nodes = new ArrayList<RDFNode>();
		String finalQuery = loadQueryFromFile();
		Query query = QueryFactory.create(finalQuery);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(this.endpoint, query);
		ResultSet results = qexec.execSelect();
		while (results.hasNext()) {
			QuerySolution qs = results.next();
			nodes.add(qs.get(var_name));
		}
		if (qexec != null) {
			qexec.close();
		}
		return nodes;
	}
	

	
	private String loadQueryFromFile() throws Exception {
		String query = "";
		BufferedReader bL = new BufferedReader(new FileReader(this.query_file_path));
		while (bL.ready()) {
			String read = bL.readLine() + "\n";
			query += read;
		}
		bL.close();
		return query;
	} 
}
