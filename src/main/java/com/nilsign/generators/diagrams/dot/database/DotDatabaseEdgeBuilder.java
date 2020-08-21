package com.nilsign.generators.diagrams.dot.database;

import com.nilsign.dxd.model.DxdFieldRelation;
import com.nilsign.generators.database.Sql;
import com.nilsign.generators.diagrams.dot.Dot;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DotDatabaseEdgeBuilder {

  public static String buildTableRelationEdge(@NonNull DxdFieldRelation relation) {
    switch(relation.getType()) {
      case MANY_TO_MANY: return buildManyToManyEdges(relation);
      case MANY_TO_ONE: return buildManyToOneEdge(relation);
      case ONE_TO_MANY: return buildOneToManyEdge(relation);
      case ONE_TO_ONE: return buildOneToOneEdge(relation);
      default: return null;
    }
  }

  private static String buildManyToManyEdges(@NonNull DxdFieldRelation relation) {
    if (!relation.isManyToMany()) {
      return "";
    }
    return new StringBuffer()
        .append(Dot.addEdge(
            String.format("node_%s", Sql.buildTableName(relation)),
            String.format("port_%s",
                Sql.buildForeignKeyFieldNames(relation).getFirst()),
            Dot.PortAlignment.WEST,
            String.format("node_%s",
                Sql.buildTableName(relation.getFirstClass())),
            String.format("port_%s", Sql.SQL_PRIMARY_KEY_NAME),
            Dot.PortAlignment.WEST))
        .append(Dot.addEdge(
            String.format("node_%s", Sql.buildTableName(relation)),
            String.format("port_%s",
                Sql.buildForeignKeyFieldNames(relation).getSecond()),
            Dot.PortAlignment.EAST,
            String.format("node_%s",
                Sql.buildTableName(relation.getSecondClass())),
            String.format("port_%s", Sql.SQL_PRIMARY_KEY_NAME),
            Dot.PortAlignment.WEST))
        .toString();
  }

  private static String buildOneToManyEdge(@NonNull DxdFieldRelation relation) {
    if (!relation.isOneToMany()) {
      return "";
    }
    return Dot.addEdge(
        String.format("node_%s",
            Sql.buildTableName(relation.getFirstClass())),
        String.format("port_%s",
            Sql.buildForeignKeyFieldNames(relation).getSecond()),
        Dot.PortAlignment.EAST,
        String.format("node_%s",
            Sql.buildTableName(relation.getSecondClass())),
        String.format("port_%s", Sql.SQL_PRIMARY_KEY_NAME),
        Dot.PortAlignment.WEST,
        Dot.EdgeStyle.DASHED);
  }

  private static String buildManyToOneEdge(@NonNull DxdFieldRelation relation) {
    if (!relation.isManyToOne()) {
      return "";
    }
    return Dot.addEdge(
        String.format("node_%s",
            Sql.buildTableName(relation.getSecondClass())),
        String.format("port_%s",
            Sql.buildForeignKeyFieldNames(relation).getFirst()),
        Dot.PortAlignment.EAST,
        String.format("node_%s",
            Sql.buildTableName(relation.getFirstClass())),
        String.format("port_%s",
            Sql.SQL_PRIMARY_KEY_NAME),
        Dot.PortAlignment.WEST,
        Dot.EdgeStyle.DASHED);
  }

  private static String buildOneToOneEdge(@NonNull DxdFieldRelation relation) {
    if (!relation.isOneToOne()) {
      return "";
    }
    if (relation.isSelfReference()) {
      return Dot.addEdge(
          String.format(
            "node_%s", Sql.buildTableName(relation.getFirstClass())),
          String.format(
              "port_%s", Sql.buildForeignKeyFieldNames(relation).getFirst()),
          Dot.PortAlignment.EAST,
          String.format(
              "node_%s", Sql.buildTableName(relation.getSecondClass())),
          String.format("port_%s", Dot.PortLocation.TOP.getShortName()),
          Dot.PortAlignment.NORTH,
          Dot.EdgeStyle.DOTTED);
    }
    StringBuffer output = new StringBuffer();
    if (relation.isOneToOne()) {
      output.append(Dot.addEdge(
          String.format(
              "node_%s", Sql.buildTableName(relation.getFirstClass())),
          String.format(
              "port_%s", Sql.buildForeignKeyFieldNames(relation).getSecond()),
          Dot.PortAlignment.EAST,
          String.format(
              "node_%s", Sql.buildTableName(relation.getSecondClass())),
          String.format("port_%s", Sql.SQL_PRIMARY_KEY_NAME),
          Dot.PortAlignment.WEST,
          Dot.EdgeStyle.DOTTED));
      if (relation.isBiDirectional()) {
        output.append(Dot.addEdge(
            String.format(
              "node_%s", Sql.buildTableName(relation.getSecondClass())),
              String.format(
                  "port_%s", Sql.buildForeignKeyFieldNames(relation).getFirst()),
              Dot.PortAlignment.EAST,
              String.format(
                  "node_%s", Sql.buildTableName(relation.getFirstClass())),
              String.format("port_%s", Sql.SQL_PRIMARY_KEY_NAME),
              Dot.PortAlignment.WEST,
              Dot.EdgeStyle.DOTTED));
      }
    }
    return output.toString();
  }
}
