SADI-Galaxy
===========

About
-----

SADI-Galaxy is able to query any SADI registry and install, in a local Galaxy server, a wrapper for each of the retrieved SADI services. With SADI-Galaxy, the Galaxy web interface can be used to execute SADI services on their own or in combination with other Galaxy tools to generate workflows. 

More information:

* SADI: http://sadiframework.org/ 
* Galaxy: http://galaxyproject.org/
* A public Galaxy instance with SADI services pre-installed: http://biordf.org:8983/

Usage
-----

The main function of SADI-Galaxy is to query a SADI registry and create, for each service, a Galaxy tool (a tool XML file, an entry in the `tool_conf.xml` file, and the needed jar files). Those files will be copied to the appropriate places in your Galaxy server (the `galaxy-dist` directory resembles an actual Galaxy server setting so it is self-explanatory); additionally, the tool can be configured to automatically (try to) edit the `tool_conf.xml` file of your Galaxy server.

The script `SADI-Galaxy.sh` is the principal tool; you should stop Galaxy, run it, and start Galaxy again for the SADI services to appear as part of the Galaxy interface. It can be executed directly (See parameters list bellow) but a convenience script, `RUN-SADI-Galaxy.sh`, is included with example parameters. `SADI-Galaxy.sh` can be used to generate the following Galaxy tools:

* SADI Generic. This tool accepts any SADI service URI and an RDF input, and executes the SADI service with the RDF input, producing output RDF.
* RDF Syntax Converter. This tool converts an RDF/XML file to N3, N-Triple, or, (more importantly for Galaxy) a tab delimited, three column file (Subject-Predicate-Object).
* A tool for each SADI service. This is the same as the generic tool, without having to provide the service URI, thus, only RDF input is needed.

Therefore `SADI-Galaxy.sh` can be executed to generate, depending on the parameters used:

* Generic SADI caller and RDF Syntax Converter: 

** (Required) Absolut path to your Galaxy installation: `/home/mikel/galaxy-dist/`
** (Required) Attempt to edit Galaxy tool_conf.xml (`edit`) or not (`no_edit`). If `no_edit` is selected (recommended), copy the lines from the newly generated `galaxy-dist/tool_conf.xml` to the actual `tool_conf.xml` of your galaxy server (e.g. `/home/mikel/galaxy-dist/tool_conf.xml`). 

* Generic SADI caller, RDF Syntax Converter and a tool for each of the services retrieved from the registry:

** (Required) `/home/mikel/galaxy-dist/`
** (Required) `edit` or `no_edit` 
** (Optional) Path (Relative to this script) to SPARQL query (use this to tune the retrieved SADI services): `sparql/simple_query.sparql`
** (Optional) SADI registry endpoint: `http://dev.biordf.net/sparql/`

So a typical execution to generate the basic system (no services from registry) without editing `tool_conf.xml` would look like this:

`./SADI-Galaxy.sh /home/mikel/galaxy-dist/ no_edit`

If we want to generate the SADI services defined in file `sparql/simple_query.sparql`, querying the registry at `http://dev.biordf.net/sparql/`, without editing:

`./SADI-Galaxy.sh /home/mikel/galaxy-dist/ no_edit sparql/simple_query.sparql http://dev.biordf.net/sparql/`

In order to define new SPARQL queries, a new file can be created with a different SPARQL query. Any new SPARQL must have a variable called `?s` representing services, and (presumably) the following basic structure:

```
PREFIX  sadi: <http://sadiframework.org/ontologies/sadi.owl#>
PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX  serv: <http://www.mygrid.org.uk/mygrid-moby-service#>

SELECT ?s
WHERE { 
	  ?s rdf:type serv:serviceDescription .
	  ?s rdf:type  sadi:Service 
  }
```

You can build new queries, e.g. if we want to retrieve the services provided by the Wilkinson lab:

```
PREFIX  dc:   <http://protege.stanford.edu/plugins/owl/dc/protege-dc.owl#>
PREFIX  sadi: <http://sadiframework.org/ontologies/sadi.owl#>
PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX  serv: <http://www.mygrid.org.uk/mygrid-moby-service#>

SELECT ?s ?org
WHERE { 
	  ?s rdf:type serv:serviceDescription .
	  ?s rdf:type  sadi:Service .
          ?s  serv:providedBy ?org .
          ?org  dc:publisher "wilkinsonlab.info"
  }
```

`TEST_SADI_services.sh` can be used to test concrete SADI services.

Funding
-------

Mikel Egaña Aranguren is funded by the Marie Curie Cofund programme (FP7) of the European Union through the UPM (Spain).
