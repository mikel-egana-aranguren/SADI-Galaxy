package info.wilkinsonlab.sadi.galaxy;

import java.io.InputStream;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public class RDFSyntaxConverter {

	/**
	 * @param input RDF           
	 * @param format one of N3, N-TRIPLE, TAB
	 *            
	 */
	public static void main(String[] args) {
		String input_RDF_path = args[0];
		String format = args[1];
		Model model = ModelFactory.createOntologyModel();
		InputStream in = FileManager.get().open(input_RDF_path);
		model.read(in, null);

		if (format.equals("TAB")) {
			String queryString = "SELECT * WHERE { ?s ?p ?o } ";
			Query query = QueryFactory.create(queryString);
			QueryExecution qe = QueryExecutionFactory.create(query, model);
			try {
				ResultSet rs = qe.execSelect();
				System.out.println("Subject\tPredicate\tObject\n");
				while (rs.hasNext()) {
					QuerySolution row = rs.nextSolution();
					System.out.println(row.get("s") + "\t" + row.get("p")
							+ "\t" + row.get("o") + "\n");
				}
			} finally {
				qe.close();
			}
		} else {
			model.write(System.out, format);
		}
	}
}
