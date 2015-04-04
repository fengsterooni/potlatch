/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.coursera.androidcapstone.potlatch.services;

import android.content.Context;
import android.content.Intent;

import org.coursera.androidcapstone.potlatch.activities.LoginScreenActivity;
import org.coursera.androidcapstone.potlatch.interfaces.GiftSvcApi;
import org.coursera.androidcapstone.potlatch.oauth.SecuredRestBuilder;
import org.coursera.androidcapstone.potlatch.helpers.EasyHttpClient;

import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;

public class GiftSvc {

	public static final String CLIENT_ID = "mobile";

	private static GiftSvcApi GiftSvc_;

	public static synchronized GiftSvcApi getOrShowLogin(Context ctx) {
		if (GiftSvc_ != null) {
			return GiftSvc_;
		} else {
			Intent i = new Intent(ctx, LoginScreenActivity.class);
			ctx.startActivity(i);
			return null;
		}
	}

	public static synchronized GiftSvcApi init(String server, String user,
			String pass) {

		GiftSvc_ = new SecuredRestBuilder()
				.setLoginEndpoint(server + GiftSvcApi.TOKEN_PATH)
				.setUsername(user)
				.setPassword(pass)
				.setClientId(CLIENT_ID)
				.setClient(
						new ApacheClient(new EasyHttpClient()))
				.setEndpoint(server).setLogLevel(LogLevel.FULL).build()
				.create(GiftSvcApi.class);

		return GiftSvc_;
	}
}
