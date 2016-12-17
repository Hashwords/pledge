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

/**
 * Example of pledge(2) in Java.
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
     * Promises to pledge.
     *
     * @see <a href="http://man.openbsd.org/pledge">pledge(2)</a>
     */
    private final static String promises = "stdio cpath";

    /**
     * Paths to be whitelisted.
     *
     * @see <a href="http://man.openbsd.org/pledge">pledge(2)</a>
     */
    private final static String[] paths = {};

    public static void main(String[] args)
    {
	System.out.println("Hello, World!");
	pledge(promises,paths);
	System.out.println("Goodbye, cruel world!");
    }
}
