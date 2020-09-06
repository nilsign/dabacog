package com.nilsign.generators.database.postgresql;

import com.nilsign.dxd.model.DxdModel;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PostgreSqlSchemaBuilder {

  public static String buildSettings(@NonNull DxdModel model) {
    return new StringBuffer()
        .append("\n-- Global Settings")
        .append("\nSET client_encoding = 'UTF8';\n")
        .toString();
  }

  public static String buildDropSchema(@NonNull DxdModel model) {
    return model.getConfig().isSqlDropSchema()
        ? new StringBuffer()
            .append("\n-- Drops the database schema")
            .append("\nDROP SCHEMA IF EXISTS public CASCADE;")
            .append("\nCREATE SCHEMA public;")
            .append("\nGRANT ALL ON SCHEMA public TO postgres;")
            .append("\nGRANT ALL ON SCHEMA public TO public;\n")
            .toString()
        : "";
  }

  public static String buildGlobalSequence(@NonNull DxdModel model) {
    return model.getConfig().isSqlGlobalSequence()
        ? new StringBuilder()
            .append("\n-- Configures a global id sequence shared by all generated primary keys")
            .append("\nCREATE SEQUENCE IF NOT EXISTS public.shared_sequence")
            .append("\n    START WITH 1")
            .append("\n    INCREMENT BY 1")
            .append("\n    NO MINVALUE")
            .append("\n    NO MAXVALUE")
            .append("\n    CACHE 1;\n")
            .toString()
        : "";
  }

  public static String buildTables(@NonNull DxdModel model) {
    StringBuffer output = new StringBuffer()
        .append("\n-- Creates tables");
    model.getClasses().forEach(aClass
        -> output.append(PostgreSqlTableBuilder.buildTable(aClass)));
    model.getDistinctManyToManyRelations().forEach(relation
        -> output.append(PostgreSqlTableBuilder.buildRelationalTable(relation)));
    return output.toString();
  }

  public static String buildForeignKeys(@NonNull DxdModel model) {
    StringBuffer output = new StringBuffer()
        .append("\n-- Creates foreign keys");
    model.getClasses().forEach(aClass
        -> aClass.getFields().forEach(field
            -> output.append(PostgreSqlForeignKeyBuilder.buildForeignKey(aClass, field))));
    model.getDistinctManyToManyRelations().forEach(relation
        -> output.append(PostgreSqlForeignKeyBuilder.buildForeignKeys(relation)));
    return output.toString();
  }

  public static String buildIndices(@NonNull DxdModel model) {
    StringBuffer output = new StringBuffer()
        .append("\n-- Creates indices");
    model.getClasses().forEach(aClass
        -> aClass.getFields().forEach(field
        -> output.append(PostgreSqlIndexBuilder.buildIndex(aClass, field))));
    return output.toString();
  }
}
