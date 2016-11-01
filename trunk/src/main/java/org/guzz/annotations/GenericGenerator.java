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
package org.guzz.annotations;

import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 
 * Generator annotation describing any kind of guzz generator in a detyped manner
 *
 * @author liu kaixuan(liukaixuan@gmail.com)
 */
@Target({PACKAGE, TYPE, METHOD, FIELD})
@Retention(value=RUNTIME)
public @interface GenericGenerator {

	/**
	 * unique generator name
	 */
	String name() ;
	
	/**
	 * Generator strategy either a predefined Hibernate strategy or a fully qualified class name.
	 */
	String strategy () ;
	
	/**
	 * Optional generator parameters
	 */
	Parameter[] parameters() default {} ;
}
