#!/bin/sh

java -jar galaxy-dist/tool-data/shared/jars/sadi_generic_client.jar http://sadiframework.org/examples/hello	tests/hello-input.rdf > tests/test_hello_output.rdf

# tests/test_hello_output.rdf should be similar to tests/hello-output.rdf 