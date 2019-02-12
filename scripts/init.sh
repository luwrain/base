
THIS="${0##*/}"

. config.sh

PWD=$(pwd)
SRC_DIR="${PWD%/base/scripts*}"
SCRIPTS_DIR="$SRC_DIR/base/scripts"

if ! [ -e "$SRC_DIR/base/scripts/init.sh" ]; then
    echo "$THIS:Something wrong with the current directory:no $SRC_DIR/base/scripts/init.sh"
    exit 1
fi

cd "$SRC_DIR"
export PATH="$SCRIPTS_DIR:$PATH"
