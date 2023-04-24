#!/bin/bash

csv_data_process() {
  if find "$dir"CSV/ -name "*.csv"; then
    data_created=$(find "$dir"CSV/*.csv | head -1 | grep -Po '[0-9]{8}(?=_)' )
    url=$(wget -q -O - https://nahlizenidokn.cuzk.cz/StahniAdresniMistaRUIAN.aspx | grep 'id="ctl00_bodyPlaceHolder_linkCR"' | sed -r 's/^.+href="([^"]+)".+$/\1/')
    csv_archive=${url##*/}
    last_update=$(echo "$csv_archive" | grep -Po '[0-9]{8}(?=_)')
    if [ "$data_created" != "$last_update" ]; then
      echo "Newer data available. Updating solr..."
      /opt/ruian-solr-scripts/update.sh
    else
      echo "Data are present and up-to-date."
    fi
  else
    echo "No data found. Updating solr..."
    /opt/ruian-solr-scripts/update.sh
  fi
}

#find "/var/solr/data" -iname "*.csv" -quit | grep -Po '.{8}(?=_)'
echo "Starting initial update"
dir="/opt/solr-8.3.1/data/"

# downloads CSV files from ruian if they are not downloaded yet

if [ -d "$dir" ]; then
  csv_data_process
else
  /opt/ruian-solr-scripts/update.sh
fi
echo "Initial update check done"
