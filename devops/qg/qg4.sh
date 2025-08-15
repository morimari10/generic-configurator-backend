#!/bin/sh

###     Lint exceptions for shellcheck. This part has to be right after the shebang !
# shellcheck disable=SC2034
# SC2034: skip check for unused variables

echo "Running QG #4"
echo "branch ${BRANCH_NAME}"
echo "commit ${GITHUB_SHA}"

echo "QG #3"
/bin/sh devops/qg/qg3.sh
if [ $? -ne 0 ]; then
  exit 255
fi

echo "QG #4 done at $(date +"%Y-%m-%d %H:%M:%S")!"