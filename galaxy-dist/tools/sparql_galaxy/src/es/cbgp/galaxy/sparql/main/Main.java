package es.cbgp.galaxy.sparql.main;

import java.io.File;

import es.cbgp.galaxy.sparql.jena.OntologyManager;

public class Main {

	public Main(String args[]) {
		Result r = check(args);
		if (r.getBoolValue()) {
			String ontFile = args[0];
			String sparqlFile = args[1];
			run(ontFile, sparqlFile);
		} else {
			System.err.println("Error: " + r.getMessage());
		}
	}

	private void run(String ontFile, String sparqlFile) {
		try {
			OntologyManager om = new OntologyManager(ontFile, sparqlFile);
			om.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Result check(String[] args) {
		if (args.length == 2) {
			String fo = args[0];
			String fs = args[1];
			if (new File(fo).exists()) {
				if (new File(fs).exists()) {
					return new Result(true);
				} else {
					return new Result(false,
							"Ontology file exists. SPARQL file not!");
				}
			}
			return new Result(false, "Ontology file not exists!");
		}
		return new Result(false,
				"Incorrect number of parameters. Necessary 2: " + args.length);
	}

	public static void main(String[] args) {
		/*
		 * Input: ontologia "SELECT .... blala"
		 */
		new Main(args);
	}

}
