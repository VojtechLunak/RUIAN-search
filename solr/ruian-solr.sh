#!/bin/bash

solr start
solr create -c ruian -d "/opt/solr/server/solr/configsets/ruianConfig"

/opt/ruian-solr-scripts/initial-update.sh

solr stop
solr-foreground