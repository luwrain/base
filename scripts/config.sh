
VERSION=1.1.0
GITS='base doctree education extensions i18n interaction-javafx linux luwrain network pim registry windows'
PROPERTIES_FILE=release.properties

# Used for compilation, everything must be listed regardless the target platform
DBS='news contacts mail'
LANGS='en ru'
APPS='notepad news fetch commander twitter reader message mail term contacts browser player chat writer'
EXTENSIONS='voiceman emacspeak cmdtts yatran mssapi rhvoice'
COMPONENTS='base pim doctree interaction-javafx education linux windows network'

WIN_ARCH=32
WIN_LANGS='ru'
WIN_COMPONENTS='base pim doctree interaction-javafx education windows'
WIN_APPS='notepad news fetch commander twitter reader message mail contacts browser player chat'
WIN_EXTENSIONS='mssapi cmdtts yatran rhvoice'

LINUX_ARCH='32 64'
LINUX_LANGS='ru'
LINUX_COMPONENTS='base pim doctree interaction-javafx education linux'
LINUX_APPS='notepad news fetch commander twitter reader message mail term contacts browser player chat'
LINUX_EXTENSIONS='voiceman emacspeak cmdtts yatran rhvoice'

ISO_ARCH='32 64'
ISO_LANGS='ru'
ISO_COMPONENTS='base pim doctree interaction-javafx education network linux'
ISO_APPS='notepad news fetch commander twitter reader message mail term contacts browser player chat'
ISO_EXTENSIONS='voiceman emacspeak cmdtts yatran rhvoice'


TAKE_SCRIPTS='network linux app-reader'
TAKE_PROPERTIES='luwrain'
SDK_JARS='base pim doctree network linux windows'
SRC_DEST_DIR=/tmp/luwrain-src
