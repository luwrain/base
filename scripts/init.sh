
THIS="${0##*/}"

if ! [ -r config.sh ]; then
echo "$THIS:no config.sh" >&2
exit 1
fi

. config.sh

PWD=$(pwd)
SRC_DIR="${PWD%/base/scripts*}"
