/**
 * 
 */
package com.mongodb.anil;

/**
 * @author h122337
 * 
 */
public final class BaseOperationsHelper {
	/**
	 * Start operation layout.
	 * 
	 * @param methodName
	 *            the method name
	 */
	public static void startOperationLayout(String methodName) {
		System.out.println("\n-------------------------------- " + methodName
				+ " -----------------------------");
	}

	/**
	 * Close operation layout.
	 */
	public static void closeOperationLayout() {
		System.out.println("----------------------------------------"
				+ "------------------------------------");
	}
}
