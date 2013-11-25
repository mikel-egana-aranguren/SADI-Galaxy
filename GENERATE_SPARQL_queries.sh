#!/bin/sh

java -jar generate_sparql_queries.jar sparql/basequeries/publisher.sparql wilkinsonlab.info illuminae.com dev.biordf.net Cyber-ShARE sadiframework.org

java -jar generate_sparql_queries.jar sparql/basequeries/predicate.sparql http://sadiframework.org/ontologies/properties.owl#hasName