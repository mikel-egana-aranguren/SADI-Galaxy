#!/bin/sh

java -jar galaxy-dist/tool-data/shared/jars/RDFSyntaxConverter.jar tests/hello-input.rdf TAB > tests/RDFSyntaxConverter_TAB_test_hello_output
java -jar galaxy-dist/tool-data/shared/jars/RDFSyntaxConverter.jar tests/hello-input.rdf N3 > tests/RDFSyntaxConverter_N3_test_hello_output
java -jar galaxy-dist/tool-data/shared/jars/RDFSyntaxConverter.jar tests/hello-input.rdf N-TRIPLE > tests/RDFSyntaxConverter_N-TRIPLE_test_hello_output