/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.api.common;

import java.util.Map;

/**
 * The result of a job execution. Gives access to the execution time of the job,
 * and to all accumulators created by this job.
 */
public class JobExecutionResult {

	private long netRuntime;
	private Map<String, Object> accumulatorResults;

	/**
	 * Creates a new JobExecutionResult.
	 *
	 * @param netRuntime The net runtime of the job (excluding pre-flight phase like the optimizer)
	 * @param accumulators A map of all accumulators produced by the job.
	 */
	public JobExecutionResult(long netRuntime, Map<String, Object> accumulators) {
		this.netRuntime = netRuntime;
		this.accumulatorResults = accumulators;
	}

	/**
	 * Gets the net execution time of the job, i.e., the execution time in the parallel system,
	 * without the pre-flight steps like the optimizer.
	 *
	 * @return The net execution time.
	 */
	public long getNetRuntime() {
		return this.netRuntime;
	}

	/**
	 * Gets the accumulator with the given name. Returns {@code null}, if no accumulator with
	 * that name was produced.
	 *
	 * @param accumulatorName The name of the accumulator.
	 * @param <T> The generic type of the accumulator value.
	 * @return The value of the accumulator with the given name.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAccumulatorResult(String accumulatorName) {
		return (T) this.accumulatorResults.get(accumulatorName);
	}

	/**
	 * Gets all accumulators produced by the job. The map contains the accumulators as
	 * mappings from the accumulator name to the accumulator value.
	 *
	 * @return A map containing all accumulators produced by the job.
	 */
	public Map<String, Object> getAllAccumulatorResults() {
		return this.accumulatorResults;
	}
	
	/**
	 * Gets the accumulator with the given name as an integer.
	 *
	 * @param accumulatorName Name of the counter
	 * @return Result of the counter, or null if the counter does not exist
	 * @throws java.lang.ClassCastException Thrown, if the accumulator was not aggregating a {@link java.lang.Integer}
	 */
	public Integer getIntCounterResult(String accumulatorName) {
		Object result = this.accumulatorResults.get(accumulatorName);
		if (result == null) {
			return null;
		}
		if (!(result instanceof Integer)) {
			throw new ClassCastException("Requested result of the accumulator '" + accumulatorName
							+ "' should be Integer but has type " + result.getClass());
		}
		return (Integer) result;
	}

	// TODO Create convenience methods for the other shipped accumulator types
}
