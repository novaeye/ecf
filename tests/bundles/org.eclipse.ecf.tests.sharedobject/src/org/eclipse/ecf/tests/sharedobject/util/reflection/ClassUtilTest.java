/*******************************************************************************
 * Copyright (c) 2010 Markus Alexander Kuppe.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Markus Alexander Kuppe (ecf-dev_eclipse.org <at> lemmster <dot> de) - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.tests.sharedobject.util.reflection;

import java.lang.reflect.Method;
import java.util.Arrays;

import junit.framework.TestCase;

import org.eclipse.ecf.core.util.reflection.ClassUtil;

public class ClassUtilTest extends TestCase {

	/**
	 * Test method for {@link org.eclipse.ecf.core.util.reflection.ClassUtil#getMethod(java.lang.Class, java.lang.String, java.lang.Class[])}.
	 */
	public void testGetPrimitiveMethodWithPrimitive() {
		testGetMethod(new Class[] {int.class}, new Class[] {int.class});		
	}
	
	/**
	 * Test method for {@link org.eclipse.ecf.core.util.reflection.ClassUtil#getMethod(java.lang.Class, java.lang.String, java.lang.Class[])}.
	 */
	public void testGetPrimitiveMethodWithObject() {
		testGetMethod(new Class[] {Integer.class}, new Class[] {int.class});
	}

	/**
	 * Test method for {@link org.eclipse.ecf.core.util.reflection.ClassUtil#getMethod(java.lang.Class, java.lang.String, java.lang.Class[])}.
	 */
	public void testGetObjectMethodWithObject() {
		testGetMethod(new Class[] {Long.class}, new Class[]{Long.class});
	}

	/**
	 * Test method for {@link org.eclipse.ecf.core.util.reflection.ClassUtil#getMethod(java.lang.Class, java.lang.String, java.lang.Class[])}.
	 */
	public void testGetObjectMethodWithPrimitive() {
		testGetMethod(new Class[] {long.class}, new Class[]{Long.class});
	}
	
	/**
	 * Test method for {@link org.eclipse.ecf.core.util.reflection.ClassUtil#getMethod(java.lang.Class, java.lang.String, java.lang.Class[])}.
	 */
	public void testGetObjectMethodWhenBoth() {
		testGetMethod(new Class[] {Boolean.class}, new Class[]{Boolean.class});
	}
	
	/**
	 * Test method for {@link org.eclipse.ecf.core.util.reflection.ClassUtil#getMethod(java.lang.Class, java.lang.String, java.lang.Class[])}.
	 */
	public void testGetPrimitiveMethodWhenBoth() {
		testGetMethod(new Class[] {boolean.class}, new Class[]{boolean.class});
	}
	
	// helper
	private void testGetMethod(Class[] searchParameterTypes, Class[] expectedParameterTypes) {
		Method method = null;
		try {
			method = ClassUtil.getMethod(TestClass.class, TestClass.methodName, searchParameterTypes);
		} catch (NoSuchMethodException e) {
			fail("failed to match expected the method: " + e.getMessage());
		}
		
		final Class[] someParameterTypes = method.getParameterTypes();
		assertTrue("Parameters don't match", Arrays.equals(expectedParameterTypes, someParameterTypes));
	}

	// helper class 
	public static class TestClass {
		public static String methodName = "foo";
		public void foo(final int i) {}
		public void foo(final Long i) {}
		public void foo(final boolean b) {}
		public void foo(final Boolean b) {}
	}
}
