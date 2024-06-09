#!/bin/bash -e
# Copyright 2024 Michael Pozhidaev <msp@luwrain.org>
# The LUWRAIN Project, GPL v.3
# Des major

INS=78
VOLUME=100
DUR=120

./melody.sh $INS $VOLUME 77 $DUR 73 $DUR | csvmidi - > melody.midi && timidity -Ow melody.midi > /dev/null && mv melody.wav .melody-src.wav
sox -D .melody-src.wav  -r 48000 -c 1 -b 16 .melody.wav bass 5
sox -D --norm=-0.1 .melody.wav -c 2 area-layout-double.wav reverb 65 fade t 0 2 2
rm -f .*.midi .*.wav
