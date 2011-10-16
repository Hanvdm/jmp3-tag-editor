/**
 *
 */
package com.mscg.jmp3.util.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A <code>PositionNotifierInputStream</code>
 * is a {@link FilterInputStream} that notifies a listener
 * every time datas are read from the input stream, passing
 * to the listener the actual percentage of data read.
 * This stream needs to know the complete size of the stream
 * in order to calculate the percentage.
 *
 * @author Giuseppe Miscione
 *
 */
public class PositionNotifierInputStream extends FilterInputStream {

	public static final int DEFAULT_READ_THRESHOLD = 1024;

	protected long totalSize;
	protected int increaseThreshold;
	protected long actualPosition;
	protected long lastPosition;
	protected List<InputStreamDataReadListener> listeners;

	private boolean reading;

	/**
	 * Wrap the provided {@link InputStream} in order to notify
	 * listeners when data are read from it. This constructor
	 * initializes the stream with the default threshold value.
	 *
	 * @param in The original {@link InputStream}
	 * @param totalSize The total size of the stream.
	 */
	public PositionNotifierInputStream(InputStream in, long totalSize) {
		super(in);
		init(totalSize, DEFAULT_READ_THRESHOLD, null);
	}

	/**
	 * Wrap the provided {@link InputStream} in order to notify
	 * listeners when data are read from it. This constructor
	 * initializes the stream with the default threshold value.
	 *
	 * @param in The original {@link InputStream}
	 * @param totalSize The total size of the stream.
	 * @param listener A listener that will be associated to the stream.
	 */
	public PositionNotifierInputStream(InputStream in, long totalSize, InputStreamDataReadListener listener) {
		super(in);
		init(totalSize, DEFAULT_READ_THRESHOLD, listener);
	}

	/**
	 * Wrap the provided {@link InputStream} in order to notify
	 * listeners when data are read from it.
	 *
	 * @param in The original {@link InputStream}
	 * @param totalSize The total size of the stream.
	 * @param increaseThreshold The minimum size of data read before events are propagated to listeners.
	 */
	public PositionNotifierInputStream(InputStream in, long totalSize, int increaseThreshold) {
		super(in);
		init(totalSize, increaseThreshold, null);
	}

	/**
	 * Wrap the provided {@link InputStream} in order to notify
	 * listeners when data are read from it.
	 *
	 * @param in The original {@link InputStream}
	 * @param totalSize The total size of the stream.
	 * @param increaseThreshold The minimum size of data read before events are propagated to listeners.
	 * @param listener A listener that will be associated to the stream.
	 */
	public PositionNotifierInputStream(InputStream in, long totalSize, int increaseThreshold, InputStreamDataReadListener listener) {
		super(in);
		init(totalSize, increaseThreshold, listener);
	}

	/**
	 * Adds a {@link InputStreamDataReadListener} to the listeners
	 * list.
	 *
	 * @param listener The listener to add to the list.
	 */
	public void addListener(InputStreamDataReadListener listener) {
		if(listener != null)
			listeners.add(listener);
	}

	/**
	 * Returns the minimum size of data that must be read before events are propagated to listeners.
	 * @return The minimum size of data that must be read before events are propagated to listeners.
	 */
	public int getIncreaseThreshold() {
		return increaseThreshold;
	}

	/**
	 * Returns a {@List} with all the listeners associated with
	 * this stream.
	 *
	 * @return A {@List} with all the listeners.
	 */
	public List<InputStreamDataReadListener> getListeners() {
		return listeners;
	}

	/**
	 * Returns the total size of the input stream.
	 *
	 * @return The total size of the input stream.
	 */
	public long getTotalSize() {
		return totalSize;
	}

	/**
	 * Internal init method called by constructors.
	 *
	 * @param totalSize The total size of the stream.
	 * @param increaseThreshold The minimum size of data read before events are propagated to listeners.
	 * @param listener A listener that will be associated to the stream.
	 */
	protected void init(long totalSize, int increaseThreshold, InputStreamDataReadListener listener) {
		this.totalSize = totalSize;
		this.actualPosition = 0L;
		this.lastPosition = 0L;
		this.increaseThreshold = increaseThreshold;
		listeners = new LinkedList<InputStreamDataReadListener>();
		addListener(listener);
	}

	protected void notifyListeners(int delta) {
		if(delta > 0) {
			actualPosition += delta;
			if(actualPosition - lastPosition > increaseThreshold) {
				lastPosition = actualPosition;
				for(Iterator<InputStreamDataReadListener> it = listeners.iterator(); it.hasNext();) {
					try {
						InputStreamDataReadListener l = it.next();
						l.onDataRead(actualPosition, totalSize);
					} catch(Exception e){}
				}
			}
		}
		else {
			for(Iterator<InputStreamDataReadListener> it = listeners.iterator(); it.hasNext();) {
				try {
					InputStreamDataReadListener l = it.next();
					l.onStreamEnd();
				} catch(Exception e){}
			}
		}
	}

	@Override
	public int read() throws IOException {
		int ret = super.read();
		if(!reading) {
			notifyListeners(ret >= 0 ? 1 : -1);
		}
		return ret;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		reading = true;
		int ret = super.read(b, off, len);
		reading = false;
		notifyListeners(ret);
		return ret;
	}

}
