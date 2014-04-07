package es.cbgp.galaxy.sparql.jena;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Iterator;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.shared.Lock;

public class SPARQLQueryEngine {

	private Model queryModel;
	private String sparqlFile;

	public SPARQLQueryEngine(Model om) {
		this.queryModel = om;
	}

	public String executeQuery() throws Exception {
		String finalQuery = loadQueryFromFile();
		// System.out.println("Final query: " + finalQuery);
		Query query = null;
		QueryExecution qexec = null;
		try {
			queryModel.enterCriticalSection(Lock.READ);
			query = QueryFactory.create(finalQuery);
			qexec = QueryExecutionFactory.create(query, queryModel);
			ResultSet results = qexec.execSelect();
			String res = "";
			while (results.hasNext()) {
				QuerySolution qs = results.next();
				Iterator<String> vars = qs.varNames();
				while (vars.hasNext()) {
					String var = vars.next();
					res += "?" + var + "\t" + getValue(qs, var) + "\r\n";

				}
			}
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			queryModel.leaveCriticalSection();
			if (qexec != null) {
				qexec.close();
			}
		}
		return null;
	}

	private String getValue(QuerySolution qs, String var) {
		RDFNode n = qs.get(var);
		return n.toString();
		
//		System.out.println("RDFNode (" + var + "): " + n);
//		try {
//			if (!qs.getResource(var).isAnon()) {
//				if (qs.getResource(var).isResource()) {
//					return qs.getResource(var).toString();
//				}
//				if (qs.getResource(var).isLiteral()) {
//					return qs.getLiteral(var).toString();
//				}
//			}
//		} catch (Exception e) {
//
//		}
//		try {
//			if (!qs.getLiteral(var).isAnon()) {
//				if (qs.getResource(var).isResource()) {
//					return qs.getResource(var).toString();
//				}
//				if (qs.getResource(var).isLiteral()) {
//					return qs.getLiteral(var).toString();
//				}
//			}
//		} catch (Exception e) {
//
//		}
//		return "Error";
	}

	private String loadQueryFromFile() throws Exception {
		String query = "";
		BufferedReader bL = new BufferedReader(new FileReader(this.sparqlFile));
		while (bL.ready()) {
			query += bL.readLine();
		}
		bL.close();
		return query;
	}

	public void setQueryFile(String sparqlFile) {
		this.sparqlFile = sparqlFile;
	}
}
