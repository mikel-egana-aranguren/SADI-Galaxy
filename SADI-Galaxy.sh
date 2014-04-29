#!/bin/sh


###############################################################
######################### SADI-Galaxy #########################
###############################################################


#### ABOUT ####################################################

# This script is able to install SADI services in a Galaxy server.

# The mimimun setting installs a SADI generic client and associated tools (RDF
# Syntax Converter and Merge Graphs). Additionally, concrete SADI services can
# be installed by executing SPARQL queries against a SADI registry.

# Look at log at the end.

###############################################################


#### CONFIGURATION ############################################

# Change variables bellow to make it work in your server. 

# (Compulsory) the absolute path to your galaxy installation. Change as
# necessary

GALAXY="/home/mikel/galaxy-dist/"

# (Compulsory) edit you galaxy's tool_conf.xml? Uncomment as necessary.

# "no_edit": don't edit. You will have to manually merge the generated
# tool_conf.xml with your server's tool_conf.xml

# "edit": the script will merge both files, i.e. append SADI services to
# tool_conf.xml. Use with care.

EDIT="no_edit"
# EDIT="edit"

# The following two options are needed only if you want to generate a given set
# of SADI services: uncomment and edit if needed, otherwise leave commented.

# (Optional) SADI registry endpoint containing information about SADI services.

ENDPOINT="http://dev.biordf.net/sparql/" 

# (Optional) Relative path (according to this script) to the directory that
# contains the SPARQL queries to execute against the endpoint

QUERY_DIR="sparql/queries/"

###############################################################


#### RUN ######################################################

# You shouldn't touch anything here

# If endpoint is set, generate service tools: otherwise only generate basic
# system

if [ ! -z ${ENDPOINT+x} ]; 
    then

	# Create new tool_conf.xml file
  
	echo ""
	echo "INFO: Generating galaxy-dist/tool_conf.xml for SADI-Galaxy generic tool, RDF Syntax Converter, Merge RDF Graphs and the SADI services defined in $QUERY_DIR"
	echo ""
	echo "	<section name=\"SADI services\" id=\"SADI\">" > galaxy-dist/tool_conf.xml
	echo "		<label text=\"SADI common utilities\" id=\"SADI-common-utilities\"/>" >> galaxy-dist/tool_conf.xml
	echo "			<tool file=\"SADI/sadi_generic.xml\"/>" >> galaxy-dist/tool_conf.xml
	echo "			<tool file=\"SADI/RDFSyntaxConverter.xml\"/>" >> galaxy-dist/tool_conf.xml
	echo "			<tool file=\"SADI/mergeRDFgraphs.xml\"/>" >> galaxy-dist/tool_conf.xml
	echo "		<label text=\"SADI services\" id=\"SADI-services\"/>" >> galaxy-dist/tool_conf.xml
  
	# Execute queries and add each generated tool as a line in tool_conf.xml
  
	LIST=$QUERY_DIR"*"
  
	for QUERY in $LIST
	  do
	    echo "INFO: executing tool generator with $QUERY"
	    java -jar sadi_tool_generator.jar $QUERY $ENDPOINT galaxy-dist/tool_conf.xml galaxy-dist/tools/SADI/ 2 > log 
	  done
  
	echo "	</section>" >> galaxy-dist/tool_conf.xml
	echo "</toolbox>" >> galaxy-dist/tool_conf.xml
	echo ""
  
    else
  
	echo ""
	echo "INFO: Generating galaxy-dist/tool_conf.xml for the SADI-Galaxy generic tool, RDF Syntax Converter and Merge RDF Graphs"
	echo ""
	echo "	<section name=\"SADI services\" id=\"SADI\">" > galaxy-dist/tool_conf.xml
	echo "		<label text=\"SADI common utilities\" id=\"SADI-common-utilities\"/>" >> galaxy-dist/tool_conf.xml
	echo "			<tool file=\"SADI/sadi_generic.xml\"/>" >> galaxy-dist/tool_conf.xml
	echo "			<tool file=\"SADI/RDFSyntaxConverter.xml\"/>" >> galaxy-dist/tool_conf.xml
	echo "			<tool file=\"SADI/mergeRDFgraphs.xml\"/>" >> galaxy-dist/tool_conf.xml
	echo "	</section>" >> galaxy-dist/tool_conf.xml
	echo "</toolbox>" >> galaxy-dist/tool_conf.xml
fi

GALAXY_SADI=$GALAXY"tools/SADI/"

# Create SADI directory in server if it doesn't exist 
if [ ! -d $GALAXY_SADI ]; 
 then
   mkdir $GALAXY_SADI
fi
 
# Copy everything
cp galaxy-dist/tools/SADI/* $GALAXY_SADI
cp galaxy-dist/tool-data/shared/jars/sadi_generic_client.jar $GALAXY"/tool-data/shared/jars/"
cp galaxy-dist/tool-data/shared/jars/RDFSyntaxConverter.jar $GALAXY"/tool-data/shared/jars/"
cp galaxy-dist/tool-data/shared/errwrap.sh $GALAXY"/tool-data/shared/"
cp galaxy-dist/test-data/* $GALAXY"/test-data/" 


# Edit main tool_conf.xml if needed
if [ $EDIT = "edit" ];
  then
    
    # !!! Use with care !!! 
    # It will add the tool_conf.xml bit to the tool_confx.xml of the galaxy
    # server you want SADI to be installed in
    # (SADI will appear at the end: it can also handle already existing SADI
    # sections, if there are at the end, if they are not it 
    # will delete everything till </toolbox>, and you will have to recover from
    # tool_conf.xml.bk)

    MAIN_TOOL_CONF=$GALAXY"tool_conf.xml"   
    TMP_TOOL_CONF=$GALAXY"tool_conf.xml.tmp"
    cp $MAIN_TOOL_CONF $galaxy"tool_conf.xml.bk"
    cat $MAIN_TOOL_CONF | sed '/<section name="SADI/,/<\/toolbox>/d' | grep -v "</toolbox>" > $TMP_TOOL_CONF
    less galaxy-dist/tool_conf.xml >> $TMP_TOOL_CONF
    mv $TMP_TOOL_CONF $MAIN_TOOL_CONF
    echo ""
    echo "INFO: Editing $MAIN_TOOL_CONF"
    echo ""
  else
    echo "INFO: Edit $GALAXY""tool_conf.xml by adding the content of ./galaxy-dist/tool_conf.xml to it"
    echo ""
fi

###############################################################
  
