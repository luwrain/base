// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.core;

/**
 * Simplifies checking of objects validity. This class provides a set of
 * static methods which take care of checking the given data and throw
 * corresponding exceptions. All method are rather short and purposed
 * only for making code more readable and self-documented.
 */
public final class NullCheck
{
    /**
     * Checks that provided reference isn't null. 
     *
     * @param obj The reference to check 
     * @param name The reference name to construct better exception message
     * @throws NullPointerException
     */
    static public void notNull(Object obj, String name)
    {
	if (obj == null)
	    throw new NullPointerException(name + " can't be null");
    }

    /**
     * Checks that given reference isn't null and {@code obj.toString.isEmpty()} returns false.
     *
     * @param obj The reference to check 
     * @param name The reference name to construct better exception message
     * @throws NullPointerException IllegalArgumentException
     */
    static public void notEmpty(Object obj, String name)
    {
	if (obj == null)
	    throw new NullPointerException(name + " can't be null");
	if (obj.toString().isEmpty())
	    throw new IllegalArgumentException(name + " can't be empty");
    }

    /**
     * Checks that the array reference isn't null and none of its items are null
     * (if there are any).
     *
     * @param items The reference to an array to check
     * @param name The reference name to construct better exception message
     * @throws NullPointerException
     */
    static public void notNullItems(Object[] items, String name)
    {
	notNull(items, name);
	for(int i = 0;i < items.length;++i)
	    if (items[i] == null)
		throw new NullPointerException(name + "[" + i + "] can't be null");
    }

    /**
     * Checks that the array reference isn't null and {@code items[i].toString().isEmpt ()} returns false 
     * for all items (if there are any).
     *
     * @param items The reference to an array to check
     * @param name The reference name to construct better exception message
     * @throws NullPointerException IllegalArgumentException
     */
    static public void notEmptyItems(Object[] items, String name)
    {
	if (items == null)
	    throw new NullPointerException(name + " can't be null");
	for(int i = 0;i < items.length;++i)
	{
	    if (items[i] == null)
		throw new NullPointerException(name + "[" + i + "] can't be null");
	    if (items[i].toString().isEmpty())
		throw new IllegalArgumentException(name + "[" + i + "] can't be empty");
	}
    }

    /**
     * Checks that the array reference isn't null and the array has at least one item.
     *
     * @param items The reference to an array to check
     * @param name The reference name to construct better exception message
     * @throws NullPointerException IllegalArgumentException
     */
    static public void notEmptyArray(Object[] items, String name)
    {
	if (items == null)
	    throw new NullPointerException(name + " can't be null");
	if (items.length < 1)
	    throw new IllegalArgumentException(name + " can't be empty");
    }
}
