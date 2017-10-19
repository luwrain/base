
THIS="${0##*/}"

if ! [ -r config.sh ]; then
echo "$THIS:no config.sh" >&2
exit 1
fi

. config.sh

PWD=$(pwd)
SRC_DIR="${PWD%/base/scripts*}"

if ! [ -e "$SRC_DIR/base/scripts/init.sh" ]; then
    echo "$THIS:Something wrong with the current directory:no $SRC_DIR/base/scripts/init.sh"
    exit 1
fi

cd "$SRC_DIR"
