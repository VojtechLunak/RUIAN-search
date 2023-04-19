#!/bin/bash

solr-precreate ruian "/opt/solr/server/solr/configsets/ruianConfig"  # maybe solr-create ?

/opt/ruian-solr-scripts/initial-update.sh

# 
