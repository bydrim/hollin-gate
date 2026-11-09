#!/usr/bin/env bash

handle_error() {
    echo "Error occurred on line $1."
    exit 1
}
# in case of error, print error line and exit
trap 'handle_error $LINENO' ERR

echo "INFO: This script should be running on the same environment with the hollin-gate itself, \
not in a compilation container or in a different virtual environment!"

if ! test -z "$(sudo -nv 2>&1)" ; then
    echo "This script requires admin/root/sudo access!"
    exit 1
fi

script_dir=$(dirname $(realpath "$0"))
if [ ! -L $script_dir/current ]; then
    echo "$script_dir/current symbolic link folder has not found! This is where the service should be located!"
    exit 1
fi

jar_loc=$(find -L "$script_dir/current" -regex ".*hollin-gate.*jar" -print -quit)
if [ $jar_loc = "" ]; then
    echo "Cannot locate hollin-gate jar file! Jar file is required for systemd service to know what to run!"
    exit 1
fi

if ! command -v java >/dev/null 2>&1 ; then
    echo "'java' not found! It is required for service to run the .jar!"
    exit 1
fi

service_content="\
[Unit]
Description=Hollin Gate Gateway Service
After=network.target
StartLimitIntervalSec=0

[Service]
Type=simple
Restart=always
RestartSec=1
ExecStart=java -Dspring.profiles.active=prod -jar "$jar_loc"

[Install]
WantedBy=multi-user.target"

echo "Overwriting /etc/systemd/system/hollin-gate.service file.."
printf "$service_content" | tee "/etc/systemd/system/hollin-gate.service" >/dev/null

echo "Enabling & restarting hollin-gate service .."
systemctl enable hollin-gate
systemctl restart hollin-gate

echo "Success."
