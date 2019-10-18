#!/bin/sh

# make sure logs folder exist
mkdir -p ~/logs

# set application folder relative to the location of this scripts (remark next 2 lines to set application folder path directly)
APP_HOME=`dirname $0`
APP_HOME=`dirname $APP_HOME`

# or set application folder directly
APP_HOME=/Volumes/RootFolder/FileDeck

# launch filedeck
java -Djava.util.logging.config.file=$APP_HOME/resources/logging.properties -cp $APP_HOME/resources:$APP_HOME/lib/* org.hilel14.filedeck.de.Main


# copy and paste into Apple Script Editor
# do shell script "/path/to/this/script"
# then export as application