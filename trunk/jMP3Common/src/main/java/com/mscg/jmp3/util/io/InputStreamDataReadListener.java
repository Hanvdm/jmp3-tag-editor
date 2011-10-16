/**
 *
 */
package com.mscg.jmp3.util.io;

/**
 * Classes implementing this interface can be used
 * with {@link PositionNotifierInputStream} in order
 * to be notified when data are read from the stream.
 *
 * @author Giuseppe Miscione
 *
 */
public interface InputStreamDataReadListener {

	/**
	 * This event is fired soon after data are read from the stream.
	 *
	 * @param actualPosition The actual position in the stream.
	 * @param totalSize The total size of the stream.
	 */
	public void onDataRead(long actualPosition, long totalSize);

	/**
	 * This event is fired when the reading reaches the
	 * stream end.
	 */
	public void onStreamEnd();

}
