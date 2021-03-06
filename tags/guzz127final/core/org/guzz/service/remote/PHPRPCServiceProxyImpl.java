/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.guzz.service.remote;

import java.util.Properties;

import org.phprpc.PHPRPC_Client;

/**
 * 
 * 
 *
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class PHPRPCServiceProxyImpl implements RemoteRPCProxy{
	
	private String serviceURL ;
	
	public void startup(Properties props){
		serviceURL = props.getProperty("serviceURL") ;
	}

	public Object getRemoteStub(Class serviceInterface) {
		PHPRPC_Client client = new PHPRPC_Client(serviceURL); 
		
		return client.useService(serviceInterface);
	}	

	public void close(){
		
	}

}
