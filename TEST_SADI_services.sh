#!/bin/sh

# To test a new SADI service before deploying it, you need the service URI and input/output RDF, and then you can test it like this:


# java -jar galaxy-dist/tool-data/shared/jars/sadi_generic_client.jar http://sadiframework.org/examples/hello tests/hello-input.rdf > tests/test_hello_output.rdf

# java -Xmx2000M -Xms250M -jar galaxy-dist/tool-data/shared/jars/sadi_generic_client.jar http://sadiframework.org/services/HelloWorld tests/hello-input.rdf > tests/test_hello_output_2.rdf

# java -jar galaxy-dist/tool-data/shared/jars/sadi_generic_client.jar http://biordf.org:8080/cgi-bin/SADI/biomodels/Pathway_dataSource_Provenance tests/Pathway_dataSource_input.rdf > tests/Pathway_dataSource_output.rdf

# java -jar galaxy-dist/tool-data/shared/jars/sadi_generic_client.jar http://biordf.org:8080/cgi-bin/services/retrieveProteomeProtiens_sync.pl tests/retrieveProteomeProtiens_sync_inputr.rdf > tests/retrieveProteomeProtiens_sync_output.rdf

# java -jar galaxy-dist/tool-data/shared/jars/sadi_generic_client.jar http://biordf.org:8080/cgi-bin/services/Dragon/getAlleleDescription.pl tests/getAlleleDescription_input.rdf > tests/getAlleleDescription_output.rdf

# java -jar galaxy-dist/tool-data/shared/jars/sadi_generic_client.jar http://biordf.org:8080/cgi-bin/services/Dragon/getAlleleDescription.pl tests/getAlleleDescription_input_2.rdf > tests/getAlleleDescription_output_2.rdf

# java -Xmx2000M -Xms250M -jar galaxy-dist/tool-data/shared/jars/sadi_generic_client.jar http://sadiframework.org/examples/pdb2uniprot tests/pdb2uniprot-input.rdf > tests/pdb2uniprot-output.rdf

java -Xmx2000M -Xms250M -jar galaxy-dist/tool-data/shared/jars/sadi_generic_client.jar http://sadiframework.org/examples/uniprot2pdb tests/pdb2uniprot-output.rdf > tests/uniprot2pdb-output.rdf

# java -Xmx2000M -Xms250M -jar galaxy-dist/tool-data/shared/jars/sadi_generic_client.jar http://sadiframework.org/examples/uniprotInfo tests/pdb2uniprot-output.rdf > tests/uniprotInfo-output.rdf

java -Xmx2000M -Xms250M -jar galaxy-dist/tool-data/shared/jars/sadi_generic_client.jar http://sadiframework.org/examples/uniprot2EntrezGene tests/uniprotInfo-output.rdf > tests/uniprot2EntrezGene-output.rdf

java -Xmx2000M -Xms250M -jar galaxy-dist/tool-data/shared/jars/sadi_generic_client.jar http://dev.biordf.net/~kawas/cgi-bin/entrezGene2pubmed tests/uniprot2EntrezGene-output.rdf > tests/entrezGene2pubmed-ouput.rdf

