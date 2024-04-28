#!/bin/bash -e
# Copyright 2024 Michael Pozhidaev <msp@luwrain.org>
# The LUWRAIN Project, GPL v.3
# As major

INS=46
DUR=30

./melody.sh $INS 120 75 $DUR 80 $DUR 84 $DUR | csvmidi - > melody.midi
timidity -Ow melody.midi > /dev/null
mv melody.wav .melody-src.wav
sox -D .melody-src.wav -r 48000 -c 1 -b 16 .melody1.wav treble -5
sox -D .melody1.wav -c 2 .melody.wav

sox -D -n -r 48000 -b 16 -c 2 .harm.wav \
    synth 10 sin %-13 sin %-9 sin %-6 \
    fade t 0.5 3 2 gain -25

sox -D .melody.wav .harm.wav -m .pre.wav
sox -D --norm=-0.1 .pre.wav list-item-important.wav pad 0 0.5 reverb 65 fade t 0 2 2
rm -f *.midi .*.wav
