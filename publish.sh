#!/bin/bash
set -e
cat <(echo -e $OSSRH_GPG_SECRET_KEY) | gpg1 --batch --import
gpg1 --list-secret-keys --keyid-format LONG

ls -al $HOME/.gnupg/

echo "app.version=$VERSION_SEMVER" >./src/main/resources/com/neuronrobotics/javacad/build.properties


./gradlew publish -Psigning.secretKeyRingFile=$HOME/.gnupg/secring.gpg -Psigning.password=$OSSRH_GPG_SECRET_KEY_PASSWORD -Psigning.keyId=$OSSRH_GPG_SECRET_KEY_ID -PstagingProgressTimeoutMinutes=15

# close and release Sonatype
./gradlew closeAndReleaseRepository -PnexusUsername=$MAVEN_USERNAME -PnexusPassword=$MAVEN_PASSWORD

