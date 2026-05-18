#!/usr/bin/env bash
set -euo pipefail

GITHUB_USER="liouyang19"
REPO="android-gradle-plugins"

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

usage() {
    cat <<EOF
Usage: $(basename "$0") <version> [commit-message]

Publish a new release to JitPack.

Arguments:
  version          Release version (e.g. 1.3.2)
  commit-message   Optional commit message. If omitted and there are
                   uncommitted changes, the script will abort.

Examples:
  $(basename "$0") 1.3.2                         # no changes, just tag
  $(basename "$0") 1.3.2 "fix: some bug"         # commit + tag
  $(basename "$0") 1.3.2 "$(git log --oneline -1 --format=%s)"  # re-use last commit msg
EOF
    exit 1
}

if [ $# -lt 1 ]; then
    usage
fi

VERSION="$1"
COMMIT_MSG="${2:-}"

if ! echo "$VERSION" | grep -qP '^\d+\.\d+\.\d+$'; then
    echo -e "${RED}Error: version must be in X.Y.Z format (e.g. 1.3.2)${NC}"
    exit 1
fi

cd "$(git rev-parse --show-toplevel)"

if [ -n "$(git status --porcelain)" ]; then
    if [ -z "$COMMIT_MSG" ]; then
        echo -e "${RED}Error: you have uncommitted changes. Provide a commit message or stash them.${NC}"
        git status --short
        exit 1
    fi
    echo -e "${YELLOW}Committing changes...${NC}"
    git add -A
    git commit -m "$COMMIT_MSG"
    echo -e "${GREEN}Committed:${NC} $(git log --oneline -1)"
else
    echo -e "${GREEN}Working tree clean, no commit needed.${NC}"
fi

TAG_EXISTS=$(git tag -l "$VERSION")
if [ -n "$TAG_EXISTS" ]; then
    echo -e "${YELLOW}Tag $VERSION already exists locally. Deleting...${NC}"
    git tag -d "$VERSION"
fi

REMOTE_TAG_EXISTS=$(git ls-remote --tags origin "refs/tags/$VERSION" 2>/dev/null || true)
if [ -n "$REMOTE_TAG_EXISTS" ]; then
    echo -e "${YELLOW}Tag $VERSION exists on remote. Deleting...${NC}"
    git push origin ":refs/tags/$VERSION"
fi

echo -e "${YELLOW}Creating tag $VERSION...${NC}"
git tag -a "$VERSION" -m "Release v$VERSION"

echo -e "${YELLOW}Pushing to GitHub...${NC}"
git push origin main
git push origin "$VERSION"

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN} Release v$VERSION published!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "  JitPack:  https://jitpack.io/#${GITHUB_USER}/${REPO}/${VERSION}"
echo -e "  GitHub:   https://github.com/${GITHUB_USER}/${REPO}/releases/tag/${VERSION}"
echo ""
