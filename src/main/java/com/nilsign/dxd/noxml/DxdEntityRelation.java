package com.nilsign.dxd.noxml;

import com.nilsign.dxd.xml.entities.DxdEntityClass;
import com.nilsign.dxd.xml.entities.DxdEntityField;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

@Data(staticConstructor = "of")
@Setter(AccessLevel.PRIVATE)
public class DxdEntityRelation {

  @NonNull
  private final DxdEntityClass referencingClass;

  @NonNull
  private final DxdEntityField referencingField;

  @NonNull
  private final DxdEntityClass referencedClass;

  private final DxdEntityField backReferencingField;

  @NonNull
  private final DxdEntityRelationType type;

  public static DxdEntityRelation of(
      @NonNull DxdEntityClass referencingClass,
      @NonNull DxdEntityField referencingField,
      @NonNull DxdEntityClass referencedClass) {
    DxdEntityField backReferencingField = referencedClass.findReferenceFieldTo(referencingClass);
    return of(
        referencingClass,
        referencingField,
        referencedClass,
        backReferencingField,
        detectRelationType(referencingField, backReferencingField));
  }

  private static DxdEntityRelationType detectRelationType(
      @NonNull DxdEntityField referencingField,
      DxdEntityField backReferencingField) {
    if (referencingField.isToManyRelation()) {
      return backReferencingField == null || backReferencingField.isToManyRelation()
          ? DxdEntityRelationType.MANY_TO_MANY
          : DxdEntityRelationType.MANY_TO_ONE;
    }
    if (referencingField.isToOneRelation()) {
      return backReferencingField == null || backReferencingField.isToManyRelation()
          ? DxdEntityRelationType.ONE_TO_MANY
          : DxdEntityRelationType.ONE_TO_ONE;
    }
    return null;
  }

  public boolean hasBackReferencingField() {
    return backReferencingField != null;
  }

  public boolean isManyToMany() {
    return type == DxdEntityRelationType.MANY_TO_MANY;
  }

  public boolean isManyToOne() {
    return type == DxdEntityRelationType.MANY_TO_ONE;
  }

  public boolean isOneToMany() {
    return type == DxdEntityRelationType.ONE_TO_MANY;
  }

  public boolean isOneToOne() {
    return type == DxdEntityRelationType.ONE_TO_ONE;
  }

  @Override
  public String toString() {
    return String.format("%s -> %s [%s]%s",
        referencingClass.getName(),
        referencedClass.getName(),
        type,
        !hasBackReferencingField() ? "[NO-BACK-REFERENCE]" : "");
  }
}
