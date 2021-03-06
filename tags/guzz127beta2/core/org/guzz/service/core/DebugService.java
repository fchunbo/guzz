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
package org.guzz.service.core;

import org.guzz.orm.sql.BindedCompiledSQL;

/**
 * 
 * 
 *
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public interface DebugService {
	
	public static final String DEMON_NAME_PREFIX = "gz_demon_t_" ;
	
	public boolean isDebugMode() ;
	
	public boolean isLogParams() ;
	
	public void onErrorProcess(String msg, Exception e) ;

	public void logSQL(String sql, Object[] params) ;
	
	public void logSQL(BindedCompiledSQL bsql) ;
	
	public void logSQL(BindedCompiledSQL bsql, String sqlStatment) ;
	
}
