SADI-Galaxy
===========

About
-----

SADI-Galaxy is able to query any SADI registry and install, in a local Galaxy server, a wrapper for each of the retrieved SADI services. With SADI-Galaxy, the Galaxy web interface can be used to execute SADI services on their own or in combination with other Galaxy tools to generate workflows. 

More information:

* SADI: http://sadiframework.org/ 
* Galaxy: http://galaxyproject.org/
* A public Galaxy instance with SADI services pre-installed: http://biordf.org:8983/
* A galaxy page containing use cases: http://biordf.org:8983/u/mikel-egana-aranguren/p/sadi-galaxy-jbms-use-cases

Usage
-----

The main function of SADI-Galaxy is to query a SADI registry and create, for each service, a Galaxy tool (a tool XML file, an entry in the `tool_conf.xml` file, and the needed jar files). Those files will be copied to the appropriate places in your Galaxy server (the `galaxy-dist` directory resembles an actual Galaxy server setting so it is self-explanatory); additionally, SADI-Galaxy can be configured to automatically (try to) edit the `tool_conf.xml` file of your Galaxy server.

The script `SADI-Galaxy.sh` is the principal tool; you should stop Galaxy, run it, and start Galaxy again for the SADI services to appear as part of the Galaxy interface (The results of the process can be checked at the `log` file). It can be executed directly (See parameters list bellow) but a convenience script, `RUN-SADI-Galaxy.sh`, is included with example parameters. `SADI-Galaxy.sh` can be used to generate the following Galaxy tools:

* SADI Generic. This tool accepts any SADI service URI and an RDF input, and executes the SADI service with the RDF input, producing output RDF.
* RDF Syntax Converter. This tool converts an RDF/XML file to N3, N-Triple, or, (more importantly for Galaxy) a tab delimited, three column file (Subject-Predicate-Object).
* A tool for each SADI service. This is the same as the generic tool, without having to provide the service URI, thus, only RDF input is needed. The URIs of these services are retrieved from a SADI registry using a SPARQL query, and each service status is tested: if it is not up and running, a Galaxy tool will not be generated for that service (this error will be shown in the log).

Therefore `SADI-Galaxy.sh` can be executed to generate, depending on the parameters used:

* Generic SADI caller and RDF Syntax Converter: 

  * (Required) Absolute path to your Galaxy installation (For SADI-Galaxy to copy the necessary files into your Galaxy server): `/home/mikel/galaxy-dist/`
  * (Required) Attempt to edit Galaxy tool_conf.xml (`edit`) or not (`no_edit`). If `no_edit` is selected (recommended), copy the lines from the newly generated `galaxy-dist/tool_conf.xml` to the actual `tool_conf.xml` of your galaxy server (e.g. `/home/mikel/galaxy-dist/tool_conf.xml`). 

* Generic SADI caller, RDF Syntax Converter and a tool for each of the services retrieved from the registry:

  * (Required) `/home/mikel/galaxy-dist/`
  * (Required) `edit` or `no_edit` 
  * (Optional) Path (Relative to this script) to the folder containing the SPARQL queries to execute (use this to tune which SADI services are retrieved): `sparql/queries/`
  * (Optional) SADI registry endpoint: `http://dev.biordf.net/sparql/`

So a typical execution to generate the basic system (no services from registry) without editing `tool_conf.xml` would look like this:

`./SADI-Galaxy.sh /home/mikel/galaxy-dist/ no_edit`

This will copy the necessary files for the tools SADI Generic and RDF Syntax Converter to appear as part of Galaxy when restarted. If we want to generate the SADI services defined in the queries at the folder `sparql/queries/`, querying the registry at `http://dev.biordf.net/sparql/`, without editing:

`./SADI-Galaxy.sh /home/mikel/galaxy-dist/ no_edit sparql/queries/ http://dev.biordf.net/sparql/`

This will generate the necessary files for each SADI service to appear as a Galaxy tool and appear in Galaxy when started (As well as SADI generic and RDF Syntax Converter).

In order to define different SPARQL queries to retrieve a different set of SADI services, they can be simply added to the queries folder (In this case `sparql/queries/`). The new SPARQL queries must have a variable called `?s` representing SADI services, and (presumably) the following basic structure:

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

SELECT ?s
WHERE { 
	  ?s rdf:type serv:serviceDescription .
	  ?s rdf:type  sadi:Service .
	  ?s  serv:providedBy ?org .
	  ?org  dc:publisher "wilkinsonlab.info"
  }
```

The script `GENERATE_SPARQL_queries.sh` can be used to generate new queries using pre-caned queries so that you only need to add values and you can create your own base queries, as long as you add the `%PATTERN` keyword to your query. For example, if we want to generate many queries for many publishers, we can define the base query like this: 

```
PREFIX  dc:   <http://protege.stanford.edu/plugins/owl/dc/protege-dc.owl#>
PREFIX  sadi: <http://sadiframework.org/ontologies/sadi.owl#>
PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX  serv: <http://www.mygrid.org.uk/mygrid-moby-service#>

SELECT ?s
WHERE {
      ?s rdf:type serv:serviceDescription .
      ?s rdf:type  sadi:Service .
      ?s  serv:providedBy ?org .
      ?org  dc:publisher "%PATTERN"
  }

```

And then execute the query generator like this:

`java -jar generate_sparql_queries.jar sparql/basequeries/publisher.sparql wilkinsonlab.info illuminae.com dev.biordf.net Cyber-ShARE sadiframework.org`

This will generate a query for each publisher. Finally, `TEST_SADI_services.sh` can be used to test concrete SADI services. Therefore, in summary, the following tools are available:

* `SADI-Galaxy.sh`: main tool, for generating SADI services.
* `RUN-SADI-Galaxy.sh`: convenience script to run `SADI-Galaxy.sh`.
* `GENERATE_SPARQL_queries.sh`: convenience tool for generating queries that can be consumed by `SADI-Galaxy.sh`.      
* `TEST_RDFSyntaxConverter.sh`: test the RDF Syntax Converter. 
* `TEST_SADI_services.sh`: test a SADI service by providing its URI and input RDF.

Funding
-------

Mikel Egaña Aranguren is funded by the Marie Curie Cofund programme (FP7) of the European Union through the UPM (Spain).
