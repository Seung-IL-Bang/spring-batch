/*
 * Copyright 2013 the original author or authors.
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
package org.springframework.batch.core.jsr.partition;

@SuppressWarnings("serial")
public class PartitionException extends RuntimeException {

	public PartitionException() {
		super();
	}

	public PartitionException(String message) {
		super(message);
	}

	public PartitionException(Throwable t) {
		super(t);
	}

	public PartitionException(String message, Throwable t) {
		super(message, t);
	}
}
