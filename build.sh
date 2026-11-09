#!/usr/bin/env bash

handle_error() {
    echo "Error occurred on line $1."
    exit 1
}
# in case of error, print error line and exit
trap 'handle_error $LINENO' ERR

./mvnw clean package

woodpecker_volume='timestamped-build'
timestamp=$(date -u +%Y%m%d%H%M%S)
build_dir="$woodpecker_volume/$timestamp"
echo "## Copying jar file to $build_dir."
mkdir -p $build_dir
cp target/*.jar $build_dir
echo "## jar file has copied."

printf "\n## Creating symbolic link for the db file.\n"
if [ ! -f $woodpecker_volume/hollin-gate.db ]; then
    echo "### Creating db file as it is not yet created."
    touch $woodpecker_volume/hollin-gate.db
fi
ln -s ../hollin-gate.db "$build_dir"
echo "## Symbolic link for the db file has created."

printf "\n## Creating a symbolic link for 'hollin-gate.yaml' in $build_dir ..\n"
if [ ! -f $woodpecker_volume/hollin-gate.yaml ]; then
    printf "###  First creating a sample as there is no 'hollin-gate.yaml' found "
    printf "in the $woodpecker_volume folder ..\n"
    while IFS= read -r line; do
        echo "$line" >> $woodpecker_volume/hollin-gate.yaml
    done < sample.hollin-gate.yaml
    echo "## Sample has created"
fi
ln -s ../hollin-gate.yaml "$build_dir"

printf "\n## Copying update-restart-systemd.sh ..\n"
cp -f update-restart-systemd.sh $woodpecker_volume

printf "\n## Creating the 'current' symbolic link.\n"
if [ -L $woodpecker_volume/current ]; then
    echo "### Deleting the symbolic link created in previous build.."
    rm $woodpecker_volume/current
fi
ln -sf "$timestamp" $woodpecker_volume/current
echo "## New symbolic link for 'current' has created."

printf "\n## Build has completed.\n"
