/*
   Copyright 2012-2015 Michael Pozhidaev <msp@altlinux.org>

   This file is part of the Luwrain.

   Luwrain is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   Luwrain is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.speech;

/**
 * The main interface for any speech output. Luwrain holds one instance
 * of this interface to be able to "speak" to user through its whole
 * execution time. This interface is purposed for real-time processing
 * without any ways to send synthesized audio data back to the caller of
 * particular methods. The command to make silence (meaning, interrupt
 * any playing ) is important as well.
 * <p>
 * On its startup Luwrain creates one instance of this interface using
 * {@code Class.forName} method. The exact name is taken from the
 * command line option {@code --speech=} After the creation, the
 * {@code init} method is always called with providing the entire command
 * line for the case there can be any additional options, sensible for a
 * particular speech back-end implementation.
 * <p>
 * The implementation must have two context attributes: default pitch and
 * default rate. These attributes may be freely chosen from the range
 * between 0 and 100. They are take effect if corresponding arguments of
 * the methods to say are omitted.
 * <p>
 * All say methods should not interrupt any playing if there is some as a
 * result of their prior calls. If it happened that new "say" command
 * comes before the previous one isn't finished yet it should result in
 * placing new text chunk in a queue.  Queue items are played immediately
 * after the previous ones comes to silence. The {@code silence}
 * method interrupts current playing and erases queue.
 */
public interface BackEnd
{
    /** The minimal value for pitch or rate*/
    public static final int LOW = 0;

    /** The regular value for pitch or rate*/
    public static final int NORMAL = 50;

    /** The maximum value for pitch or rate*/
    public static final int HIGH = 100;

    /**
     * Performs the general initialization of the back-end. This method is
     * called by {@code Init} class just after the creation and Luwrain core
     * expects that the back-end is ready for any operations after this
     * method return. As an argument the whole command line is
     * provided and the back-end may take from it any values it wants,
     * including any additional which are addressed specifically to it.
     *
     * @param cmdLine The full command line the Luwrain environment was launched with
     *  @return The error description if initialization failes or null if everything is done successfully
     */
    String init(String[] cmdLine);

    /**
     * Gets a new utterance to be spoken. If the back-end is busy with
     * speaking the previous chunk of text, the new one should be placed into
     * queue and processed when all previous portions are handled. This
     * method doesn't take arguments neither for pitch nor for rate and
     * should use the values which are set with {@code setDefaultPitch()} and
     * {@code setDefaultRate()}.
     *
     * @param text A new utterance to speak, may not be null
     */
    void say(String text);

    /**
     * Gets a new utterance to be spoken. If the back-end is busy with
     * speaking the previous chunk of text, the new one should be placed into
     * queue and processed when all previous portions are handled. This
     * method takes the explicit argument for pitch. For rate the value
     * previously set with {@code setDefaultRate()} should be used.
     *
     * @param text A new utterance to speak, may not be null
     * @param pitch The value for pitch, should be in range between 0 and 100
     */
    void say(String text, int pitch);

    /**
     * Gets a new utterance to be spoken. If the back-end is busy with
     * speaking the previous chunk of text, the new one should be placed into
     * queue and processed when all previous portions are handled. This
     * method takes the explicit arguments for pitch and rate ignoring any
     * values previously set with {@code setDefaultPitch()} and {@code
     * setDefault Rate()}.
     *
     * @param text A new utterance to speak, may not be null
     * @param pitch The value for pitch, should be in range between 0 and 100
     * @param rate The value for rate, should be in range between 0 and 100
     */
    void say(String text, int pitch, int rate);

    /**
     * Speaks a single letter. If the back-end is busy with speaking the
     * previous utterance, this new command should be placed into queue and
     * processed when all previous portions are handled. The values
     * previously set with (@code setDefaultPitch()) and {@code
     * setDefaultRate()} should be used as effective values for pitch and
     * rate.
     * <p>
     * This method should distinguish small and capital letters, speaking
     * capital letters with higher pitch.
     *
      * @param letter A letter to speak
     */
    void sayLetter(char letter);

    /**
     * Speaks a single letter. If the back-end is busy with speaking the
     * previous utterance, this new command should be placed into queue and
     * processed when all previous portions are handled. This method takes
     * the explicit value for pitch but the number previously set with {@code
     * setDefaultRate()} should be used as an effective value for rate.  
     * <p>
     * This method should distinguish small and capital letters, speaking
     * capital letters with higher pitch.
     *
     * @param letter A letter to speak
     * @param pitch The value for pitch, should be in range between 0 and 100
     */
    void sayLetter(char letter, int pitch);

    /**
     * Speaks a single letter. If the back-end is busy with speaking the
     * previous utterance, this new command should be placed into queue and
     * processed when all previous portions are handled. This method takes
     * the explicit values for pitch and rate.
     * <p>
     * This method should distinguish small and capital letters, speaking
     * capital letters with higher pitch.
     *
     * @param letter A letter to speak
     * @param pitch The value for pitch, should be in range between 0 and 100
     * @param rate The value for rate, should be in range between 0 and 100
     */
    void sayLetter(char letter, int pitch, int rate);

    /**
     * Makes silence in speech output. This method must immediately interrupt
     * any sound of any utterance being spoken and remove all chunks of text
     * in queue if there are any.  any.
     */
    void silence();

    /**
     * Sets new default value for pitch. This value should take effect on any call of {@code say()} and {@code sayLetter()}
     *      * if explicit one isn't provided.
     *
     * @param value The new value, should be in range between 0 and 100
     */
    void setDefaultPitch(int value);

    /**
     * Sets new default value for rate. This value should take effect on any call of {@code say()} and {@code sayLetter()}
     *      * if explicit one isn't provided.
     *
     * @param value The new value, should be in range between 0 and 100
     */
    void setDefaultRate(int value);
}
