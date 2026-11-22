# Preparing for submission

## Goal
Update the README with run instructions and an evidence index, confirm every module tag and evidence file is present, clean up and scan for secrets, verify the project from a fresh clone, then submit the repository URL.

## Steps taken
1. Updated the README
2. Checked tags and evidence
3. Cleaned up every module and exercise
4. Verified from a fresh clone

## Commands
git ls-remote --tags origin 'module-*'

for f in docs/[0-9][0-9]-*/*.md; do
  for h in "## Goal" "## Steps taken" "## Commands" "## Results" "## Observations"; do
    grep -q "^$h" "$f" || echo "$f is missing: $h"
  done
done

gitleaks git .

## Results
Had to add in a .gitleaksignore file for an older commit which had a public GHCR image listed:


    ○
    │╲
    │ ○
    ○ ░
    ░    gitleaks

Finding:     ...r.io/rachel-fennell/task-api   1.0.0     sha256:REDACTED
Secret:      REDACTED
RuleID:      generic-api-key
Entropy:     3.771825
File:        docs/05-containerising-the-task-tracker/measure-and-harden-the-images.md
Line:        51
Commit:      54d4d2b022e14c73362e21bfe3f69de38aeeb1f3

rachel@BEL-MC-260924B docker-fundamentals % gitleaks git .

    ○
    │╲
    │ ○
    ○ ░
    ░    gitleaks

5:25PM INF 12 commits scanned.
5:25PM INF scanned ~180521 bytes (180.52 KB) in 62.9ms
5:25PM INF no leaks found

## Observations
