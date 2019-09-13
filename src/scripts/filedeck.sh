#!/bin/sh

# make sure logs folder exist
mkdir -p ~/logs

# launch filedeck
APP_HOME=/opt/hilel14/filedeck
java -Djava.util.logging.config.file=$APP_HOME/logging.properties -cp .:$APP_HOME/lib/* org.hilel14.filedeck.de.Main
