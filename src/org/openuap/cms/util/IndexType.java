/*
 * Copyright 2005-2008 the original author or authors.
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
package org.openuap.cms.util;

import java.io.Serializable;


/**
 * <p>
 * 索引类型.
 * </p>
 * 
 * 
 * <p>
 * Company:
 * </p>
 * $Id: IndexType.java 3917 2010-10-26 11:39:48Z orangeforjava $
 * @author Weiping Ju
 * @version 1.0
 */
public interface IndexType extends Serializable {

	public static final Index[] ALL_INDEX_TYPE = new Index[] { Index.NO,
			Index.TOKENIZED, Index.UN_TOKENIZED };

	public static final Store[] ALL_STORE_TYPE = new Store[] { Store.NO,
			Store.YES, Store.COMPRESS };
	
	public static final TermVector[] ALL_TREMVECTOR_TYPE = new TermVector[] {
			TermVector.NO, TermVector.YES, TermVector.WITH_OFFSETS,
			TermVector.WITH_POSITIONS, TermVector.WITH_POSITIONS_OFFSETS };

	/**
	 * Specifies whether and how a meta-data property will be stored.
	 */
	public static final class Store extends Parameter {

		/**
		 * 
		 */
		private static final long serialVersionUID = 250580475691251426L;

		private Store(String name) {
			super(name);
		}

		/**
		 * Do not store the property value in the index.
		 */
		public static final Store NO = new Store("NO");

		/**
		 * Store the original property value in the index. This is useful for
		 * short texts like a document's title which should be displayed with
		 * the results. The value is stored in its original form, i.e. no
		 * analyzer is used before it is stored.
		 */
		public static final Store YES = new Store("YES");

		/**
		 * Store the original property value in the index in a compressed form.
		 * This is useful for long documents and for binary valued fields.
		 */
		public static final Store COMPRESS = new Store("COMPRESS");

		public static String toString(IndexType.Store propertyStore) {
			if (propertyStore == IndexType.Store.NO) {
				return "NO";
			} else if (propertyStore == IndexType.Store.YES) {
				return "YES";
			} else if (propertyStore == IndexType.Store.COMPRESS) {
				return "COMPRESS";
			}
			throw new IllegalArgumentException(
					"Can't find property store for [" + propertyStore + "]");
		}

		public static IndexType.Store fromString(String propertyStore) {
			if ("no".equalsIgnoreCase(propertyStore)) {
				return IndexType.Store.NO;
			} else if ("yes".equalsIgnoreCase(propertyStore)) {
				return IndexType.Store.YES;
			} else if ("compress".equalsIgnoreCase(propertyStore)) {
				return IndexType.Store.COMPRESS;
			}
			throw new IllegalArgumentException(
					"Can't find property store for [" + propertyStore + "]");
		}
	}

	/**
	 * Specifies whether and how a meta-data property should be indexed.
	 */
	public static final class Index extends Parameter {

		private static final long serialVersionUID = 3761973756863985718L;

		private Index(String name) {
			super(name);
		}

		/**
		 * Do not index the property value. This property can thus not be
		 * searched, but one can still access its contents provided it is
		 * {@link Property.Store stored}.
		 */
		public static final Index NO = new Index("NO");

		/**
		 * Index the property's value so it can be searched. An Analyzer will be
		 * used to tokenize and possibly further normalize the text before its
		 * terms will be stored in the index. This is useful for common text.
		 */
		public static final Index TOKENIZED = new Index("TOKENIZED");

		/**
		 * Index the property's value without using an Analyzer, so it can be
		 * searched. As no analyzer is used the value will be stored as a single
		 * term. This is useful for unique Ids like product numbers.
		 */
		public static final Index UN_TOKENIZED = new Index("UN_TOKENIZED");

		public static String toString(IndexType.Index propertyIndex) {
			if (propertyIndex == IndexType.Index.NO) {
				return "NO";
			} else if (propertyIndex == IndexType.Index.TOKENIZED) {
				return "TOKENIZED";
			} else if (propertyIndex == IndexType.Index.UN_TOKENIZED) {
				return "UN_TOKENIZED";
			}
			throw new IllegalArgumentException(
					"Can't find property index for [" + propertyIndex + "]");
		}

		public static IndexType.Index fromString(String propertyIndex) {
			if ("no".equalsIgnoreCase(propertyIndex)) {
				return IndexType.Index.NO;
			} else if ("tokenized".equalsIgnoreCase(propertyIndex)) {
				return IndexType.Index.TOKENIZED;
			} else if ("un_tokenized".equalsIgnoreCase(propertyIndex)) {
				return IndexType.Index.UN_TOKENIZED;
			}
			throw new IllegalArgumentException(
					"Can't find property index for [" + propertyIndex + "]");
		}
	}

	/**
	 * Specifies whether and how a meta-data property should have term vectors.
	 */
	public static final class TermVector extends Parameter {

		private static final long serialVersionUID = 3256728372590948921L;

		private TermVector(String name) {
			super(name);
		}

		/**
		 * Do not store term vectors.
		 */
		public static final TermVector NO = new TermVector("NO");

		/**
		 * Store the term vectors of each document. A term vector is a list of
		 * the document's terms and their number of occurences in that document.
		 */
		public static final TermVector YES = new TermVector("YES");

		/**
		 * Store the term vector + token position information
		 * 
		 * @see #YES
		 */
		public static final TermVector WITH_POSITIONS = new TermVector(
				"WITH_POSITIONS");

		/**
		 * Store the term vector + Token offset information
		 * 
		 * @see #YES
		 */
		public static final TermVector WITH_OFFSETS = new TermVector(
				"WITH_OFFSETS");

		/**
		 * Store the term vector + Token position and offset information
		 * 
		 * @see #YES
		 * @see #WITH_POSITIONS
		 * @see #WITH_OFFSETS
		 */
		public static final TermVector WITH_POSITIONS_OFFSETS = new TermVector(
				"WITH_POSITIONS_OFFSETS");

		public static String toString(IndexType.TermVector propertyTermVector) {
			if (propertyTermVector == IndexType.TermVector.NO) {
				return "NO";
			} else if (propertyTermVector == IndexType.TermVector.YES) {
				return "YES";
			} else if (propertyTermVector == IndexType.TermVector.WITH_POSITIONS) {
				return "WITH_POSITIONS";
			} else if (propertyTermVector == IndexType.TermVector.WITH_OFFSETS) {
				return "WITH_OFFSETS";
			} else if (propertyTermVector == IndexType.TermVector.WITH_POSITIONS_OFFSETS) {
				return "WITH_POSITIONS_OFFSETS";
			}
			throw new IllegalArgumentException(
					"Can't find property term vector for ["
							+ propertyTermVector + "]");
		}

		public static IndexType.TermVector fromString(String propertyTermVector) {
			if ("no".equalsIgnoreCase(propertyTermVector)) {
				return IndexType.TermVector.NO;
			} else if ("yes".equalsIgnoreCase(propertyTermVector)) {
				return IndexType.TermVector.YES;
			} else if ("positions".equalsIgnoreCase(propertyTermVector)) {
				return IndexType.TermVector.WITH_POSITIONS;
			} else if ("offsets".equalsIgnoreCase(propertyTermVector)) {
				return IndexType.TermVector.WITH_OFFSETS;
			} else if ("positions_offsets".equalsIgnoreCase(propertyTermVector)) {
				return IndexType.TermVector.WITH_POSITIONS_OFFSETS;
			}
			throw new IllegalArgumentException(
					"Can't find property term vector for ["
							+ propertyTermVector + "]");
		}
	}
}
