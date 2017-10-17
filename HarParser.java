package com.ventuno.playertest;

import java.io.FileReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ventuno.util.VEN_CONSTANTS;

public class HarParser {

	private TestResult result;

	public void setTestResult(TestResult res) {
		this.result = res;
	}

	// to parse the har data and extract the http requests information
	public TestResult parseData(String videokey, int n) {

		JsonParser parser = new JsonParser();
		try {
			Object obj = parser.parse(new FileReader(VEN_CONSTANTS.harFilePath
					+ videokey + "_" + n + ".txt"));
			JsonObject jsonObject = (JsonObject) obj;

			String addressCalled = "";
			JsonArray entries = jsonObject.getAsJsonObject("log")
					.getAsJsonArray("entries");

			result.keypassed = "Test " + n + "\n";

			for (int i = 0; i < entries.size(); i++) {
				JsonObject newj = (JsonObject) entries.get(i);
				addressCalled = newj.getAsJsonObject("request").get("url")
						.toString();

				int status = newj.getAsJsonObject("response").get("status")
						.getAsInt();

				getCallbackDetail(addressCalled, status, newj, n);

				System.out.println(addressCalled);
			}

		} catch (Exception e) {
			System.out.println("Exception: @Parsing HAR file");
			e.printStackTrace();
		}

		return result;
	}

	private void getCallbackDetail(String addressCalled, int status,
			JsonObject newj, int n) {

		// Ad Error type - Vast Empty
		if (addressCalled.indexOf("type=error;status=playlist_error") != -1
				&& addressCalled.indexOf("ecode=303") != -1) {
			int ecode = addressCalled.indexOf("ecode=") + 6;
			String code = addressCalled.substring(ecode, ecode + 3);
			result.aderrorstring = "ad request error(vast empty) " + code;
			++result.ae;
		}
		// Ad Error type - Vast Timeout
		if (addressCalled.indexOf("type=error;status=playlist_error") != -1
				&& addressCalled.indexOf("ecode=301") != -1) {
			int ecode = addressCalled.indexOf("ecode=") + 6;
			String code = addressCalled.substring(ecode, ecode + 3);
			result.aderrorstring = "ad request error(timeout) " + code;
			++result.aeto;
		}

		// Ad Error type - Parser Error
		if (addressCalled.indexOf("type=error;status=playlist_error") != -1
				&& addressCalled.indexOf("ecode=200") != -1) {

			int ecode = addressCalled.indexOf("ecode=") + 6;
			String code = addressCalled.substring(ecode, ecode + 3);
			result.aderrorstring = "Ad Parser error(Invalid Ad type) " + code;
			++result.ape;
		}

		// Ad Error type - General VPAID Error
		if (addressCalled.indexOf("type=error;status=playlist_error") != -1
				&& addressCalled.indexOf("ecode=901") != -1) {
			int ecode = addressCalled.indexOf("ecode=") + 6;
			String code = addressCalled.substring(ecode, ecode + 3);
			result.aderrorstring = "General Vpaid Error" + code;
			++result.vae;
		}

		// Ad Error type - GoogleIMA Error
		if (addressCalled.indexOf("type=error;status=playlist_error") != -1
				&& addressCalled.indexOf("ecode=951") != -1) {
			int ecode = addressCalled.indexOf("ecode=") + 6;
			String code = addressCalled.substring(ecode, ecode + 3);
			result.aderrorstring = "Ad Parser error(Invalid Ad type) " + code;
			++result.aeima;
		}
		// Ad Request
		if (addressCalled.indexOf("type=ad;status=ad_request") != -1) {

			// check for best Ad Request load time
			if (result.arc < result.best_arc) {
				result.best_arc = result.arc;
			}

			// check for worst smart player load time
			if (result.arc > result.worst_arc) {
				result.worst_arc = result.arc;
			}

			if ((status >= 200 && status < 400)) {
				result.keypassed += addressCalled + "\n";
				++result.ar;
			}

			else {
				result.keyfailed += addressCalled + "\n";
			}

		}

		// Ad start
		if (addressCalled.indexOf("type=adstart") != -1) {
			if (addressCalled.indexOf("&status=start") != -1) {

				// best Ad request load time
				if (result.asc < result.best_asc) {
					result.best_asc = result.asc;
				}

				// worst Ad request load time
				if (result.asc > result.worst_asc) {
					result.worst_asc = result.asc;
				}

				if ((status >= 200 && status < 400)) {
					result.keypassed += addressCalled + "\n";
					++result.as;
				}

				else {
					result.keyfailed += addressCalled + "\n";
				}
			}
		}

		// Comscore calls
		if (addressCalled.indexOf("scorecardresearch.com") != -1) {
			// For Ad beacon
			if (addressCalled.indexOf("c5=09") != -1) {

				if ((status >= 200 && status < 400)) {
					result.keypassed += addressCalled + "\n";
					++result.comscoreAd;
				} else {
					result.keyfailed += addressCalled + "\n";
				}
			} else if (addressCalled.indexOf("c5=02") != -1) {

				if ((status >= 200 && status < 400)) {

					result.keypassed += addressCalled + "\n";
					++result.comscoreVideo;
				} else {
					result.keyfailed += addressCalled + "\n";
				}
			}
		}

		// check for ad status
		if (addressCalled.indexOf("type=adprogress") != -1) {

			// Ad Firstquartile call
			if (addressCalled.indexOf(";status=first") != -1) {

				// best Firstquartile call load time
				if (result.afqc < result.best_afqc) {
					result.best_afqc = result.afqc;
				}
				// Worst Firstquartile call load time
				if (result.afqc > result.worst_afqc) {
					result.worst_afqc = result.afqc;
				}

				if ((status >= 200 && status < 400)) {
					result.keypassed += addressCalled + "\n";
					++result.afq;
				}

				else {
					result.keyfailed += addressCalled + "\n";
				}

			}

			// Ad Midpoint call
			if (addressCalled.indexOf(";status=mid") != -1) {

				// Best Midpoint load time
				if (result.ampc < result.best_ampc) {
					result.best_ampc = result.ampc;
				}

				// Worst Midpoint load time
				if (result.ampc > result.worst_ampc) {
					result.worst_ampc = result.ampc;
				}

				if ((status >= 200 && status < 400)) {
					result.keypassed += addressCalled + "\n";
					++result.am;
				}

				else {
					result.keyfailed += addressCalled + "\n";
				}
			}

			// Ad Thirdquartile call
			if (addressCalled.indexOf(";status=third") != -1) {

				// Best Thirdquartile load time
				if (result.atqc < result.best_atqc) {
					result.best_atqc = result.atqc;
				}

				// Worst Thirdquartile load time
				if (result.atqc > result.worst_atqc) {
					result.worst_atqc = result.atqc;
				}

				if ((status >= 200 && status < 400)) {
					result.keypassed += addressCalled + "\n";
					++result.atq;
				}

				else {
					result.keyfailed += addressCalled + "\n";
				}
			}

			// Ad complete call
			if (addressCalled.indexOf(";status=complete") != -1) {

				// Best Ad complete load time
				if (result.acc < result.best_acc) {
					result.best_acc = result.acc;
				}
				// Worst Ad complete load time
				if (result.acc > result.worst_acc) {
					result.worst_acc = result.acc;
				}

				if ((status >= 200 && status < 400)) {
					result.keypassed += addressCalled + "\n";
					++result.ac;
				}

				else {
					result.keyfailed += addressCalled + "\n";
				}
			}
		}

		// check for video status
		if (addressCalled.indexOf("type=videoprogress") != -1
				|| addressCalled.indexOf("type=videostart") != -1) {
			// Video Start call
			if (addressCalled.indexOf(";status=start") != -1) {

				// Best Video start call load time
				if (result.vsc < result.best_vsc) {
					result.best_vsc = result.vsc;
				}
				// Worst Video start call load time
				if (result.vsc > result.worst_vsc) {
					result.worst_vsc = result.vsc;
				}

				if ((status >= 200 && status < 400)) {
					result.keypassed += addressCalled + "\n";
					++result.vs;
				}

				else {
					result.keyfailed += addressCalled + "\n";
				}
			}
			// Video Firstquartile call
			if (addressCalled.indexOf(";status=first") != -1) {

				// Best Video FQ call load time
				if (result.vfqc < result.best_vfqc) {
					result.best_vfqc = result.vfqc;
				}
				// Worst Video FQ call load time
				if ((status >= 200 && status < 400)) {
					result.keypassed += addressCalled + "\n";
					++result.vfq;
				}

				else {
					result.keyfailed += addressCalled + "\n";
				}
			}

			// Video Midoint call
			if (addressCalled.indexOf(";status=mid") != -1) {

				// Best video MP call load time
				if (result.vmpc < result.best_vmpc) {
					result.best_vmpc = result.vmpc;
				}
				// Worst video MP call load time
				if (result.vmpc > result.worst_vmpc) {
					result.worst_vmpc = result.vmpc;
				}

				if ((status >= 200 && status < 400)) {
					result.keypassed += addressCalled + "\n";
					++result.vm;
				}

				else {
					result.keyfailed += addressCalled + "\n";
				}
			}
			// Video Third Quartile call
			if (addressCalled.indexOf(";status=third") != -1) {

				// Best Video TQ call load time
				if (result.vtqc < result.best_vtqc) {
					result.best_vtqc = result.vtqc;
				}
				// Worst Video TQ call load time
				if (result.vtqc > result.worst_vtqc) {
					result.worst_vtqc = result.vtqc;
				}

				if ((status >= 200 && status < 400)) {
					result.keypassed += addressCalled + "\n";
					++result.vtq;
				}

				else {
					result.keyfailed += addressCalled + "\n";
				}
			}
			// Video Complete call
			if (addressCalled.indexOf(";status=complete") != -1) {

				// Best Video Complete call load time
				if (result.vcc < result.best_vcc) {
					result.best_vcc = result.vcc;
				}
				// Worst Video complete call load time
				if (result.vcc > result.worst_vcc) {
					result.worst_vcc = result.vcc;
				}

				if ((status >= 200 && status < 400)) {
					result.keypassed += addressCalled + "\n";
					++result.vc;
				}

				else {
					result.keyfailed += addressCalled + "\n";
				}
			}
		}

		// check for ad click through
		if (addressCalled.indexOf("type=adclick&status=click") != -1) {
			// Best Ad click thru load time
			if (result.actc < result.best_actc) {
				result.actc = result.actc;
			}
			// Worst Ad click thru load time
			if (result.actc > result.worst_actc) {
				result.worst_actc = result.actc;
			}

			if (status >= 200 && status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.act;
			} else {

			}
		}

		// check for smart player call
		if (addressCalled.indexOf("ventunoSmartPlayer.js") != -1) {
			result.spc = newj.getAsJsonObject().get("time").getAsInt();
			result.avg_spc = ((n - 1) * result.avg_spc + result.spc) / n;

			// check for best smart player load time
			if (result.spc < result.best_spc) {
				result.best_spc = result.spc;
			}

			// check for worst smart player load time
			if (result.spc > result.worst_spc) {
				result.worst_spc = result.spc;
			}

			if ((status >= 200 && status < 400)) {
				result.keypassed += addressCalled + "\n";
				++result.spcc;
			}

			else {
				result.keyfailed += addressCalled + "\n";
			}

		}

		// check for ventuno lib call
		if (addressCalled.indexOf("ventuno-lib") != -1) {
			result.vlc = newj.getAsJsonObject().get("time").getAsInt();
			result.avg_vlc = ((n - 1) * result.avg_vlc + result.vlc) / n;

			// check for best ventuno lib call load time
			if (result.vlc < result.best_vlc) {
				result.best_vlc = result.vlc;
			}

			// check for worst ventuno lib call load time
			if (result.vlc > result.worst_vlc) {
				result.worst_vlc = result.vlc;
			}

			if ((status >= 200 && status < 400)) {
				result.keypassed += addressCalled + "\n";
				++result.vlcc;
			}

			else {
				result.keyfailed += addressCalled + "\n";
			}
		}

		// check for beacon call
		if (addressCalled.indexOf("beacon.gif?type=script") != -1) {
			result.bc = newj.getAsJsonObject().get("time").getAsInt();
			result.avg_bc = ((n - 1) * result.avg_bc + result.bc) / n;

			// check for best beacon call load time
			if (result.bc < result.best_bc) {
				result.best_bc = result.bc;
			}

			// check for worst beacon call load time
			if (result.bc > result.worst_bc) {
				result.worst_bc = result.bc;
			}

			if ((status >= 200 && status < 400)) {
				result.keypassed += addressCalled + "\n";
				++result.bcc;
			}

			else {
				result.keyfailed += addressCalled + "\n";
			}
		}
		// check for ventuno webplayer min
		if (addressCalled.indexOf("ventuno-webplayer.min") != -1) {

			result.venwebplayerminc = newj.getAsJsonObject().get("time")
					.getAsInt();
			result.avg_venwebplayermin = ((n - 1) * result.avg_venwebplayermin + result.venwebplayerminc)
					/ n;

			// best web player min call
			if (result.venwebplayerminc < result.best_venwebplayermin) {
				result.best_venwebplayermin = result.venwebplayerminc;
			}

			// worst web player min call
			if (result.venwebplayerminc > result.best_venwebplayermin) {
				result.best_venwebplayermin = result.venwebplayerminc;
			}

			if ((status >= 200 && status < 400)) {
				result.keypassed += addressCalled + "\n";
				++result.venwebplayermin;
			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}

		// check for jquery call
		if (addressCalled.indexOf("jquery") != -1) {
			result.jqc = newj.getAsJsonObject().get("time").getAsInt();
			result.avg_jqc = ((n - 1) * result.avg_jqc + result.jqc) / n;

			// check for best Jquery call load time
			if (result.jqc < result.best_jqc) {
				result.best_jqc = result.jqc;
			}

			// check for worst Jquery call load time
			if (result.jqc > result.worst_jqc) {
				result.worst_jqc = result.jqc;
			}

			if ((status >= 200 && status < 400)) {
				result.keypassed += addressCalled + "\n";
				++result.jqcc;
			}

			else {
				result.keyfailed += addressCalled + "\n";
			}

		}

		// check for html5Wrapper call
		if (addressCalled.indexOf("vtnHtml5Wrapper") != -1) {
			result.html5wc = newj.getAsJsonObject().get("time").getAsInt();
			result.avg_html5wc = ((n - 1) * result.avg_html5wc + result.html5wc)
					/ n;

			// check for besthtml5Wrapper call load time
			if (result.html5wc < result.best_html5wc) {
				result.best_html5wc = result.html5wc;
			}

			// check for worst html5Wrapper call load time
			if (result.html5wc > result.worst_html5wc) {
				result.worst_html5wc = result.html5wc;
			}

			if ((status >= 200 && status < 400)) {
				result.keypassed += addressCalled + "\n";
				++result.html5wcc;
			}

			else {
				result.keyfailed += addressCalled + "\n";
			}

		}

		// check for pc view elements call
		if (addressCalled.indexOf("ventunoPCViewElements") != -1) {
			result.pcvc = newj.getAsJsonObject().get("time").getAsInt();
			result.avg_pcvc = ((n - 1) * result.avg_pcvc + result.pcvc) / n;

			// check for best smart player load time
			if (result.pcvc < result.best_pcvc) {
				result.best_pcvc = result.pcvc;
			}

			// check for worst smart player load time
			if (result.pcvc > result.worst_pcvc) {
				result.worst_pcvc = result.pcvc;
			}

			if ((status >= 200 && status < 400)) {
				result.keypassed += addressCalled + "\n";
				++result.pcvcc;
			} else {
				result.keyfailed += addressCalled + "\n";
			}

		}

		// check for loader image call
		if (addressCalled.indexOf("public/images/loader") != -1) {
			result.lic = newj.getAsJsonObject().get("time").getAsInt();
			result.avg_lic = ((n - 1) * result.avg_lic + result.lic) / n;

			// check for best loader image load time
			if (result.lic < result.best_lic) {
				result.best_lic = result.lic;
			}

			// check for worst loader image load time
			if (result.lic > result.worst_lic) {
				result.worst_lic = result.lic;
			}

			if ((status >= 200 && status < 400)) {
				result.keypassed += addressCalled + "\n";
				++result.licc;
			}

			else {
				result.keyfailed += addressCalled + "\n";
			}

		}

		// check for playlist request
		if (addressCalled.indexOf("player_request") != -1) {
			result.pc = newj.getAsJsonObject().get("time").getAsInt();
			result.avg_pc = ((n - 1) * result.avg_pc + result.pc) / n;

			// check for best playlist request load time
			if (result.pc < result.best_pc) {
				result.best_pc = result.pc;
			}

			// check for worst playlist request load time
			if (result.pc > result.worst_pc) {
				result.worst_pc = result.pc;
			}

			if ((status >= 200 && status < 400)) {
				result.keypassed += addressCalled + "\n";

				++result.pcc;
			}

			else {
				result.keyfailed += addressCalled + "\n";
			}
		}

		// check for poster thumb call
		if (addressCalled.indexOf("http://ventunovideos.edgesuite.net") != -1
				&& addressCalled.indexOf(".jpg") != -1) {
			result.ptc = newj.getAsJsonObject().get("time").getAsInt();
			result.avg_ptc = ((n - 1) * result.avg_ptc + result.ptc) / n;

			// check for best poster thumb load time
			if (result.ptc < result.best_ptc) {
				result.best_ptc = result.ptc;
			}

			// check for worst poster thumb load time
			if (result.ptc > result.worst_ptc) {
				result.worst_ptc = result.ptc;
			}

			if ((status >= 200 && status < 400)) {
				result.keypassed += addressCalled + "\n";
				++result.ptcc;
			}

			else {
				result.keyfailed += addressCalled + "\n";
			}

		}

		// Related video plugin call
		if (addressCalled.indexOf("relatedvideo-plugin") != -1) {
			if (status >= 200 && status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.rel_video_plugin;
			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}

		// Title Plugin Min
		if (addressCalled.indexOf("title-plugin.min") != -1) {
			if (status >= 200 && status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.title_plugin;
			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}

		// endscreen-plugin.min
		if (addressCalled.indexOf("endscreen-plugin.min") != -1) {
			if (status >= 200 && status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.es_plugin;
			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}

		// ms-controlbar-plugin.min
		if (addressCalled.indexOf("ms-controlbar-plugin.min") != -1) {
			if (status >= 200 && status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.ms_cb_plugin;
			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}

		// ms-iconmenu-plugin.min
		if (addressCalled.indexOf("ms-iconmenu-plugin.min") != -1) {
			if (status >= 200 && status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.ms_icon_plugin;
			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}

		// UserInfo Call
		if (addressCalled.indexOf("type=userinfo;") != -1
				&& addressCalled.indexOf("status=userinfo") != -1) {
			// Best userinfo call load time
			if (result.userinfoc < result.best_userinfo) {
				result.best_userinfo = result.userinfoc;
			}

			// Worst userinfo call load time
			if (result.userinfoc > result.worst_userinfo) {
				result.worst_userinfo = result.userinfoc;
			}

			if (status >= 200 && status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.userinfo;

			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}
		// Viewport call
		if (addressCalled.indexOf("type=viewport") != -1) {

			// Best viewport call load time
			if (result.viewportc < result.best_viewport) {
				result.best_viewport = result.viewportc;
			}

			// Worst viewport call load time
			if (result.viewportc > result.worst_viewport) {
				result.worst_viewport = result.viewportc;
			}

			if (status >= 200 && status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.viewport;
			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}

		// Autoplay call
		if (addressCalled.indexOf("type=videoengengagement") != -1
				&& addressCalled.indexOf("status=autoplay") != -1) {
			// Best Autoplay call load time
			if (result.autoplayc < result.best_autoplay) {
				result.best_autoplay = result.autoplayc;
			}

			// Worst Autoplay call load time
			if (result.autoplayc > result.worst_autoplay) {
				result.worst_autoplay = result.autoplayc;
			}

			if (status >= 200 & status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.autoplay;
			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}

		// videoListingWidget-view
		if (addressCalled.indexOf("videoListingWidget-view") != -1) {
			if (status >= 200 && status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.widget_view;
			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}

		// titleText.min
		if (addressCalled.indexOf("titleText.min") != -1) {
			if (status >= 200 && status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.title_text;
			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}

		// ms-ad-indicator.min
		if (addressCalled.indexOf("ms-ad-indicator.min") != -1) {
			if (status >= 200 && status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.Ad_indicator;
			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}

		// ms-ad-knowmore.min
		if (addressCalled.indexOf("ms-ad-knowmore.min") != -1) {
			if (status >= 200 && status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.Ad_knowmore;
			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}

		// ms-skip-ad.min
		if (addressCalled.indexOf("ms-skip-ad.min") != -1) {
			if (status >= 200 && status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.SkipAdmin;
			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}

		// previewThumb.min
		if (addressCalled.indexOf("previewThumb.min") != -1) {
			if (status >= 200 && status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.previewthumb;
			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}

		// adCountDown.min
		if (addressCalled.indexOf("adCountDown.min") != -1) {
			if (status >= 200 && status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.Adcountdown;
			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}

		// Postrolladmode type

		if (addressCalled.indexOf("post_ad_status.php?") != -1
				&& addressCalled.indexOf("admode=postroll") != -1) {
			if (status >= 200 && status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.postrolladmode;
			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}

		// SkipAdflow
		if (addressCalled.indexOf("type=adengagementothers") != -1
				&& addressCalled.indexOf("status=skip") != -1) {
			if (status >= 200 && status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.SkipAd;
			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}

		// PlayerMuteflow
		if ((addressCalled.indexOf("type=adengagement") != -1 || addressCalled
				.indexOf("type=videoengengagement") != -1)
				&& (addressCalled.indexOf("status=unmute") != -1)) {
			if (status >= 200 && status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.unmute;
			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}

		// PlayerUnmuteflow
		if ((addressCalled.indexOf("type=adengagement") != -1 || addressCalled
				.indexOf("type=videoengengagement") != -1)
				&& (addressCalled.indexOf("status=unmute") != -1)) {
			if (status >= 200 && status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.unmute;
			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}
		// Player Pause flow
		if ((addressCalled.indexOf("type=adengagement") != -1 || addressCalled
				.indexOf("type=videoengengagement") != -1)
				&& (addressCalled.indexOf("status=pause") != -1)) {
			if (status >= 200 && status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.pause;
			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}
		// Player Resume flow
		if ((addressCalled.indexOf("type=adengagement") != -1 || addressCalled
				.indexOf("type=videoengengagement") != -1)
				&& (addressCalled.indexOf("status=resume") != -1)) {
			if (status >= 200 && status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.resume;
			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}
		// Player fullscreen flow
		if ((addressCalled.indexOf("type=adengagement") != -1 || addressCalled
				.indexOf("type=videoengengagement") != -1)
				&& (addressCalled.indexOf("status=fullscreen") != -1)) {
			if (status >= 200 && status < 400) {
				result.keypassed += addressCalled + "\n";
				++result.fullscreen;
			} else {
				result.keyfailed += addressCalled + "\n";
			}
		}

	}

}
