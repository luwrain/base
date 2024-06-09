#!/bin/bash -e
# Copyright 2024 Michael Pozhidaev <msp@luwrain.org>
# The LUWRAIN Project, GPL v.3
# Des major

INS=78
VOLUME=100
DUR=50

./melody.sh $INS $VOLUME 77 $DUR | csvmidi - > melody.midi && timidity -Ow melody.midi > /dev/null && mv melody.wav .melody-src.wav
sox -D .melody-src.wav  -r 48000 -c 1 -b 16 .melody-pre.wav bass 5
sox -D --norm=-0.1 .melody-pre.wav -c 2 .melody.wav reverb 50  fade t 0 1.5 1.5

sox -D -n -r 48000 -b 16 -c 2 .harm.wav \
    synth 10 sin %-19 sin %-15 sin %-12 gain -35  pad 0.10 \
    fade t 0.1 1.4 1.3 

sox -D .melody.wav .harm.wav -m .pre.wav
sox -D .pre.wav area-layout.wav  pad 0 1
rm -f .*.midi .*.wav
