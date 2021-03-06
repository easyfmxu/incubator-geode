/*=========================================================================
 * Copyright (c) 2005-2014 Pivotal Software, Inc. All Rights Reserved.
 * This product is protected by U.S. and international copyright
 * and intellectual property laws. Pivotal products are covered by
 * more patents listed at http://www.pivotal.io/patents.
 *========================================================================
 */

package com.gemstone.gemfire.cache.query.internal.types;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;

import com.gemstone.gemfire.DataSerializer;
import com.gemstone.gemfire.cache.query.*;
import com.gemstone.gemfire.cache.query.types.*;

import com.gemstone.gemfire.cache.query.internal.*;
import com.gemstone.gemfire.internal.i18n.LocalizedStrings;

/**
 * Implementation of StructType
 * @author Eric Zoerner
 * @since 4.0
 */
public final class StructTypeImpl extends ObjectTypeImpl implements StructType {
  private static final long serialVersionUID = -6368710865627039204L;
  private  String[] fieldNames;
  private  ObjectType[] fieldTypes;
  private String[] indexAlternativeFieldNames;
  
  /**
   * Empty constructor to satisfy <code>DataSerializer</code> requirements
   */
  public  StructTypeImpl() { 
  }
  
  /** Creates a new instance of StructType */
  public StructTypeImpl(String[] fieldNames) {
    this(fieldNames, null);
  }

  public StructTypeImpl(String[] fieldNames, ObjectType[] fieldTypes) {
    this(Struct.class, fieldNames, fieldTypes);
  }
  
  public StructTypeImpl(String[] fieldNames, String[] indexAlternativeFieldNames, ObjectType[] fieldTypes) {
    this(Struct.class, fieldNames, fieldTypes);
    this.indexAlternativeFieldNames = indexAlternativeFieldNames;
  }
  
  // supply actual implementation class, handles Region.Entry case
  // where Region.Entry acts like a Struct with extra protocol (e.g. getStatistics, etc.)
  // By using a StructType, we get access to key and value types as well as the
  // Region.Entry properties.
  public StructTypeImpl(Class clazz, String[] fieldNames, ObjectType[] fieldTypes) {
    super(clazz);
    if (fieldNames == null) {
      throw new IllegalArgumentException(LocalizedStrings.StructTypeImpl_FIELDNAMES_MUST_NOT_BE_NULL.toLocalizedString());
    }
    this.fieldNames = fieldNames;
    this.fieldTypes = fieldTypes == null ?
                      new ObjectType[this.fieldNames.length] : fieldTypes;
    for (int i = 0; i < this.fieldTypes.length; i++) {
      if (this.fieldTypes[i] == null) {
        this.fieldTypes[i] = TypeUtils.getObjectType(Object.class);
      }
    }
  }
  
  
  public ObjectType[] getFieldTypes() {
    return this.fieldTypes;
  }
  
  public String[] getFieldNames() {
    return this.fieldNames;
  }
  
  public int getFieldIndex(String fieldName) {
    for (int i = 0; i < this.fieldNames.length; i++) {
      if (this.fieldNames[i].equals(fieldName)) {
        return i;
      }
    }
    throw new IllegalArgumentException(LocalizedStrings.StructTypeImpl_FIELDNAME_0_NOT_FOUND.toLocalizedString(fieldName));
  }
  
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof StructTypeImpl)) return false;
    StructTypeImpl t = (StructTypeImpl)obj;
    return  (Arrays.equals(this.fieldNames, t.getFieldNames()) || Arrays.equals(this.indexAlternativeFieldNames, t.getFieldNames())) &&
            Arrays.equals(getFieldTypes(), t.getFieldTypes());
  }
  
  @Override
  public int hashCode() {
    return Arrays.hashCode(fieldNames) ^ Arrays.hashCode(fieldTypes);
  }
  
  @Override
  public String toString(){
    StringBuffer sb = new StringBuffer("struct<");
    for(int i=0; i < fieldNames.length; i++){
      if (i > 0) sb.append(',');
      sb.append(fieldNames[i] + ":" + fieldTypes[i]);
    }
    sb.append('>');
    return sb.toString();
  }

  @Override
  public boolean isCollectionType() { return false; }
  @Override
  public boolean isMapType() { return false; }
  @Override
  public boolean isStructType() { return true; }
  

  // Static Utilities 
  public static StructTypeImpl typeFromStruct(Struct s) {
    // handle unlikely event that Struct is not a StructImpl
    if (s instanceof StructImpl) { 
      return (StructTypeImpl)s.getStructType();
    }
    return new StructTypeImpl(s.getStructType().getFieldNames(), s.getStructType().getFieldTypes());
  }
  
  @Override
  public int getDSFID() {
    return STRUCT_TYPE_IMPL;
  }

  @Override
  public void fromData(DataInput in) throws IOException, ClassNotFoundException {
    super.fromData(in);
    this.fieldNames = DataSerializer.readStringArray(in);    
    this.fieldTypes = (ObjectType[])DataSerializer.readObjectArray(in);
  }

  @Override
  public void toData(DataOutput out) throws IOException {
    super.toData(out);
    DataSerializer.writeStringArray(this.fieldNames, out);
    DataSerializer.writeObjectArray(fieldTypes, out);    
  }  
  
}
