#!/bin/bash

solr-precreate ruian "/opt/solr/server/solr/configsets/ruianConfig" # maybe solr-create ?
solr start -force

/opt/ruian-solr-scripts/initial-update.sh

# 
