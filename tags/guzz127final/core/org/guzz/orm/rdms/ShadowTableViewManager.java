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
package org.guzz.orm.rdms;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.guzz.GuzzContextImpl;
import org.guzz.orm.ShadowTableView;
import org.guzz.web.context.ExtendedBeanFactory;
import org.guzz.web.context.ExtendedBeanFactoryAware;
import org.guzz.web.context.GuzzContextAware;

/**
 * 
 * TODO: refact to interface.
 *
 * @author liu kaixuan(liukaixuan@gmail.com)
 */
public class ShadowTableViewManager {
	private transient static Log log = LogFactory.getLog(ShadowTableViewManager.class.getName()) ;
	
	private List views = new LinkedList() ;
	
	private GuzzContextImpl guzzContextImpl ;
	
	public ShadowTableViewManager(GuzzContextImpl guzzContextImpl){
		this.guzzContextImpl = guzzContextImpl ;
	}
	
	public void addShadowView(ShadowTableView view){
		this.views.add(view) ;
		
		if(guzzContextImpl.isFullStarted()){
			if(view instanceof GuzzContextAware){
				((GuzzContextAware) view).setGuzzContext(guzzContextImpl) ;
			}
			
			view.startup() ;
		}
	}
	
	public void onGuzzFullStarted(){
		for(int i = 0 ; i < views.size() ; i++){
			ShadowTableView view = (ShadowTableView) views.get(i) ;
			
			if(view instanceof GuzzContextAware){
				((GuzzContextAware) view).setGuzzContext(guzzContextImpl) ;
			}
			
			view.startup() ;
		}
	}
	
	public void onExtendedBeanFactorySetted(ExtendedBeanFactory extendedBeanFactory){
		for(int i = 0 ; i < views.size(); i++){
			ShadowTableView view = (ShadowTableView) views.get(i) ;
			
			if(view instanceof ExtendedBeanFactoryAware){
				((ExtendedBeanFactoryAware) view).setExtendedBeanFactory(guzzContextImpl.getExtendedBeanFactory()) ;
			}
		}
	}
	
	public void shutdown(){
		for(int i = 0 ; i < views.size() ; i++){
			ShadowTableView loader = (ShadowTableView) views.get(i) ;
			
			try {
				loader.shutdown() ;
			} catch (Exception e) {
				log.error("error while shutting down ColumnDataLoader:" + loader.getClass(), e) ;
			}
		}
		
		this.views.clear() ;
	}

}
