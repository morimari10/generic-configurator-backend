#!/bin/sh

###     Lint exceptions for shellcheck. This part has to be right after the shebang !
# shellcheck disable=SC2034
# SC2034: skip check for unused variables

index="8"
while echo "${GITHUB_SHA}" | cut -c1-"${index}" | grep -E "^[0-9]+$" > /dev/null ; do
    index="$((index+1))"
done
SHORTEN_COMMIT="$(echo "${GITHUB_SHA}" | cut -c1-"${index}")"
MAVEN_REPO="s3://maven-repo.sche-mcc.info-origin"

echo "Running QG #1"
echo "branch ${BRANCH_NAME}"
echo "commit ${GITHUB_SHA}"
echo "SHORTEN_COMMIT ${SHORTEN_COMMIT}"

echo "checkstyle"
bash ./gradlew checkstyleMain checkstyleTest \
    -PMavenRepoUrl="${MAVEN_REPO}" \
    -PbuildHash="${SHORTEN_COMMIT}" \
    -Penvironment=REMOTE
if [ $? -ne 0 ]; then
  exit 255
fi

echo "pmd"
bash ./gradlew pmdMain pmdTest \
    -PMavenRepoUrl="${MAVEN_REPO}" \
    -PbuildHash="${SHORTEN_COMMIT}" \
    -Penvironment=REMOTE
if [ $? -ne 0 ]; then
  exit 255
fi

echo "findbugs"
bash ./gradlew findbugsMain findBugsTest \
    -PMavenRepoUrl="${MAVEN_REPO}" \
    -PbuildHash="${SHORTEN_COMMIT}" \
    -Penvironment=REMOTE
if [ $? -ne 0 ]; then
  exit 255
fi

echo "QG #1 done at $(date +"%Y-%m-%d %H:%M:%S")!"