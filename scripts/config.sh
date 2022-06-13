# Copyright 2012-2022 Michael Pozhidaev <msp@luwrain.org>
# The LUWRAIN Project, GPL v.3

VERSION=1.9.3
REGISTRY_VERSION=1.9.3
GITS='base browser extensions i18n io interaction-javafx linux luwrain packs pim player reader registry studio windows'
PROPERTIES_FILE=release.properties
JUNIT_COMPONENTS='base luwrain linux browser io pim app-notepad'
SDK_JARS='base browser io pim studio'
SRC_DEST_DIR=/tmp/luwrain-src

# Used for compilation, everything must be listed regardless the target platform
LANGS='en ru'
APPS='commander contacts mail news notepad telegram twitter viewer vk'
EXTENSIONS='cmdtts emacspeak mssapi plmp3 rhvoice speechd voiceman'
COMPONENTS='base interaction-javafx io browser linux packs pim player reader studio windows'

WIN_ARCH=64
WIN_LANGS='en ro ru'
WIN_COMPONENTS='base browser interaction-javafx io packs pim player reader studio windows'
WIN_APPS='commander contacts mail news notepad telegram twitter viewer vk'
WIN_EXTENSIONS='mssapi plmp3 rhvoice'
WIN_PROPERTIES='luwrain player reader windows'

LINUX_ARCH='64'

ISO_ARCH='64'
ISO_LANGS='en ro ru'
ISO_COMPONENTS='base browser interaction-javafx io linux packs pim player reader studio'
ISO_APPS='commander contacts mail news notepad telegram twitter viewer vk'
ISO_EXTENSIONS='cmdtts emacspeak plmp3 rhvoice speechd voiceman'
ISO_PROPERTIES='linux luwrain player reader'
ISO_SCRIPTS='linux'
