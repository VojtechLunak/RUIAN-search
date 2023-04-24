#!/bin/bash

# Download RUIAN data
echo "Downloading RUIAN data"
url=$(wget -q -O - https://nahlizenidokn.cuzk.cz/StahniAdresniMistaRUIAN.aspx | grep 'id="ctl00_bodyPlaceHolder_linkCR"' | sed -r 's/^.+href="([^"]+)".+$/\1/')
#url="https://vdp.cuzk.cz/vymenny_format/csv/20230331_OB_530115_ADR.csv.zip"
wget "$url"
unzip -a -- *.zip
rm -rf -- *.zip

echo "Modifying csv files..."
mkdir -p data
mkdir -p temp
for file in CSV/*.csv; do
	outputFile=${file#*/}
	# Convert encoding from Windows 1250 to UTF-8
	iconv -f CP1250 -t UTF-8 "$file" -o "temp/$outputFile"
	rm "$file"
done

rm -rf CSV

# Update data in csv files using java application.
# Add 2 new columns. One containing house number and orientational number combined
# and the other containing coordinates converted to wgs84.
#docker run --rm -v "$PWD/temp:/temp" -v "$PWD/data:/data" csvmodifier
java -jar /opt/csv-modifier/csv-modifier.jar

if [[ "$?" != 0 ]]; then
	echo "Failed to index data, check if docker is running"
	exit 1
fi

rm -rf temp

# Delete data in core
echo "Deleting existing data from Solr"
# Using default docker "localhost" IP address
curl -X POST -H 'Content-Type: application/json' 'http://172.17.0.1:8983/solr/ruian/update?commit=true' -d '{ "delete": {"query":"*:*"} }'

# Index data using post tool
echo "Indexing data"
/opt/solr/bin/post -c ruian -params "separator=%3B" /opt/solr-8.3.1/data/

echo "Indexing done"
exit 0
