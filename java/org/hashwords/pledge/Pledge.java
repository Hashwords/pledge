/*
 * Copyright (c) 2016 Sam Hart. All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package org.hashwords.pledge;

import java.util.List;
import java.util.Vector;

/**
 * Java Native Interface to OpenBSD pledge(2)
 *
 * @see <a href="http://man.openbsd.org/pledge">pledge(2)</a>
 */
public class Pledge
{
    // Load libPledge.so when the class loads.
    static
    {
	try
	{
	    System.loadLibrary("Pledge");
	}
	catch(SecurityException e)
	{
	    System.err.println(e);
	    System.exit(-1);
	}
	catch(UnsatisfiedLinkError e)
	{
	    System.err.println(e);
	    System.exit(-1);
	}
	catch(NullPointerException e)
	{
	    System.err.println("Inconceivable : " + e);
	    System.exit(-1);
	}
    }

    /**
     * Restrict the current process.
     * @param promises
     *
     * @see <a href="http://man.openbsd.org/pledge">pledge(2)</a>
     */
    private native static void pledge(String promises,String[] paths);

    /**
     * List of available promises.
     *
     * @see /usr/src/sys/sys/pledge.h
     */
    public final static String[] PROMISE_NAMES =
    {
	"rpath" , "wpath" , "cpath" , "stdio" ,
	"tmppath" , "dns" , "inet" , "flock" ,
	"unix" , "id" , "ioctl" , "getpw" ,
	"proc" , "settime" , "fattr" , "prot_exec" ,
	"tty" , "sendfd" , "recvfd" , "exec" ,
	"route" , "mcast" , "vminfo" , "ps" ,
	"disklabel" , "pf" , "audio" , "dpath" ,
	"drm" , "vmm" , "chown"
    };

    /**
     * Additional promises to pledge.
     *
     * @see <a href="http://man.openbsd.org/pledge">pledge(2)</a>
     */
    private final static List<String> PROMISES = new Vector<String>();

    /**
     * Promises that are required by Java.
     *
     * To be automatically included in each pledge call.
     * @see #pledge(String,String[])
     */
    private final static String[] PERMANENT_PROMISES = {"stdio" , "cpath"};

    /**
     * Paths to be whitelisted.
     *
     * @see <a href="http://man.openbsd.org/pledge">pledge(2)</a>
     */
    private final static String[] PATHS = {};

    /**
     * Restrict the current process.
     *
     * @see <a href="http://man.openbsd.org/pledge">pledge(2)</a>
     */
    public final static void pledge()
    {
	String promises = generatePromises();
	pledge(promises , PATHS);
    }

    /**
     * Generate a list of promises based on promises requested and
     * permanent promises.
     */
    private final static String generatePromises()
    {
	String promises = "";

	for(String promise : PERMANENT_PROMISES)
	    promises = promises.concat(promise + " ");
	for(String promise : PROMISES)
	    promises = promises.concat(promise + " ");
	promises = promises.trim();

	return promises;
    }

    /**
     * Add promises to the set of promises.
     *
     * @param promises promises to be added.
     * @return boolean indicating wether or not all the promises were added.
     */
    public final static boolean addPromise(String... promises)
    {
	boolean added = true;

	if(promises != null)
	{
	    for(String promise : promises)
	    {
		if(promise != null)
		{
		    promise = promise.toLowerCase().trim();

		    String[] subpromises = promise.split("\\s");
		    for(String subpromise : subpromises)
		    {
			if(arrayContains(subpromise , PROMISE_NAMES)
			   && !arrayContains(subpromise , PERMANENT_PROMISES)
			   && !PROMISES.contains(subpromise))
			{
			    PROMISES.add(subpromise);
			    added &= true;
			}
		    }
		}
		else
		{
		    added = false;
		}
	    }
	}
	else
	{
	    added = false;
	}

	return added;
    }

    /**
     * Search for an Object in a given array.
     *
     * @param candidate object to find.
     * @param list array to find the candidate in.
     *
     * @return boolean indicating wether or not the candidate was found
     * in the list.
     */
    private final static boolean arrayContains(Object candidate ,
					       Object[] list)
    {
	boolean contains = false;

	if(candidate != null && list != null)
	{
	    for(Object value : list)
	    {
		if(value.equals(candidate))
		{
		    contains = true;
		    break;
		}
	    }
	}

	return contains;
    }

    /**
     * Remove promises from set of promises.
     *
     * @param promises to be removed.
     * @return boolean indicating wether or not all the promises were removed.
     */
    public final static boolean removePromise(String... promises)
    {
	boolean removed = true;

	if(promises != null)
	{
	    for(String promise : promises)
	    {
		if(promise != null)
		{
		    promise = promise.toLowerCase().trim();

		    String[] subpromises = promise.split("\\s");
		    for(String subpromise : subpromises)
			removed &= PROMISES.remove(subpromise);
		}
		else
		{
		    removed = false;
		}
	    }
	}
	else
	{
	    removed = false;
	}

	return removed;
    }

    /**
     * Return the current promises.
     * These are not necessarily pledged promises.
     *
     * @return array of promises.
     */
    public final static String[] currentPromises()
    {
	return generatePromises().split("\\s");
    }

    /**
     * Add paths to be whitelisted to the set of whitelisted paths.
     *
     * @param paths to be whitelisted/
     * @return boolean indicating wether or not all the paths were added.
     */
    public final static boolean addPath(String... paths)
    {
	return false;
    }

    /**
     * Remove whitelisted paths from the set of whitelisted paths.
     *
     * @param paths to remove from whitelisted set.
     * @return boolean indicating wether or not all the paths were removed.
     */
    public final static boolean removePath(String... paths)
    {
	return false;
    }
}
