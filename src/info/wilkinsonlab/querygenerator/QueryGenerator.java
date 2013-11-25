package info.wilkinsonlab.querygenerator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class QueryGenerator {
	
	private final String PATTERN_STRING = "%PATTERN";
	private final String DESTINATION_QUERY_FOLDER = "sparql/queries";
	
	/**
	 * M�todo para crear la query sin par�metro de borrado.
	 * @param f Recibe el fichero.
	 * @param p Recibe el replace.
	 * @return Devuelve el fichero generado.
	 * @throws Exception Puede lanzar excepci�n.
	 */
	public String createQuery(String f, String p) throws Exception {
		return createQuery(f,p,false);
		
	}
	/**
	 * M�todo para crear la query.
	 * @param f Recibe el fichero query base.
	 * @param p Recibe el par�metro a sustituir.
	 * @param clean Recibe un boolean que dice si borrar queries anteriores.
	 * @return Devuelve el fichero generado.
	 * @throws Exception Puede lanzar excepci�n.
	 */
	public String createQuery(String f, String p, boolean clean) throws Exception {
		if (clean) {
			removeQueries();
		}
		String queryBase = loadQueryBase(f);
		String destQueryFile = createRandomQueryFile(f);
		String finalQuery = queryBase.replace(PATTERN_STRING,p);
		write(finalQuery,destQueryFile);
		System.out.println("Query generated!\n");
		System.out.println("Data:");
		System.out.println("\tOriginal query file: " + f);
		System.out.println("\tReplacement parameter: " + p);
		System.out.println("\tPerformed clean?: " + clean);
		System.out.println("\tResulting query file: " + destQueryFile);
		return destQueryFile;
	}

	/**
	 * M�todo para borrar las queries generadas anteriormente.
	 */
	private void removeQueries() {
		File dir = new File(DESTINATION_QUERY_FOLDER);
		File files[] = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}
	}

	/**
	 * M�todo para escribir la query en el fichero.
	 * @param finalQuery Recibe la query.
	 * @param destQueryFile Recibe el fichero.
	 * @throws Exception Puede lanzar excepci�n.
	 */
	private void write(String finalQuery, String destQueryFile) throws Exception {
		BufferedWriter bW = new BufferedWriter(new FileWriter(destQueryFile));
		bW.write(finalQuery);
		bW.newLine();
		bW.close();
	}

	/**
	 * M�todo para crear el nombre del fichero donde guardar la query.
	 * @param f Recibe el fichero de la query base.
	 * @param p Recibe el par�metro a sustituir.
	 * @return Devuelve el nombre del fichero.
	 */
	private String createRandomQueryFile(String f) {
		File fi = new File(f);
		/* De directorio/predicado.sparql cogemos "predicado". */
		String predicate = fi.getName().substring(0,fi.getName().lastIndexOf('.'));
		String retFile = DESTINATION_QUERY_FOLDER + "/" + "query_" + predicate + "_" + new java.util.Random().nextInt(999999) + new java.util.Random(System.currentTimeMillis()).nextInt(999999) + ".sparql";
		return retFile;
	}

	/**
	 * M�todo para cargar la query base.
	 * @param f Recibe el fichero donde est�.
	 * @return Devuelve la query.
	 * @throws Exception Puede lanzar excepci�n.
	 */
	private String loadQueryBase(String f) throws Exception {
		BufferedReader bL = new BufferedReader(new FileReader(f));
		String ret = "";
		while (bL.ready()) {
			ret += bL.readLine() + "\r\n";
		}
		bL.close();
		return ret;
	}
}
