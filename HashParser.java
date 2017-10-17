package com.ventuno.playertest;

import java.io.FileReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ventuno.result.PlayerTestResult;
import com.ventuno.result.VideoTestResult;
import com.ventuno.util.VEN_CONSTANTS;

public class HashParser {

	private PlayerTestResult result;

	public void setTestResult(PlayerTestResult res) {
		this.result = res;
	}

	// to parse the har data and extract the http requests information
	public PlayerTestResult parseData(String videokey, int videoKeyPos,
			int iteration) {

		VideoTestResult videoResult = this.result.testResults[videoKeyPos][iteration];

		JsonParser parser = new JsonParser();
		try {
			Object obj = parser.parse(new FileReader(VEN_CONSTANTS.harFilePath
					+ videokey + "_" + iteration + ".txt"));
			JsonObject jsonObject = (JsonObject) obj;

			String addressCalled = "";
			int status = 200;
			int duration = 0;
			JsonArray entries = jsonObject.getAsJsonObject("log")
					.getAsJsonArray("entries");

			for (int i = 0; i < entries.size(); i++) {

				JsonObject newj = (JsonObject) entries.get(i);
				// Get the callback URL and Status of the HTTP request
				addressCalled = newj.getAsJsonObject("request").get("url")
						.toString();
				status = newj.getAsJsonObject("response").get("status")
						.getAsInt();

				duration = newj.get("time").getAsInt();

				this.getCallbackDetail(videoResult, addressCalled, status,
						duration);

				System.out.println(addressCalled);

			}

		} catch (Exception e) {
			System.out.println("Exception: @Parsing HAR file");
			e.printStackTrace();
		}

		return result;

	}

	private void getCallbackDetail(VideoTestResult videoResult,
			String addressCalled, int status, int duration) {

		if (status >= 200 && status <= 400) {
			getErrorCallbacks(videoResult, addressCalled, duration);
			getVideoCallbacks(videoResult, addressCalled, duration);
			getadCallbacks(videoResult, addressCalled, duration);
			getcomscorecallbacks(videoResult, addressCalled, duration);
			getbeaconcallbacks(videoResult, addressCalled, duration);
			getvenplayercallbacks(videoResult, addressCalled, duration);
			getjquerycallbacks(videoResult, addressCalled, duration);
			gethtml5Wrappercallbacks(videoResult, addressCalled, duration);
			getpcviewelementscallbacks(videoResult, addressCalled, duration);
			getloaderimagescallbacks(videoResult, addressCalled, duration);
			getplayer_requestcallbacks(videoResult, addressCalled, duration);
			getposterthumbcallbacks(videoResult, addressCalled, duration);
			getvideoplugincallbacks(videoResult, addressCalled, duration);
			getTitlePluginMincallbacks(videoResult, addressCalled, duration);
			getendscreenPluginMincallbacks(videoResult, addressCalled, duration);
			getmscontrolbarPluginMincallbacks(videoResult, addressCalled,
					duration);
			getmsiconmenuPluginMincallbacks(videoResult, addressCalled,
					duration);
			getUserInfocallbacks(videoResult, addressCalled, duration);
			getViewportcallbacks(videoResult, addressCalled, duration);
			getAutoplaycallbacks(videoResult, addressCalled, duration);
			getvideoListingWidgetviewcallbacks(videoResult, addressCalled,
					duration);
			gettitleTextmincallbacks(videoResult, addressCalled, duration);
			getmsadindicatormincallbacks(videoResult, addressCalled, duration);
			getmsadknowmoremincallbacks(videoResult, addressCalled, duration);
			getmsskipadmincallbacks(videoResult, addressCalled, duration);
			getpreviewThumbmincallbacks(videoResult, addressCalled, duration);
			getadCountDownmincallbacks(videoResult, addressCalled, duration);
			getPostrolladmodecallbacks(videoResult, addressCalled, duration);
			getSkipAdflowcallbacks(videoResult, addressCalled, duration);
			getPlayerMuteflowcallbacks(videoResult, addressCalled, duration);
			getPlayerUnMuteflowcallbacks(videoResult, addressCalled, duration);
			getPlayerPauseflowcallbacks(videoResult, addressCalled, duration);
			getPlayerResumeflowcallbacks(videoResult, addressCalled, duration);
			getPlayerfullscreenflowcallbacks(videoResult, addressCalled,
					duration);

		} else {
			// record an error in callback
		}

	}

	private void getErrorCallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		// Ad Error type - Vast Empty
		if (addressCalled.indexOf("type=error;status=playlist_error") != -1
				&& addressCalled.indexOf("ecode=303") != -1) {
			int ecode = addressCalled.indexOf("ecode=") + 6;
			String code = addressCalled.substring(ecode, ecode + 3);
			// result.aderrorstring = "ad request error(vast empty) " + code;
			videoResult.addError(code, addressCalled, duration);

		}

		// Ad Error type - Vast Timeout
		if (addressCalled.indexOf("type=error;status=playlist_error") != -1
				&& addressCalled.indexOf("ecode=301") != -1) {
			int ecode = addressCalled.indexOf("ecode=") + 6;
			String code = addressCalled.substring(ecode, ecode + 3);
			// result.aderrorstring = "ad request error(timeout) " + code;
			videoResult.addError(code, addressCalled, duration);
		}

		// Ad Error type - Parser Error
		if (addressCalled.indexOf("type=error;status=playlist_error") != -1
				&& addressCalled.indexOf("ecode=200") != -1) {

			int ecode = addressCalled.indexOf("ecode=") + 6;
			String code = addressCalled.substring(ecode, ecode + 3);
			// result.aderrorstring = "Ad Parser error(Invalid Ad type) " +
			// code;
			videoResult.addError(code, addressCalled, duration);
		}

		// Ad Error type - General VPAID Error
		if (addressCalled.indexOf("type=error;status=playlist_error") != -1
				&& addressCalled.indexOf("ecode=901") != -1) {
			int ecode = addressCalled.indexOf("ecode=") + 6;
			String code = addressCalled.substring(ecode, ecode + 3);
			// result.aderrorstring = "General Vpaid Error" + code;
			videoResult.addError(code, addressCalled, duration);
		}

		// Ad Error type - GoogleIMA Error
		if (addressCalled.indexOf("type=error;status=playlist_error") != -1
				&& addressCalled.indexOf("ecode=951") != -1) {
			int ecode = addressCalled.indexOf("ecode=") + 6;
			String code = addressCalled.substring(ecode, ecode + 3);
			// result.aderrorstring = "Ad Parser error(Invalid Ad type) " +
			// code;
			videoResult.addError(code, addressCalled, duration);
		}

	}

	private void getVideoCallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {

		// check for video status
		if (addressCalled.indexOf("type=videoprogress") != -1
				|| addressCalled.indexOf("type=videostart") != -1) {
			// Video Start call
			if (addressCalled.indexOf(";status=start") != -1) {
				videoResult.addVideoCallbacks("start", addressCalled, duration);
			}
			// Video First quartile call
			if (addressCalled.indexOf(";status=first") != -1) {
				videoResult.addVideoCallbacks("first", addressCalled, duration);
			}

			// Video Midoint call
			if (addressCalled.indexOf(";status=mid") != -1) {
				videoResult.addVideoCallbacks("mid", addressCalled, duration);
			}
			// Video Third Quartile call
			if (addressCalled.indexOf(";status=third") != -1) {
				videoResult.addVideoCallbacks("third", addressCalled, duration);
			}
			// Video Complete call
			if (addressCalled.indexOf(";status=complete") != -1) {
				videoResult.addVideoCallbacks("complete", addressCalled,
						duration);
			}
		}

	}

	public void getadCallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("type=adprogress") != -1) {

			// Ad First quartile call
			if (addressCalled.indexOf(";status=first") != -1) {

				videoResult.addADCallbacks("first", addressCalled, duration);

			}

			// Ad Midpoint call
			if (addressCalled.indexOf(";status=mid") != -1) {

				videoResult.addADCallbacks("mid", addressCalled, duration);

			}

			// Ad Thirdquartile call
			if (addressCalled.indexOf(";status=third") != -1) {

				// Best Thirdquartile load time
				videoResult.addADCallbacks("third", addressCalled, duration);

			}

			// Ad complete call
			if (addressCalled.indexOf(";status=complete") != -1) {

				videoResult.addADCallbacks("complete", addressCalled, duration);
			}
		}
	}

	public void getcomscorecallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("scorecardresearch.com") != -1) {
			// For Ad beacon
			if (addressCalled.indexOf("c5=09") != -1) {
				videoResult.addcomscoreCallbacks("c5=09", addressCalled,
						duration);
			}
			if (addressCalled.indexOf("c5=02") != -1) {
				videoResult.addcomscoreCallbacks("c5=02", addressCalled,
						duration);
			}

		}
	}

	public void getbeaconcallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("beacon.gif?type=script") != -1) {
			videoResult.addBeaconCallbacks("beacon_call", addressCalled,
					duration);
		}
	}

	public void getvenplayercallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("ventuno-webplayer.min") != -1) {
			videoResult.addvideoengagementCallbacks("venplayer", addressCalled,
					duration);
		}

	}

	public void getjquerycallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("jquery") != -1) {
			videoResult
					.addresourcescallbacks("jquery", addressCalled, duration);
		}

	}

	public void gethtml5Wrappercallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("vtnHtml5Wrapper") != -1) {
			videoResult.addresourcescallbacks("html5Wrapper", addressCalled,
					duration);
		}

	}

	public void getpcviewelementscallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("ventunoPCViewElements") != -1) {
			videoResult.addvideoengagementCallbacks("ventunoPCViewElements",
					addressCalled, duration);
		}

	}

	public void getloaderimagescallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("public/images/loader") != -1) {
			videoResult.addvideoengagementCallbacks("loaderimage",
					addressCalled, duration);
		}

	}

	public void getplayer_requestcallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("player_request") != -1) {
			videoResult.addvideoengagementCallbacks("player_request",
					addressCalled, duration);
		}

	}

	public void getposterthumbcallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("http://ventunovideos.edgesuite.net") != -1
				&& addressCalled.indexOf(".jpg") != -1) {
			videoResult.addvideoengagementCallbacks("poster_thumb",
					addressCalled, duration);
		}

	}

	public void getvideoplugincallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("relatedvideo-plugin") != -1
				&& addressCalled.indexOf(".jpg") != -1) {
			videoResult.addvideoengagementCallbacks("video_plugin",
					addressCalled, duration);
		}

	}

	public void getTitlePluginMincallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("title-plugin.min") != -1) {
			videoResult.addvideoengagementCallbacks("TitlePlugin",
					addressCalled, duration);
		}

	}

	public void getendscreenPluginMincallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("endscreen-plugin.min") != -1) {
			videoResult.addvideoengagementCallbacks("endscreenplugin",
					addressCalled, duration);
		}

	}

	public void getmscontrolbarPluginMincallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("ms-controlbar-plugin.min") != -1) {
			videoResult.addADengagementcallbacks("mscontrolbarplugin",
					addressCalled, duration);
		}

	}

	public void getmsiconmenuPluginMincallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("ms-iconmenu-plugin.min") != -1) {
			videoResult.addADengagementcallbacks("msiconmenuplugin",
					addressCalled, duration);
		}

	}

	public void getUserInfocallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("type=userinfo;") != -1
				&& addressCalled.indexOf("status=userinfo") != -1) {
			videoResult.addADengagementcallbacks("UserInfo", addressCalled,
					duration);
		}
	}

	public void getViewportcallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("type=Viewport") != -1) {
			videoResult.addADengagementcallbacks("Viewport", addressCalled,
					duration);
		}

	}

	public void getAutoplaycallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("type=videoengengagement") != -1
				&& addressCalled.indexOf("status=autoplay") != -1) {
			videoResult.addADengagementcallbacks("Autoplay", addressCalled,
					duration);
		}
	}

	public void getvideoListingWidgetviewcallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("videoListingWidget-view") != -1) {
			videoResult.addvideoengagementCallbacks("videoListingWidget",
					addressCalled, duration);
		}
	}

	public void gettitleTextmincallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("titleText.min") != -1) {
			videoResult.addADengagementcallbacks("titleText", addressCalled,
					duration);
		}
	}

	public void getmsadindicatormincallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("ms-ad-indicator.min") != -1) {
			videoResult.addADengagementcallbacks("msadindicatormin",
					addressCalled, duration);
		}
	}

	public void getmsadknowmoremincallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("ms-ad-knowmore.min") != -1) {
			videoResult.addADengagementcallbacks("msadknowmoremin",
					addressCalled, duration);
		}
	}

	public void getmsskipadmincallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("ms-skip-ad.min") != -1) {
			videoResult.addADengagementcallbacks("msskipadmin", addressCalled,
					duration);
		}
	}

	public void getpreviewThumbmincallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("previewThumb.min") != -1) {
			videoResult.addvideoengagementCallbacks("previewThumb", addressCalled,
					duration);
		}
	}

	public void getadCountDownmincallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("adCountDown.min") != -1) {
			videoResult.addADengagementcallbacks("adCountDown", addressCalled,
					duration);
		}
	}

	public void getPostrolladmodecallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("post_ad_status.php?") != -1
				&& addressCalled.indexOf("admode=postroll") != -1) {
			videoResult.addADengagementcallbacks("Postrolladmode",
					addressCalled, duration);
		}
	}

	public void getSkipAdflowcallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if (addressCalled.indexOf("type=adengagementothers") != -1
				&& addressCalled.indexOf("status=skip") != -1) {
			videoResult.addADengagementcallbacks("SkipAdflow", addressCalled,
					duration);
		}
	}

	public void getPlayerMuteflowcallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if ((addressCalled.indexOf("type=adengagement") != -1 && (addressCalled
				.indexOf("status=unmute") != -1))) {
			videoResult.addADengagementcallbacks("PlayerMuteflow",
					addressCalled, duration);
		}
		if (addressCalled.indexOf("type=videoengengagement") != -1
				&& (addressCalled.indexOf("status=unmute") != -1)) {
			videoResult.addvideoengagementCallbacks("PlayerMuteflow",
					addressCalled, duration);
		}
	}

	public void getPlayerUnMuteflowcallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if ((addressCalled.indexOf("type=adengagement") != -1 && (addressCalled
				.indexOf("status=unmute") != -1))) {
			videoResult.addADengagementcallbacks("PlayerUnMuteflow",
					addressCalled, duration);
		}
		if (addressCalled.indexOf("type=videoengengagement") != -1
				&& (addressCalled.indexOf("status=unmute") != -1)) {
			videoResult.addvideoengagementCallbacks("PlayerUnMuteflow",
					addressCalled, duration);
		}
	}

	public void getPlayerPauseflowcallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if ((addressCalled.indexOf("type=adengagement") != -1 && (addressCalled
				.indexOf("status=pause") != -1))) {
			videoResult.addADengagementcallbacks("pause", addressCalled,
					duration);
		}
		if (addressCalled.indexOf("type=videoengengagement") != -1
				&& (addressCalled.indexOf("status=pause") != -1)) {
			videoResult.addvideoengagementCallbacks("pause", addressCalled,
					duration);
		}
	}

	public void getPlayerResumeflowcallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if ((addressCalled.indexOf("type=adengagement") != -1 && (addressCalled
				.indexOf("status=resume") != -1))) {
			videoResult.addADengagementcallbacks("PlayerResumeflow",
					addressCalled, duration);
		}
		if (addressCalled.indexOf("type=videoengengagement") != -1
				&& (addressCalled.indexOf("status=resume") != -1)) {
			videoResult.addvideoengagementCallbacks("PlayerResumeflow",
					addressCalled, duration);
		}
	}

	public void getPlayerfullscreenflowcallbacks(VideoTestResult videoResult,
			String addressCalled, int duration) {
		if ((addressCalled.indexOf("type=adengagement") != -1 && (addressCalled
				.indexOf("status=fullscreen") != -1))) {
			videoResult.addADengagementcallbacks("Playerfullscreenflow",
					addressCalled, duration);
		}
		if (addressCalled.indexOf("type=videoengengagement") != -1
				&& (addressCalled.indexOf("status=fullscreen") != -1)) {
			videoResult.addvideoengagementCallbacks("Playerfullscreenflow",
					addressCalled, duration);
		}
	}
}
