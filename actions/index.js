const core = require('@actions/core');
const github = require('@actions/github');

const handleStringify = payload => {
  if (payload.action === 'published') return payload.release && payload.release.target_commitish;
  return payload.ref && payload.ref.replace(`refs/heads/`, ``);
}

const exportList = {
  BRANCH_NAME: handleStringify(github.context.payload),
  REPOSITORY_NAME: github.context.payload.repository.name
}

try {
  Object.entries(exportList).forEach(entry => core.setOutput(entry[0], entry[1]));
} catch (error) {
  core.setFailed(error.message);
}