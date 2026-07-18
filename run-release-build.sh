#!/usr/bin/env bash
set -uo pipefail
export JAVA_HOME='/mnt/c/Program Files/Android/openjdk/jdk-21.0.8'
export PATH="$JAVA_HOME/bin:$PATH"
export ANDROID_HOME='/mnt/c/Users/LyhourMao/AppData/Local/Android/Sdk'
export ANDROID_SDK_ROOT="$ANDROID_HOME"
cd /home/lyhourmao/Wanderlust/Wanderlust/Wanderlust
java -version
bash ./gradlew assembleRelease --no-daemon