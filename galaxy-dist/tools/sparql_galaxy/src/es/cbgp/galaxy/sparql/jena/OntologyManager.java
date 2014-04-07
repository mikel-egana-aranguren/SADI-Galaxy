package es.cbgp.galaxy.sparql.jena;

import java.io.File;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class OntologyManager {

	private String ontFile;
	private String sparqlFile;
	private Model model;
	private SPARQLQueryEngine sqe;

	public OntologyManager(String of, String sf) throws Exception {
		this.ontFile = of;
		this.sparqlFile = sf;
		init();
	}

	private void init() throws Exception {
		this.model = ModelFactory.createDefaultModel();
		this.model.read(new File(ontFile).toURI().toString());
	}

	public void executeQuery() throws Exception {
		this.sqe = new SPARQLQueryEngine(model);
		this.sqe.setQueryFile(sparqlFile);
		String ret = sqe.executeQuery();
		System.out.println(ret);
	}
}
