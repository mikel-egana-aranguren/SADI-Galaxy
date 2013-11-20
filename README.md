SADI-Galaxy
===========

About
-----

This tool is able to query any SADI registry and install, in a local Galaxy server, a wrapper for each of the retrieved services. By using this tool the Galaxy web interface can be used to execute SADI services. 

More information:

* SADI: http://sadiframework.org/ 
* Galaxy: http://galaxyproject.org/

Usage
-----

The main function of this bundle is to query the registry and create a Galaxy tool for each service (a tool XML file and an entry in the `tool_conf.xml` file). Those files will be copied to the appropriate places in your Galaxy server (the `galaxy-dist` directory resembles an actual Galaxy server setting so it is self-explanatory); additionally, the tool can be configured to automatically (try to) edit your main `tool_conf.xml` file.

You can call directly `SADI-Galaxy.sh`, but it is easier to edit `RUN-SADI-Galaxy.sh`

RDFSyntaxConverter
Generic caller

You can try this tool at http://biordf.org:8983/

(Note that generic and RDFSyntaxConverter can also be obtained at tool shed)

you can simply do something like vim tool_conf, delete toolbx, and less local>> toolconf


Use cases
---------

Funding
-------

Mikel Egaña Aranguren is funded by the Marie Curie Cofund programme (FP7) of the European Union through the UPM (Spain).
