#!/bin/bash

if [ "$1" == "build" ]; then
	docker build --tag csvmodifier ./CSVModifier/
fi

# Download RUIAN data
url=$(wget -q -O - https://nahlizenidokn.cuzk.cz/StahniAdresniMistaRUIAN.aspx | grep 'id="ctl00_bodyPlaceHolder_linkCR"' | sed -r 's/^.+href="([^"]+)".+$/\1/')
echo "Downloading RUIAN data"
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
docker run --rm -v "$PWD/temp:/temp" -v "$PWD/data:/data" csvmodifier

rm -rf temp

# Delete data in core
echo "Deleting existing data from Solr"
curl -X POST -H 'Content-Type: application/json' 'http://localhost:8983/solr/ruian/update?commit=true' -d '{ "delete": {"query":"*:*"} }'

# Index data using post tool
echo "Indexing data"
docker run --rm -v "$PWD/data:/data" --network=host solr:8.3 post -c ruian -params "separator=%3B" /data/

if [[ "$?" != 0 ]]; then
	echo "Failed to index data, check if docker is running"
	exit 1
fi

# Delete files
rm -rf data

echo "Indexing done"
exit 0
