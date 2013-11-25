package info.wilkinsonlab.querygenerator;

public class Main {

	public static void main(String[] args){
		String base_query = args [0];
//		String base_query = "sparql/basequeries/publisher.sparql";
//		String base_query = "sparql/basequeries/predicate.sparql";
		for (int i = 1; i < args.length; i++) {
			try {
				QueryGenerator qg = new QueryGenerator();
				qg.createQuery(base_query, args [i]);
//				String fileQuery = qg.createQuery(Queries.PUBLISHER, "wilkinsonlab.info", true);
//				qg.createQuery(Queries.PREDICATE, "http://sadiframework.org/ontologies/properties.owl#hasName");
						
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
