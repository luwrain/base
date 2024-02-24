#!/bin/bash -e
# Copyright 2024 Michael Pozhidaev <msp@luwrain.org>
# The LUWRAIN Project, GPL v.3
# E major

DUR=140
INS=10

./melody.sh $INS 120 71 $DUR 76 $DUR 80 $DUR 78 $DUR 83 300 | csvmidi - > melody.midi
timidity -Ow melody.midi

sox -n -r 44100 -b 16 -c 2 harm.wav \
    synth 10 sin %-16 sin %-13 sin %-10 \
    fade t 0.5 1 0.5 gain -25

sox melody.wav harm.wav -m pre.wav



sox --norm=-0.1 pre.wav done.wav pad 0 1 reverb 65
rm -f *.midi melody.wav harm.wav pre.wav

