/*=========================================================================
 * Copyright (c) 2010-2014 Pivotal Software, Inc. All Rights Reserved.
 * This product is protected by U.S. and international copyright
 * and intellectual property laws. Pivotal products are covered by
 * one or more patents listed at http://www.pivotal.io/patents.
 *=========================================================================
 */
package com.gemstone.gemfire.internal.statistics;

/**
 * Descriptor containing all of the parameters required to construct a new
 * instance of a {@link com.gemstone.gemfire.internal.StatArchiveWriter}. 
 * This describes the statistics archive.
 * <p/>
 * This is a constructor parameter object for {@link 
 * com.gemstone.gemfire.internal.StatArchiveWriter}. 
 * <p/>
 * {@link StatArchiveDescriptor.Builder} is used for constructing instances
 * instead of a constructor with many similar parameters (ie, multiple Strings
 * which could easily be interposed with one another).
 * 
 * @author Kirk Lund
 * @since 7.0
 */
public class StatArchiveDescriptor {

  private final String archiveName;
  private final long systemId;
  private final long systemStartTime;
  private final String systemDirectoryPath;
  private final String productDescription;
  
  public String getArchiveName() {
    return this.archiveName;
  }
  
  public long getSystemId() {
    return this.systemId;
  }
  
  public long getSystemStartTime() {
    return this.systemStartTime;
  }
  
  public String getSystemDirectoryPath() {
    return this.systemDirectoryPath;
  }
  
  public String getProductDescription() {
    return this.productDescription;
  }
  
  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder(getClass().getName());
    sb.append("@").append(System.identityHashCode(this)).append("{");
    sb.append("archiveName=").append(this.archiveName);
    sb.append(", systemId=").append(this.systemId);
    sb.append(", systemStartTime=").append(this.systemStartTime);
    sb.append(", systemDirectoryPath=").append(this.systemDirectoryPath);
    sb.append(", productDescription=").append(this.productDescription);
    sb.append("}");
    return sb.toString();
  }
  
  public static class Builder {
    private String archiveName;
    private long systemId = -1;
    private long systemStartTime = -1;
    private String systemDirectoryPath;
    private String productDescription;
    
    public Builder setArchiveName(String archiveName) {
      this.archiveName = archiveName;
      return this;
    }

    public Builder setSystemId(long systemId) {
      this.systemId = systemId;
      return this;
    }
    
    public Builder setSystemStartTime(long systemStartTime) {
      this.systemStartTime = systemStartTime;
      return this;
    }
    
    public Builder setSystemDirectoryPath(String systemDirectoryPath) {
      this.systemDirectoryPath = systemDirectoryPath;
      return this;
    }
    
    public Builder setProductDescription(String productDescription) {
      this.productDescription = productDescription;
      return this;
    }
    
    public StatArchiveDescriptor build() throws IllegalStateException {
      if (this.archiveName == null) {
        throw new IllegalStateException("archiveName must not be null");
      }
      if (this.systemId == -1) {
        throw new IllegalStateException("systemId must be a postive number");
      }
      if (this.systemStartTime == -1) {
        throw new IllegalStateException("systemStartTime must be a postive number");
      }
      if (this.systemDirectoryPath == null) {
        throw new IllegalStateException("systemDirectoryPath must not be null");
      }
      if (this.productDescription == null) {
        throw new IllegalStateException("productDescription must not be null");
      }
      return new StatArchiveDescriptor(this);
    }
  }
  
  private StatArchiveDescriptor(Builder builder) {
    this.archiveName = builder.archiveName;
    this.systemId = builder.systemId;
    this.systemStartTime = builder.systemStartTime;
    this.systemDirectoryPath = builder.systemDirectoryPath;
    this.productDescription = builder.productDescription;
  }
}
