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
package org.guzz.api.taglib;

import org.guzz.transaction.WriteTranSession;


/**
 * 添加一个新的ghost实例
 * @author liu kaixuan(liukaixuan@gmail.com)
 */
public class GhostBirthTag extends RestGhostTag {
	
	protected void resetToDefault() {
		super.resetToDefault();
		
		setOp("add") ;
	}

	protected void internalRestGhost(Object ghostObject) {		
		WriteTranSession tran = guzzContext.getTransactionManager().openRWTran(true) ;
		
		try{
			tran.insert(ghostObject) ;
		}catch(Exception e){
			tran.close() ;
		}
	}
	
}
