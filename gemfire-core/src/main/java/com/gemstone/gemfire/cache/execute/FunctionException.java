/*
 *=========================================================================
 * Copyright (c) 2002-2014 Pivotal Software, Inc. All Rights Reserved.
 * This product is protected by U.S. and international copyright
 * and intellectual property laws. Pivotal products are covered by
 * more patents listed at http://www.pivotal.io/patents.
 *=========================================================================
 */

package com.gemstone.gemfire.cache.execute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.gemstone.gemfire.GemFireException;
import com.gemstone.gemfire.internal.Assert;

/**
 * Thrown to indicate an error or exceptional condition during the execution of 
 * {@linkplain Function}s in GemFire. This exception can be thrown by GemFire 
 * as well as user code, in the implementation of {@linkplain Function#execute(FunctionContext)}.
 * When FunctionException is thrown in an implementation of 
 * {@linkplain Function#execute(FunctionContext)}, GemFire will transmit it back 
 * to, and throw it on, the calling side. For example, if a GemFire client 
 * executes a Function on a server, and the function's execute method throws a
 * FunctionException, the server logs the exception as a warning, and transmits
 * it back to the calling client, which throws it. This allows for separation of
 * business and error handling logic, as client code that processes function 
 * execution results does not have to deal with errors; errors can be dealt with
 * in the exception handling logic, by catching this exception.
 *
 * <p>The exception string provides details on the cause of failure.
 * </p>
 * 
 * @author Yogesh Mahajan
 * @author Mitch Thomas
 * 
 * @since 6.0
 * @see FunctionService
 */
public class FunctionException extends GemFireException {

  private static final long serialVersionUID = 4893171227542647452L;

  private transient ArrayList<Throwable> exceptions;

  /**
   * Creates new function exception with given error message.
   * 
   * @since 6.5
   */
  public FunctionException() {
  }

  /**
   * Creates new function exception with given error message.
   * 
   * @param msg
   * @since 6.0
   */
  public FunctionException(String msg) {
    super(msg);
  }

  /**
   * Creates new function exception with given error message and optional nested
   * exception.
   * 
   * @param msg
   * @param cause
   * @since 6.0 
   */
  public FunctionException(String msg, Throwable cause) {
    super(msg, cause);
  }

  /**
   * Creates new function exception given throwable as a cause and source of
   * error message.
   * 
   * @param cause
   * @since 6.0 
   */
  public FunctionException(Throwable cause) {
    super(cause);
  }

  /**
   * Adds exceptions thrown from different nodes to a ds
   * 
   * @param cause
   * @since 6.5
   */
  public final void addException(Throwable cause) {
    Assert.assertTrue(cause != null,
        "unexpected null exception to add to FunctionException");
    getExceptions().add(cause);
  }

  /**
   * Returns the list of exceptions thrown from different nodes
   * 
   * @since 6.5
   */
  public final List<Throwable> getExceptions() {
    if (this.exceptions == null) {
      this.exceptions = new ArrayList<Throwable>();
    }
    return this.exceptions;
  }

  /**
   * Adds the list of exceptions provided
   * 
   * @since 6.5
   */
  public final void addExceptions(Collection<? extends Throwable> ex) {
    getExceptions().addAll(ex);
  }
}
