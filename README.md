SADI-Galaxy
===========

About
-----

SADI-Galaxy is able to query any [SADI](http://sadiframework.org/) registry and install, in a local [Galaxy](http://galaxyproject.org/) server, a wrapper for each of the retrieved SADI services. With SADI-Galaxy, the Galaxy web interface can be used to execute SADI services on their own or in combination with other Galaxy tools, to generate workflows. 

[![doi:10.5281/zenodo.10181](https://zenodo.org/badge/doi/10.5281/zenodo.10181.png)](http://dx.doi.org/10.5281/zenodo.10181)

Requirements
------------

The main requirement is a working Galaxy server. SADI-Galaxy is meant to work in a UNIX system with Bash and sed; it has been tested in Ubuntu and CentOS, with Java 1.7. Merge RDF Graphs depends on Python (Tested with 2.7.3.) and [RDFLib](https://github.com/RDFLib/rdflib) (Tested with 4.1.1).

Deployment in a Galaxy server
-----------------------------

The main function of SADI-Galaxy is to query a SADI registry and create, for each service, a Galaxy tool (a tool XML file and an executable, and an entry in the `tool_conf.xml` file). Those files will be copied to the appropriate places in your Galaxy server (the `galaxy-dist` directory resembles an actual Galaxy server setting so it is self-explanatory); additionally, SADI-Galaxy can be configured to automatically (try to) edit the `tool_conf.xml` file of your Galaxy server.

The script `SADI-Galaxy.sh` is the principal tool; you should stop Galaxy, run it, and start Galaxy again for the SADI services to appear as part of the Galaxy interface. `SADI-Galaxy.sh` can be used to generate the following Galaxy tools:

* SADI Galaxy Generic. This tool accepts any SADI service URI and an RDF input, and executes the SADI service with the RDF input, producing output RDF.
* RDF Syntax Converter. This tool converts an RDF/XML file to N3, N-Triple, or, (more importantly for Galaxy) a tab delimited, three column file (Subject-Predicate-Object).
* Merge RDF Graphs. This tool can be used to merge the RDF outputs of different SADI services in a single graph.
* Optionally, a tool for each SADI service. This results in the same interface as the generic tool, without having to provide the service URI, thus, only the RDF input is needed. The URIs of these services are retrieved from a SADI registry using a SPARQL query, and if a service is not up and running, a Galaxy tool will not be generated for that service (this error will be shown in the log).

Therefore `SADI-Galaxy.sh` can be executed to generate the Generic SADI caller, RDF Syntax Converter, Merge RDF Graphs, and optionally a tool for each of the services retrieved from the registry. In order to configure `SADI-Galaxy.sh`, simply edit the configuration section within the script itself:

* `GALAXY` (Required): Absolute path to your Galaxy installation (For SADI-Galaxy to copy the necessary files into your Galaxy server): e.g. `/home/mikel/galaxy-dist/`
* `EDIT` (Required) Attempt to edit Galaxy tool_conf.xml (`edit`) or not (`no_edit`). If `no_edit` is selected (recommended), copy the lines from the newly generated `galaxy-dist/tool_conf.xml` to the actual `tool_conf.xml` of your galaxy server (e.g. `/home/mikel/galaxy-dist/tool_conf.xml`). 
* `ENDPOINT` (Optional) SADI registry endpoint: e.g. `http://dev.biordf.net/sparql/`
* `QUERY_DIR` (Optional) Path (Relative to this script) to the folder containing the SPARQL queries to execute (use this to tune which SADI services are retrieved; all the queries present in the directory will be executed): e.g. `sparql/queries/`
  
So a typical execution to generate the basic system (SADI Galaxy Generic, RDF Syntax Converter, and Merge RDF Graphs but no services from registry) without editing `tool_conf.xml` would look like:

* `GALAXY="/home/mikel/galaxy-dist/"` 
* `EDIT="no_edit"`

This will copy the necessary files for the tools SADI Generic, RDF Syntax Converter and Merge RDF Gaphs to appear as part of Galaxy when restarted. If you want to generate the SADI services defined in the queries at the folder `sparql/queries/`, querying the registry at `http://dev.biordf.net/sparql/`, without editing:

* `GALAXY="/home/mikel/galaxy-dist/"` 
* `EDIT="no_edit"`
* `ENDPOINT="http://dev.biordf.net/sparql/"`
* `QUERY_DIR="sparql/queries/"`

This will generate the necessary files for each SADI service to appear as a Galaxy tool when Galaxy is restarted (As well as SADI Galaxy generic, RDF Syntax Converter, and Merge RDF Graphs).

In order to define different SPARQL queries to retrieve a different set of SADI services, the new queries can be simply added to the queries folder (In this case `sparql/queries/`). The new SPARQL queries must have a variable called `?s` representing SADI services, and the following basic structure:

```
PREFIX  sadi: <http://sadiframework.org/ontologies/sadi.owl#>
PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX  serv: <http://www.mygrid.org.uk/mygrid-moby-service#>

SELECT ?s
WHERE { 
	  ?s rdf:type serv:serviceDescription .
	  ?s rdf:type sadi:Service 
  }
```

You can build new queries, e.g. if you want to retrieve the services provided by the Wilkinson lab:

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

The script `GENERATE_SPARQL_queries.sh` can be used to generate new queries using pre-caned queries so that you only need to add values and you can create your own base queries, as long as you add the `%PATTERN` keyword to your query. For example, if you want to generate many queries for many publishers, you can define the base query like this: 

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

This will generate a query for each publisher (wilkinsonlab.info, illuminae.com, dev.biordf.net, Cyber-ShARE, sadiframework.org). 

Deployment as a Docker image
----------------------------

A [Docker](https://www.docker.com/) image is available with a pre-configured SADI-Galaxy server, including a use case. Please refer to [SADI-Galaxy-Docker](http://github.com/mikel-egana-aranguren/SADI-Galaxy-Docker).

Use cases
---------

There is a public [Galaxy page](http://biordf.org:8983/u/mikel-egana-aranguren/p/sadi-galaxy-jbms-use-cases) with example use cases (the use cases can also be found in this repository at `/use_cases`).
 
Publications mentioning SADI-Galaxy
-----------------------------------

Alejandro Rodríguez González, Alison Callahan, José Cruz-Toledo, Adrian Garcia, Mikel Egaña Aranguren, Michel Dumontier, Mark D. Wilkinson. Automatically exposing OpenLifeData via SADI semantic Web Services. Journal of Biomedical Semantics 2014, 5(1):46+. [doi:10.1186/2041-1480-5-46](http://dx.doi.org/doi:10.1186/2041-1480-5-46)

Mikel Egaña Aranguren, Alejandro Rodríguez González, Mark D. Wilkinson. Executing SADI services in Galaxy. Journal of Biomedical Semantics 2014, 5(1):42+. [doi:10.1186/2041-1480-5-42](http://dx.doi.org/doi:10.1186/2041-1480-5-42)

Aranguren, M. E. (2015). Merging OpenLifeData with SADI services using Galaxy and Docker (DOI 10.1101/013615). BioRxiv, Cold Spring Harbor Labs [doi:10.1101/013615](http://dx.doi.org/10.1101/013615)

Funding
-------

Mikel Egaña Aranguren is funded by the Marie Curie Cofund programme (FP7) of the European Union through the UPM (Spain), and by the Genomic Resources group of the UPV-EHU (Spain).
