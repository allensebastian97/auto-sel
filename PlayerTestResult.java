package com.ventuno.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import com.thoughtworks.selenium.webdriven.commands.GetValue;

public class PlayerTestResult {

	public ArrayList<String> videoKeys;
	public Integer iterations = 0;
	public VideoTestResult[][] testResults;

	public ArrayList<HashMap<String, CallDuration>> callDurations = new ArrayList<>();

	// getting the keysize and no.of iterations
	public PlayerTestResult(Integer iterations, ArrayList<String> keys) {
		videoKeys = keys;
		this.iterations = iterations;
		testResults = new VideoTestResult[keys.size()][iterations];
		for (int i = 0; i < videoKeys.size(); i++) {
			for (int j = 0; j < iterations; j++) {
				testResults[i][j] = new VideoTestResult();
			}
		}
	}

	public Boolean aggregateResults() {

		for (int i = 0; i < videoKeys.size(); i++) {
			HashMap<String, CallDuration> curCallDurations = new HashMap<>();
			
			// int count = 0;
			// int maxCallDuration = 0;
			// int minCallDuration = 99999999;
			// int totalCallDuration = 0;
			// int sum = 0;
			// int totalCalls = 0;
			// int totalCalls1 = 0;
			// int avgCallDuration1 = 0;
			// int avgCallDuration = 0;

			CallDuration duration = new CallDuration();
			CallDuration duration1 = new CallDuration();
			CallDuration duration2 = new CallDuration();
			CallDuration duration3 = new CallDuration();
			CallDuration duration4 = new CallDuration();
			CallDuration duration5 = new CallDuration();
			CallDuration duration6 = new CallDuration();
			CallDuration duration7 = new CallDuration();

			for (int j = 0; j < iterations; j++) {
				/*
				 * Group the video test results under the following categories
				 * 
				 * adcallbacks videocallbacks comscore beacons player_request
				 */
				VideoTestResult curResult = testResults[i][j];

				// Get adcallback data for processing

				for (Map.Entry<String, ArrayList<TestResult>> entry : curResult.adCallbacks
						.entrySet()) {

					for (TestResult result : entry.getValue()) {
						duration.totalCalls++;
						duration.totalCallDuration += result.duration;
						if (duration.maxCallDuration > result.duration) {
							duration.maxCallDuration = result.duration;
						}
						if (duration.minCallDuration < result.duration) {
							duration.minCallDuration = result.duration;
						}
					}
				}
				for (Map.Entry<String, ArrayList<TestResult>> entry : curResult.videoCallbacks
						.entrySet()) {

					for (TestResult result : entry.getValue()) {
						duration1.totalCalls++;
						duration1.totalCallDuration += result.duration;
						if (duration1.maxCallDuration > result.duration) {
							duration1.maxCallDuration = result.duration;
						} 
						if (duration1.minCallDuration < result.duration) {
							duration1.minCallDuration = result.duration;
						}
					}
				}
				for (Map.Entry<String, ArrayList<TestResult>> entry : curResult.beacons
						.entrySet()) {

					for (TestResult result : entry.getValue()) {
						duration2.totalCalls++;
						duration2.totalCallDuration += result.duration;
						if (duration2.maxCallDuration > result.duration) {
							duration2.maxCallDuration = result.duration;
						}
						if (duration2.minCallDuration < result.duration) {
							duration2.minCallDuration = result.duration;
						}
					}
				}

				for (Map.Entry<String, ArrayList<TestResult>> entry1 : curResult.comscore
						.entrySet()) {

					for (TestResult result : entry1.getValue()) {
						duration3.totalCalls++;
						duration3.totalCallDuration += result.duration;
						if (duration3.maxCallDuration > result.duration) {
							duration3.maxCallDuration = result.duration;
						}
						if (duration3.minCallDuration < result.duration) {
							duration3.minCallDuration = result.duration;
						}
					}

				}

				for (Map.Entry<String, ArrayList<TestResult>> entry1 : curResult.videoEngagement
						.entrySet()) {

					for (TestResult result : entry1.getValue()) {
						duration4.totalCalls++;
						duration4.totalCallDuration += result.duration;

						if (duration4.maxCallDuration > result.duration) {
							duration4.maxCallDuration = result.duration;
						}
						if (duration4.minCallDuration < result.duration) {
							duration4.minCallDuration = result.duration;
						}
					}

				}
				for (Map.Entry<String, ArrayList<TestResult>> entry1 : curResult.resources
						.entrySet()) {

					for (TestResult result : entry1.getValue()) {
						duration5.totalCalls++;
						duration5.totalCallDuration += result.duration;
						if (duration5.maxCallDuration > result.duration) {
							duration5.maxCallDuration = result.duration;
						}
						if (duration5.minCallDuration < result.duration) {
							duration5.minCallDuration = result.duration;
						}
					}

				}
				for (Map.Entry<String, ArrayList<TestResult>> entry11 : curResult.adEngagement
						.entrySet()) {

					for (TestResult result : entry11.getValue()) {
						duration6.totalCalls++;
						duration6.totalCallDuration += result.duration;
						if (duration6.maxCallDuration > result.duration) {
							duration6.maxCallDuration = result.duration;
						}
						if (duration6.minCallDuration < result.duration) {
							duration6.minCallDuration = result.duration;
						}
					}

				}
				for (Map.Entry<String, ArrayList<TestResult>> entry11 : curResult.errors
						.entrySet()) {

					for (TestResult result : entry11.getValue()) {
						duration7.totalCalls++;
						duration7.totalCallDuration += result.duration;
						if (duration7.maxCallDuration > result.duration) {
							duration7.maxCallDuration = result.duration;
						}
						if (duration7.minCallDuration < result.duration) {
							duration7.minCallDuration = result.duration;
						}
					}

				}

			}
			

			if (duration.totalCalls > 0)
				duration.avgCallDuration = duration.totalCallDuration
						/ duration.totalCalls;
			if (duration1.totalCalls > 0)
				duration1.avgCallDuration = duration1.totalCallDuration
						/ duration1.totalCalls;
			if (duration2.totalCalls > 0)
				duration2.avgCallDuration = duration2.totalCallDuration
						/ duration2.totalCalls;
			if (duration3.totalCalls > 0)
				duration3.avgCallDuration = duration3.totalCallDuration
						/ duration3.totalCalls;
			if (duration4.totalCalls > 0)
				duration4.avgCallDuration = duration4.totalCallDuration
						/ duration4.totalCalls;
			if (duration5.totalCalls > 0)
				duration5.avgCallDuration = duration5.totalCallDuration
						/ duration5.totalCalls;
			if (duration6.totalCalls > 0)
				duration6.avgCallDuration = duration6.totalCallDuration
						/ duration6.totalCalls;
			if (duration7.totalCalls > 0)
				duration7.avgCallDuration = duration7.totalCallDuration
						/ duration7.totalCalls;
			
			
			curCallDurations.put("adCallbacks", duration);
			curCallDurations.put("videoCallbacks", duration1);
			curCallDurations.put("beacons", duration2);
			curCallDurations.put("comscore", duration3);
			curCallDurations.put("videoEngagement", duration4);
			curCallDurations.put("resources", duration5);
			curCallDurations.put("adEngagement", duration6);
			curCallDurations.put("errors", duration7);

			callDurations.add(curCallDurations);

			// avgCallDuration1 = duration.totalCallDuration
			// + duration1.totalCallDuration + duration2.totalCallDuration
			// + duration3.totalCallDuration + duration4.totalCallDuration
			// + duration5.totalCallDuration + duration6.totalCallDuration
			// + duration7.totalCallDuration;
			//
			// totalCalls1 = duration.totalCalls + duration1.totalCalls
			// + duration2.totalCalls + duration3.totalCalls
			// + duration4.totalCalls + duration5.totalCalls
			// + duration6.totalCalls + duration7.totalCalls;
			//
			// duration8.avgCallDuration = avgCallDuration1 / totalCalls1;

		}

		return false;

	}

}