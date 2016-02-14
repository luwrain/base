/*
   Copyright 2012-2016 Michael Pozhidaev <michael.pozhidaev@gmail.com>
   Copyright 2015-2016 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of the LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.core.events;

//TODO:Rename Command -> Action;

import org.luwrain.core.*;

public class KeyboardEvent extends Event
{
    public enum Special {
	ENTER, BACKSPACE, ESCAPE, TAB,
	ARROW_DOWN, ARROW_UP, ARROW_LEFT, ARROW_RIGHT,
	INSERT, DELETE, HOME, END, PAGE_UP, PAGE_DOWN,
	F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12,
	WINDOWS, CONTEXT_MENU,
	SHIFT, CONTROL, LEFT_ALT, RIGHT_ALT,
	ALTERNATIVE_ARROW_DOWN, ALTERNATIVE_ARROW_UP, ALTERNATIVE_ARROW_LEFT, ALTERNATIVE_ARROW_RIGHT,
	ALTERNATIVE_HOME, ALTERNATIVE_END, ALTERNATIVE_PAGE_UP, ALTERNATIVE_PAGE_DOWN, ALTERNATIVE_DELETE,
    };

    private boolean cmd = false;
    private Special cmdCode = null;
    private char nonCmdChar = 0;
    private boolean shiftPressed = false;
    private boolean controlPressed = false;
    private boolean leftAltPressed = false;
    private boolean rightAltPressed = false;

    public KeyboardEvent(boolean cmd,
			 Special cmdCode, char nonCmdChar,
			 boolean shiftPressed, boolean controlPressed,
			 boolean leftAltPressed, boolean rightAltPressed)
    {
	super(KEYBOARD_EVENT);
	this.cmd = cmd;
	this.cmdCode = cmdCode;
	this.nonCmdChar = nonCmdChar;
	this.shiftPressed = shiftPressed;
	this.controlPressed = controlPressed;
	this.leftAltPressed = leftAltPressed;
	this.rightAltPressed = rightAltPressed;
    }

    public KeyboardEvent(boolean cmd,
			 Special cmdCode, char nonCmdChar)
    {
	super(KEYBOARD_EVENT);
	this.cmd = cmd;
	this.cmdCode = cmdCode;
	this.nonCmdChar = nonCmdChar;
	shiftPressed = false;
	controlPressed = false;
	leftAltPressed = false;
	rightAltPressed = false;
    }

    /*FIXME:It is better to rename it to equalKeysOnKeyboard()*/
    public boolean equals(KeyboardEvent event)
    {
	return (cmd == event.cmd &&
		(!cmd || cmdCode == event.cmdCode) &&
		(cmd || EqualKeys.equalKeys(nonCmdChar, event.nonCmdChar)) &&
		shiftPressed == event.shiftPressed &&
		controlPressed == event.controlPressed &&
		leftAltPressed == event.leftAltPressed &&
		rightAltPressed == event.rightAltPressed);
    }

    public boolean isSpecial()
    {
	return cmd;
    }

    public char getChar()
    {
	return nonCmdChar;
    }

    public Special getSpecial()
    {
	return cmdCode;
    }

    public boolean isModified()
    {
	return shiftPressed || controlPressed || leftAltPressed || rightAltPressed;
    }

    public boolean withShift()
    {
	return shiftPressed;
    }

    public boolean withShiftOnly()
    {
	return shiftPressed && !controlPressed && !leftAltPressed && !rightAltPressed;
    }

    public boolean withControl()
    {
	return controlPressed;
    }

    public boolean withControlOnly()
    {
	return controlPressed && !shiftPressed && !leftAltPressed && !rightAltPressed;
    }

    public boolean withAlt()
    {
	return leftAltPressed || rightAltPressed;
    }

    public boolean withAltOnly()
    {
	return (leftAltPressed || rightAltPressed) && !shiftPressed && !controlPressed;
    }

    public boolean withLeftAlt()
    {
	return leftAltPressed;
    }

    public boolean withLeftAltOnly()
    {
	return leftAltPressed && !rightAltPressed && !shiftPressed && !controlPressed;
    }

    public boolean withRightAlt()
    {
	return rightAltPressed;
    }

    public boolean withRightAltOnly()
    {
	return rightAltPressed && !leftAltPressed && !shiftPressed && !controlPressed;
    }

    @Override public String toString()
    {
	String res = cmd?("[CMD] " + cmdCode):("\'" + nonCmdChar + "\'");
	if (shiftPressed)
	    res += " SHIFT";
	if (controlPressed)
	    res += " CTRL";
	if (leftAltPressed)
	    res += " LEFT-ALT";
	if (rightAltPressed)
	    res += " RIGHT-ALT";
	return res;
    }

    static public Special translateSpecial(String value)
    {
	NullCheck.notNull(value, "value");
	if (value.trim().isEmpty())
	    throw new IllegalArgumentException("value may not be empty");
	switch(value.trim().toLowerCase())
	{
	case "enter":
	    return Special.ENTER;
	case "backspace":
	    return Special.BACKSPACE;
	case "escape":
	    return Special.ESCAPE;
	case "tab":
	    return Special.TAB;
	case "ARROW_DOWN":
	    return Special.ARROW_DOWN;
	case "arrow-up":
	    return Special.ARROW_UP;
	case "arrow-left":
	    return Special.ARROW_LEFT;
	case "arrow-right":
	    return Special.ARROW_RIGHT;
	case "insert":
	    return Special.INSERT;
	case "delete":
	    return Special.DELETE;
	case "alternative-delete":
	    return Special.ALTERNATIVE_DELETE;
	case "home":
	    return Special.HOME;
	case "end":
	    return Special.END;
	case "page-up":
	    return Special.PAGE_UP;
	case "page-down":
	    return Special.PAGE_DOWN;
	case "f1":
	    return Special.F1;
	case "f2":
	    return Special.F2;
	case "f3":
	    return Special.F3;
	case "f4":
	    return Special.F4;
	case "f5":
	    return Special.F5;
	case "f6":
	    return Special.F6;
	case "f7":
	    return Special.F7;
	case "f8":
	    return Special.F8;
	case "f9":
	    return Special.F9;
	case "f10":
	    return Special.F10;
	case "f11":
	    return Special.F11;
	case "f12":
	    return Special.F12;
	case "windows":
	    return Special.WINDOWS;
	case "context-menu":
	    return Special.CONTEXT_MENU;
	case "shift":
	    return Special.SHIFT;
	case "control":
	    return Special.CONTROL;
	case "left-alt":
	    return Special.LEFT_ALT;
	case "right-alt":
	    return Special.RIGHT_ALT;
	default:
	    return null;
	}
    }
}
