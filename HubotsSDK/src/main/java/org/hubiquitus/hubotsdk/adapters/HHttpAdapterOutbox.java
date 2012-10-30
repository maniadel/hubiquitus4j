/*
 * Copyright (c) Novedia Group 2012.
 *
 *     This file is part of Hubiquitus.
 *
 *     Hubiquitus is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Hubiquitus is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Hubiquitus.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.hubiquitus.hubotsdk.adapters;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hubiquitus.hapi.client.HMessageDelegate;
import org.hubiquitus.hapi.hStructures.HMessage;
import org.hubiquitus.hubotsdk.AdapterOutbox;
import org.hubiquitus.hubotsdk.adapters.HHttpAdapter.HHttpAttachement;
import org.hubiquitus.hubotsdk.adapters.HHttpAdapter.HHttpData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("unused")
public class HHttpAdapterOutbox extends AdapterOutbox {
	final Logger logger = LoggerFactory.getLogger(HHttpAdapterOutbox.class);

    public HHttpAdapterOutbox(){
		super();
	}
	@Override
	public void sendMessage(HMessage message, HMessageDelegate callback) {
		if ("hhttpdata".equalsIgnoreCase(message.getType())) {
			 try {
                HHttpData httpData = new HHttpData(message.getPayloadAsJSONObject());
                // Only http post is supported for now.
                // Only handle with the attachments.
                httpPost(httpData);
            } catch (JSONException e) {
				logger.error("can not read the HttpData: ", e);
			}
		} else {
			logger.warn("Only type hHttpData is supported to be sent by http adapter outbox.");
		}
	}
	
	private void httpPost(HHttpData httpData){
		String uriString = "http://";

		uriString = uriString + httpData.getServerName() + ":"
				+ httpData.getServerPort();
		if (httpData.getQueryPath() != null
				&& httpData.getQueryPath().length() > 0) {
			uriString += httpData.getQueryPath();
		}
		if (httpData.getQueryArgs() != null
				&& httpData.getQueryArgs().length() > 0) {
			uriString += httpData.getQueryArgs();
		}
		
		try {
				HttpClient client = new DefaultHttpClient();
				// method : post
				HttpPost post = new HttpPost(uriString);
				if(httpData.getAttachments()!=null){
					// add post attachments
				    MultipartEntity reqEntity = new MultipartEntity();
				    JSONArray nameArray = httpData.getAttachments().names();
				    for(int i = 0 ; i < nameArray.length() ; i++){
				    	HHttpAttachement hattachment = new HHttpAttachement(httpData.getAttachments().getJSONObject(nameArray.getString(i)));
				    	reqEntity.addPart(nameArray.getString(i), new ByteArrayBody(hattachment.getData(), hattachment.getContentType(), hattachment.getName()));
				    }
				    post.setEntity(reqEntity);	
				    HttpResponse response = client.execute(post);
				    logger.debug("Http response status : " + response.getStatusLine());
				}
			
		} catch (Exception e) {
			logger.error("can not send an HttpData : ", e);
		}
	}
	
	
	@Override
	public void setProperties(JSONObject properties) {
		// NOP
	}

	@Override
	public void start() {
        // NOP
	}

	@Override
	public void stop() {
        // NOP
	}

}