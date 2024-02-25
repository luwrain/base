#!/bin/bash -e
# Copyright 2024 Michael Pozhidaev <msp@luwrain.org>
# The LUWRAIN Project, GPL v.3
# Des major

INS=46
TEMPO=35
VOL=120

./melody.sh $INS $VOL \
	    65 $TEMPO 56 $TEMPO 63 $TEMPO 61 $TEMPO 75 45 77 50 85 300 | csvmidi - > melody.midi
timidity -Ow melody.midi > /dev/null
sox -D --norm=-0.1 melody.wav _melody2.wav fade t 0 0.3 0.3

sox -D -n -r 44100 -b 16 -c 2 _harm.wav \
    synth 10 sin %-20 sin %-16 sin %-13 \
    fade t 0.2 0.9 0.5 gain -20

sox -D _melody2.wav _harm.wav -m pre.wav

sox -D --norm=-0.1 pre.wav main-menu.wav pad 0 2 reverb 65
rm -f _*.wav *.midi melody.wav harm.wav pre.wav
