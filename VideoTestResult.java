package com.ventuno.result;

import java.util.ArrayList;
import java.util.HashMap;

public class VideoTestResult {

	public HashMap<String, ArrayList<TestResult>> errors = new HashMap<String, ArrayList<TestResult>>();
	public HashMap<String, ArrayList<TestResult>> adCallbacks = new HashMap<String, ArrayList<TestResult>>();
	public HashMap<String, ArrayList<TestResult>> videoCallbacks = new HashMap<String, ArrayList<TestResult>>();
	public HashMap<String, ArrayList<TestResult>> beacons = new HashMap<String, ArrayList<TestResult>>();
	public HashMap<String, ArrayList<TestResult>> comscore = new HashMap<String, ArrayList<TestResult>>();
	public HashMap<String, ArrayList<TestResult>> videoEngagement = new HashMap<String, ArrayList<TestResult>>();
	public HashMap<String, ArrayList<TestResult>> resources = new HashMap<String, ArrayList<TestResult>>();
	public HashMap<String, ArrayList<TestResult>> adEngagement = new HashMap<String, ArrayList<TestResult>>();
	public HashMap<String, ArrayList<TestResult>> httpErrors = new HashMap<String, ArrayList<TestResult>>();

	public Boolean addError(String errorCode, String callBack, Integer duration) {

		TestResult result = new TestResult(callBack, duration);

		if (!errors.containsKey(errorCode)) {
			ArrayList<TestResult> errorList = new ArrayList<TestResult>();
			errors.put(errorCode, errorList);
		}
		errors.get(errorCode).add(result);
		return true;
	}

	public Boolean addVideoCallbacks(String callBackCode, String callBack,
			Integer duration) {

		TestResult result = new TestResult(callBack, duration);

		if (!videoCallbacks.containsKey(callBackCode)) {
			ArrayList<TestResult> errorList = new ArrayList<TestResult>();
			videoCallbacks.put(callBackCode, errorList);
		}
		videoCallbacks.get(callBackCode).add(result);
		return true;
	}

	public Boolean addADCallbacks(String callBackCode, String callBack,
			int duration) {
		// TODO Auto-generated method stub
		TestResult result = new TestResult(callBack, duration);

		if (!adCallbacks.containsKey(callBackCode)) {
			ArrayList<TestResult> errorList = new ArrayList<TestResult>();
			adCallbacks.put(callBackCode, errorList);
		}
		adCallbacks.get(callBackCode).add(result);
		return true;

	}

	public boolean addBeaconCallbacks(String callBackCode, String callBack,
			int duration) {
		TestResult result = new TestResult(callBack, duration);
		if (!beacons.containsKey(callBackCode)) {
			ArrayList<TestResult> errorList = new ArrayList<TestResult>();
			beacons.put(callBackCode, errorList);
		}
		beacons.get(callBackCode).add(result);
		return true;

	}

	public boolean addcomscoreCallbacks(String callBackCode, String callBack,
			int duration) {
		TestResult result = new TestResult(callBack, duration);
		if (!comscore.containsKey(callBackCode)) {
			ArrayList<TestResult> errorList = new ArrayList<TestResult>();
			comscore.put(callBackCode, errorList);
		}
		comscore.get(callBackCode).add(result);
		return true;
	}

	public boolean addvideoengagementCallbacks(String callBackCode,
			String callBack, int duration) {

		TestResult result = new TestResult(callBack, duration);
		if (!videoEngagement.containsKey(callBackCode)) {
			ArrayList<TestResult> errorList = new ArrayList<TestResult>();
			videoEngagement.put(callBackCode, errorList);
		}
		videoEngagement.get(callBackCode).add(result);
		return true;
	}

	public boolean addresourcescallbacks(String callBackCode, String callBack,
			int duration) {
		TestResult result = new TestResult(callBack, duration);
		if (!resources.containsKey(callBackCode)) {
			ArrayList<TestResult> errorList = new ArrayList<TestResult>();
			resources.put(callBackCode, errorList);
		}
		resources.get(callBackCode).add(result);
		return true;
	}

	public boolean addADengagementcallbacks(String callBackCode,
			String callBack, int duration) {
		TestResult result = new TestResult(callBack, duration);
		if (!adEngagement.containsKey(callBackCode)) {
			ArrayList<TestResult> errorList = new ArrayList<TestResult>();
			adEngagement.put(callBackCode, errorList);
		}
		adEngagement.get(callBackCode).add(result);
		return true;
	}

}
