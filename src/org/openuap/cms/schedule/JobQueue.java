/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openuap.cms.schedule;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

/**
 * <p>
 * Title: JobQueue
 * </p>
 * 
 * <p>
 * Description:任务队列.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company:<a href="http://www.openuap.org">http://www.openuap.org</a>
 * </p>
 * $Id: JobQueue.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * @author Weiping Ju
 * @version 1.0
 */
public class JobQueue {
	
	private Vector queue;

	public JobEntry getNext() {
		if (queue.size() > 0) {
			return (JobEntry) queue.elementAt(0);
		} else {
			return null;
		}
	}

	public JobEntry getJob(JobEntry je) {
		int index = -1;
		if (je != null) {
			index = queue.indexOf(je);
		}
		if (index < 0) {
			return null;
		} else {
			return (JobEntry) queue.elementAt(index);
		}
	}

	public Vector list() {
		if (queue != null && queue.size() > 0) {
			return (Vector) queue.clone();
		} else {
			return null;
		}
	}

	public synchronized void add(JobEntry je) {
		queue.addElement(je);
		sortQueue();
	}
	
	public synchronized void batchLoad(List jobEntries) {
		if (jobEntries != null) {
			queue.addAll(jobEntries);
			sortQueue();
		}
	}

	public synchronized void remove(JobEntry je) {
		queue.removeElement(je);
		sortQueue();
	}

	public synchronized void modify(JobEntry je) {
		sortQueue();
	}

	public synchronized void updateQueue(JobEntry je) throws Exception {
		je.calcRunTime();
		sortQueue();
	}

	private void sortQueue() {
		// Comparator aComparator = new _cls1();
		Collections.sort(queue, new Comparator() {
			public int compare(Object o1, Object o2) {
				Long time1 = new Long(((JobEntry) o1).getNextRuntime());
				Long time2 = new Long(((JobEntry) o2).getNextRuntime());
				return time1.compareTo(time2);

			}

			public boolean equals(Object obj) {
				return true;
			}
		});
	}

	public JobQueue() throws Exception {
		queue = null;
		queue = new Vector(10);
	}

	// private class _cls1
	// implements Comparator {
	//
	// public int compare(Object o1, Object o2) {
	// Long time1 = new Long( ( (JobEntry) o1).getNextRuntime());
	// Long time2 = new Long( ( (JobEntry) o2).getNextRuntime());
	// return time1.compareTo(time2);
	// }
	//
	// private final void constructor$0(JobQueue jobqueue) {
	// }
	//
	// _cls1() {
	// constructor$0(JobQueue.this);
	// }
	// }

}
