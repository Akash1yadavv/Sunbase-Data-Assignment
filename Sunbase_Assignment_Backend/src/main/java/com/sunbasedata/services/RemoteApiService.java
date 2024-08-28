package com.sunbasedata.services;

import com.sunbasedata.exceptions.RemoteApiException;

public interface RemoteApiService {

	String syncCustomersFromRemoteApi()throws RemoteApiException;

}
