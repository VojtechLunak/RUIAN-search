#!/bin/bash

csv_data_process() {
  if [ -f "*.csv" ]; then
    echo "csv file found"
    data_created=$(find "$DIR" -iname "*.csv" -quit | grep -Po '.{8}(?=_)')
    url=$(wget -q -O - https://nahlizenidokn.cuzk.cz/StahniAdresniMistaRUIAN.aspx | grep 'id="ctl00_bodyPlaceHolder_linkCR"' | sed -r 's/^.+href="([^"]+)".+$/\1/')
    csv_archive=${url##*/}
    last_update=$(grep -Po '.{8}(?=_)' "$csv_archive")
    if [ "$data_created" != "$last_update" ]; then
      /opt/ruian-solr-scripts/update.sh
    fi
  else
    /opt/ruian-solr-scripts/update.sh
  fi
}


echo "starting initial update"
dir="/var/solr/data"

# downloads CSV files from ruian if they are not downloaded yet

if [ -d "$dir" ]; then
  csv_data_process
else
  /opt/ruian-solr-scripts/update.sh
fi
echo "Initial update done"

### test if /data/*csv exists

