#!/bin/sh

echo "IMPORTANT: use this script in its current directory, if moved relative paths will not work"
echo ""

deploy(){

  # GET THE ARGUMENTS
  
  GALAXY=$1 # Path to your Galaxy installation: /home/mikel/galaxy-dist/
  ENDPOINT=$2 # SADI registry endpoint: http://dev.biordf.net/sparql/
  QUERY=$3 # Path to SPARQL query (use this to tune the retrieved SADI services): sparql/simple_query.sparql
  GENERIC=$4 # Deploy only the generic Galaxy tool (generic) or a tool for each of the services retrieved by the SPARQL query (all): generic|all
  EDIT=$5 # Attempt to edit Galaxy tool_conf.xml (edit) or not (no_edit)
  
  # CREATE LOCAL TOOL_CONF.XML FILE

  if [ $GENERIC = "generic" ]; 
    then
      echo "Generating galaxy-dist/tool_conf.xml for the generic Galaxy tool, RDF2TAB and RDFSyntaxConverter"
      echo "	<section name=\"SADI services\" id=\"SADI\">" > galaxy-dist/tool_conf.xml
      echo "		<label text=\"SADI common utilities\" id=\"SADI-common-utilities\"/>" >> galaxy-dist/tool_conf.xml
      echo "			<tool file=\"SADI/sadi_generic.xml\"/>" >> galaxy-dist/tool_conf.xml
      echo "			<tool file=\"SADI/RDFSyntaxConverter.xml\"/>" >> galaxy-dist/tool_conf.xml
      echo "	</section>" >> galaxy-dist/tool_conf.xml
      echo "</toolbox>" >> galaxy-dist/tool_conf.xml
    else
      echo "Generating galaxy-dist/tool_conf.xml for the generic Galaxy tool, RDF2TAB, RDFSyntaxConverter and the SADI services defined in $QUERY"
      java -jar sadi_tool_generator.jar $QUERY $ENDPOINT galaxy-dist/tool_conf.xml galaxy-dist/tools/SADI/ > log 2 > error_log 
  fi
  
  # EDIT TOOL_CONF.XML IN GALAXY SERVER INSTALLATION FOLLOWING NEW TOOL_CONF.XML, IF NECCESARY
  if [ $EDIT = "edit" ];
    then
    
      # !!! Use with care !!! 
      # It will add the tool_conf.xml bit to the tool_confx.xml of the galaxy server you want SADI to be installed in
      # (SADI will appear at the end: it can also handle already existing SADI sections, if there are at the end, if they are not it 
      # will delete everything till </toolbox>, and you will have to recover from tool_conf.xml.bk)

      MAIN_TOOL_CONF=$GALAXY"tool_conf.xml"   
      TMP_TOOL_CONF=$GALAXY"tool_conf.xml.tmp"
      cp $MAIN_TOOL_CONF $galaxy"tool_conf.xml.bk"
      cat $MAIN_TOOL_CONF | sed '/<section name="SADI/,/<\/toolbox>/d' | grep -v "</toolbox>" > $TMP_TOOL_CONF
      less galaxy-dist/tool_conf.xml >> $TMP_TOOL_CONF
      mv $TMP_TOOL_CONF $MAIN_TOOL_CONF
      echo "Editing $MAIN_TOOL_CONF"
    else
      echo "Edit Galaxy serve tool_conf.xml by adding the content of galaxy-dist/tool_conf.xml"
  fi
  
  # COPY THE NECCESARY FILES

  GALAXY_SADI=$GALAXY"tools/SADI/"

  # Create SADI directory in server if it doesn't exist 
  if [ ! -d $GALAXY_SADI ]; 
  then
    mkdir $GALAXY_SADI
  fi
 
  # Copy
  cp galaxy-dist/tools/SADI/* $GALAXY_SADI
  cp galaxy-dist/tool-data/shared/jars/sadi_generic_client.jar $GALAXY"/tool-data/shared/jars/"
  cp galaxy-dist/tool-data/shared/jars/RDFSyntaxConverter.jar $GALAXY"/tool-data/shared/jars/"
}


# CHECK ARGUMENTS AND EXECUTE MAIN METHOD

# Argument checking is not very good, e.g. if $GENERIC is anything different to "generic" it will assume value "all" 

if [ $# -eq 5 ];
  then
    deploy $1 $2 $3 $4 $5
  else
    echo "Arguments:"
    echo "Path to your Galaxy installation: /home/mikel/galaxy-dist/"
    echo "SADI registry endpoint: http://dev.biordf.net/sparql/"
    echo "Path to SPARQL query (use this to tune the retrieved SADI services): simple_query.sparql"
    echo "Deploy only the generic Galaxy tool (generic) or a tool for each of the services retrieved by the SPARQL query (all): generic|all"
    echo "Attempt to edit Galaxy tool_conf.xml (edit) or not (no_edit). If no_edit is selected, copy the lines from the generated tool_conf.xml to the tool_conf.xml of your galaxy server: edit|no_edit"
fi





