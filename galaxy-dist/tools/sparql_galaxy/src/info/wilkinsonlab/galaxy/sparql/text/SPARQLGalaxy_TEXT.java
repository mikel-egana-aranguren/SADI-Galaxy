package info.wilkinsonlab.galaxy.sparql.text;

import java.io.InputStream;
import java.util.Iterator;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.FileManager;

public class SPARQLGalaxy_TEXT {

	/**
	 * @param input
	 *            RDF
	 * @param SPARQL
	 *            query
	 * 
	 */
	public static void main(String[] args) {
		String input_RDF_path = args[0];
		String queryString = args[1];
		Model model = ModelFactory.createOntologyModel();
		InputStream in = FileManager.get().open(input_RDF_path);
		model.read(in, null);
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		String res = "";
		while (results.hasNext()) {
			QuerySolution qs = results.next();
			Iterator<String> vars = qs.varNames();
			while (vars.hasNext()) {
				String var = vars.next();
				res += "?" + var + "\t" + getValue(qs, var) + "\r\n";
				System.out.println(res);
			}
		}
	}

	private static String getValue(QuerySolution qs, String var) {
		RDFNode n = qs.get(var);
		return n.toString();
	}
}
