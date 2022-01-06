# Copyright 2012-2022 Michael Pozhidaev <msp@luwrain.org>
# The LUWRAIN Project, GPL v.3

THIS="${0##*/}"

. config.sh

PWD=$(pwd)
if [ -z "$SRC_DIR" ]; then
    export SRC_DIR="${PWD%/base/scripts*}"
fi
SCRIPTS_DIR="$SRC_DIR/base/scripts"

if ! [ -e "$SRC_DIR/base/scripts/init.sh" ]; then
    echo "$THIS:Something wrong with the current directory:no $SRC_DIR/base/scripts/init.sh"
    exit 1
fi

cd "$SRC_DIR"
export PATH="$SCRIPTS_DIR:$PATH"
