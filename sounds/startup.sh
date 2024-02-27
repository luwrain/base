#!/bin/bash -e
# Copyright 2024 Michael Pozhidaev <msp@luwrain.org>
# The LUWRAIN Project, GPL v.3
# Ges major

DRUM_VOL=-2
DRUM_DELAY=0.0
HARM_DELAY=0.12
HARM_VOL=-10
BASS=10

NOTE=30

INST=11
DUR=300

NOTE1_VOL=-20
NOTE2_VOL=-25

cat <<EOF | csvmidi > drum.midi
0, 0, Header, 1, 4, 480
1, 0, Start_track
1, 0, Title_t, "Sound icon"
1, 0, Text_t, "LUWRAIN sound icon"
1, 0, Copyright_t, "The LUWRAIN Project"
1, 0, Time_signature, 4, 2, 24, 8
1, 0, Tempo, 500000
1, 0, End_track
2, 0, Start_track
2, 0, Instrument_name_t, "MIDI instrument"
2, 0, Program_c, 1, 118
2, 0, Note_on_c, 1, $NOTE, 120
2, 300, Note_off_c, 1, $NOTE, 0
2, 300, End_track
3, 0, Start_track
3, 0, Instrument_name_t, "MIDI instrument"
3, 0, Program_c, 1, 117
3, 0, Note_on_c, 1, $NOTE, 60
3, 300, Note_off_c, 1, $NOTE, 0
3, 300, End_track
4, 0, Start_track
4, 0, Instrument_name_t, "MIDI instrument"
4, 0, Program_c, 1, 55
4, 0, Note_on_c, 1, 66, 40
4, 300, Note_off_c, 1, 66, 0
4, 300, End_track
0, 0, End_of_file
EOF

timidity -Ow drum.midi > /dev/null
mv drum.wav _drum1.wav
sox --norm=$DRUM_VOL _drum1.wav -r 48000 -c 1 _drum2.wav pad $DRUM_DELAY 5 bass $BASS
sox _drum2.wav -c 2 _drum.wav reverb 85 50 100 100 50 5

sox -D -n -r 48000 -c 2 -b 16 _harm1.wav \
    synth 10 sin %1 sin %4 sin %9 \
    fade t 0.4 2 1.5 pad $HARM_DELAY 3 \
    gain $HARM_VOL
sox -D _harm1.wav _harm.wav reverb 100 100 100 100 20 5

./melody.sh $INST 120 90 $DUR | csvmidi - > _note1-1.midi
timidity -Ow _note1-1.midi > /dev/null
sox -D --norm=$NOTE1_VOL _note1-1.wav -r 48000 _note1.wav \
    gain -10 \
        pad 0.65 3 \
    reverb 100 50 100 100 0 10

./melody.sh $INST 120 97 $DUR | csvmidi - > _note2-1.midi
timidity -Ow _note2-1.midi > /dev/null
sox -D --norm=$NOTE2_VOL _note2-1.wav -r 48000 _note2.wav \
        pad 1.4 3 \
    reverb 100  50 100 100 0 10

sox -D _drum.wav _harm.wav _note1.wav _note2.wav -m _startup.wav
sox -D --norm=-0.1 _startup.wav -r 44100 startup.wav fade q 0 4 4
rm -f *.midi _*.wav
