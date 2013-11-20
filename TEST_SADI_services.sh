#!/bin/sh

# To test a new SADI service before deploying it, you need the service URI and input/output RDF, and then you can test it like this:

# java -jar galaxy-dist/tool-data/shared/jars/sadi_generic_client.jar http://sadiframework.org/examples/hello tests/hello-input.rdf > tests/test_hello_output.rdf

# tests/test_hello_output.rdf should be similar to tests/hello-output.rdf 


java -jar galaxy-dist/tool-data/shared/jars/sadi_generic_client.jar http://biordf.org:8080/cgi-bin/SADI/biomodels/Pathway_dataSource_Provenance tests/Pathway_dataSource_input.rdf > tests/Pathway_dataSource_output.rdf