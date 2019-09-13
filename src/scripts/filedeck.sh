#!/bin/sh

# make sure logs folder exist
mkdir -p ~/logs

# launch filedeck only if user is connected to root-folder
APP_HOME=/opt/hilel14/filedeck
if [ -d $APP_HOME ]; then
	java -Djava.util.logging.config.file=$APP_HOME/logging.properties -cp .:$APP_HOME/lib/* org.hilel14.filedeck.de.Main
else
   echo "You are not connected to RootFolder"
fi


