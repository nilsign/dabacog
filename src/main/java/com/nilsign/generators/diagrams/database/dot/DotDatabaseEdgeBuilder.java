package com.nilsign.generators.diagrams.database.dot;

import com.nilsign.dxd.model.DxdFieldRelation;
import com.nilsign.generators.database.SqlSchemaGenerator;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE, staticName = "of")
public class DotDatabaseEdgeBuilder {

  public static String buildEntityRelationEdge(@NonNull DxdFieldRelation dxdRelation) {
    switch(dxdRelation.getType()) {
      case MANY_TO_MANY: return of().buildManyToManyEdges(dxdRelation);
      case MANY_TO_ONE: return of().buildManyToOneEdge(dxdRelation);
      case ONE_TO_MANY: return of().buildOneToManyEdge(dxdRelation);
      case ONE_TO_ONE: return of().buildOneToOneEdge(dxdRelation);
      default: return null;
    }
  }

  private String buildManyToManyEdges(@NonNull DxdFieldRelation dxdRelation) {
    if (!dxdRelation.isManyToMany()) {
      return "";
    }
    return new StringBuffer()
        .append(Dot.addEdge(
            String.format("node_%s", SqlSchemaGenerator.buildTableName(dxdRelation)),
            String.format("port_%s",
                SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getFirst()),
            Dot.PortAlignment.WEST,
            String.format("node_%s",
                SqlSchemaGenerator.buildTableName(dxdRelation.getFirstClass())),
            String.format("port_%s", SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
            Dot.PortAlignment.WEST))
        .append(Dot.addEdge(
            String.format("node_%s", SqlSchemaGenerator.buildTableName(dxdRelation)),
            String.format("port_%s",
                SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getSecond()),
            Dot.PortAlignment.EAST,
            String.format("node_%s",
                SqlSchemaGenerator.buildTableName(dxdRelation.getSecondClass())),
            String.format("port_%s", SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
            Dot.PortAlignment.WEST))
        .toString();
  }

  private String buildOneToManyEdge(@NonNull DxdFieldRelation dxdRelation) {
    if (!dxdRelation.isOneToMany()) {
      return "";
    }
    return Dot.addEdge(
        String.format("node_%s",
            SqlSchemaGenerator.buildTableName(dxdRelation.getFirstClass())),
        String.format("port_%s",
            SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getSecond()),
        Dot.PortAlignment.EAST,
        String.format("node_%s",
            SqlSchemaGenerator.buildTableName(dxdRelation.getSecondClass())),
        String.format("port_%s", SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
        Dot.PortAlignment.WEST,
        Dot.EdgeStyle.DASHED);
  }

  private String buildManyToOneEdge(@NonNull DxdFieldRelation dxdRelation) {
    if (!dxdRelation.isManyToOne()) {
      return "";
    }
    return Dot.addEdge(
        String.format("node_%s",
            SqlSchemaGenerator.buildTableName(dxdRelation.getSecondClass())),
        String.format("port_%s",
            SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getFirst()),
        Dot.PortAlignment.EAST,
        String.format("node_%s",
            SqlSchemaGenerator.buildTableName(dxdRelation.getFirstClass())),
        String.format("port_%s",
            SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
        Dot.PortAlignment.WEST,
        Dot.EdgeStyle.DASHED);
  }

  private String buildOneToOneEdge(@NonNull DxdFieldRelation dxdRelation) {
    if (!dxdRelation.isOneToOne()) {
      return "";
    }
    if (dxdRelation.isSelfReference()) {
      return Dot.addEdge(
          String.format(
            "node_%s", SqlSchemaGenerator.buildTableName(dxdRelation.getFirstClass())),
          String.format(
              "port_%s", SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getFirst()),
          Dot.PortAlignment.EAST,
          String.format(
              "node_%s", SqlSchemaGenerator.buildTableName(dxdRelation.getSecondClass())),
          String.format("port_%s", Dot.PortLocation.TOP.getShortName()),
          Dot.PortAlignment.NORTH,
          Dot.EdgeStyle.DOTTED);
    }
    StringBuffer output = new StringBuffer();
    if (dxdRelation.isOneToOne()) {
      output.append(Dot.addEdge(
          String.format(
              "node_%s", SqlSchemaGenerator.buildTableName(dxdRelation.getFirstClass())),
          String.format(
              "port_%s", SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getSecond()),
          Dot.PortAlignment.EAST,
          String.format(
              "node_%s", SqlSchemaGenerator.buildTableName(dxdRelation.getSecondClass())),
          String.format("port_%s", SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
          Dot.PortAlignment.WEST,
          Dot.EdgeStyle.DOTTED));
      if (dxdRelation.isBiDirectional()) {
        output.append(Dot.addEdge(
            String.format(
              "node_%s", SqlSchemaGenerator.buildTableName(dxdRelation.getSecondClass())),
              String.format(
                  "port_%s", SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getFirst()),
              Dot.PortAlignment.EAST,
              String.format(
                  "node_%s", SqlSchemaGenerator.buildTableName(dxdRelation.getFirstClass())),
              String.format("port_%s", SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
              Dot.PortAlignment.WEST,
              Dot.EdgeStyle.DOTTED));
      }
    }
//    if (dxdRelation.isBiDirectional()
//        && !dxdRelation.isSelfReference()) {
//      output.append(Dot.addEdge(
//          String.format("node_%s",
//              SqlSchemaGenerator.buildTableName(dxdRelation.getSecondClass())),
//          String.format("port_%s",
//              SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getSecond()),
//          Dot.PortAlignment.EAST,
//          String.format("node_%s",
//              SqlSchemaGenerator.buildTableName(dxdRelation.getSecondClass())),
//          String.format("port_%s",
//              SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
//          Dot.PortAlignment.WEST,
//          Dot.EdgeStyle.DOTTED));
//    }
    return output.toString();
  }
}


//    if (dxdRelation.isSelfReference()) {
//      return Dot.addEdge(
//          String.format(
//            "node_%s", SqlSchemaGenerator.buildTableName(dxdRelation.getFirstClass())),
//          String.format(
//              "port_%s", SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getFirst()),
//          Dot.PortAlignment.EAST,
//          String.format(
//              "node_%s", SqlSchemaGenerator.buildTableName(dxdRelation.getSecondClass())),
//          String.format("port_%s", Dot.PortLocation.TOP.getShortName()),
//          Dot.PortAlignment.NORTH,
//          Dot.EdgeStyle.DOTTED);
//    }
//    StringBuffer output = new StringBuffer();
//    if (!dxdRelation.getFirstField().getRelationType().isOneToOne()) {
//      output.append(Dot.addEdge(
//          String.format(
//              "node_%s", SqlSchemaGenerator.buildTableName(dxdRelation.getFirstClass())),
//          String.format(
//              "port_%s", SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getFirst()),
//          Dot.PortAlignment.EAST,
//          String.format(
//              "node_%s", SqlSchemaGenerator.buildTableName(dxdRelation.getSecondClass())),
//          String.format("port_%s", SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
//          Dot.PortAlignment.WEST,
//          Dot.EdgeStyle.DOTTED));
//    }
//    if (dxdRelation.isBiDirectional()
//        && !dxdRelation.isSelfReference()) {
//      output.append(Dot.addEdge(
//          String.format("node_%s",
//              SqlSchemaGenerator.buildTableName(dxdRelation.getSecondClass())),
//          String.format("port_%s",
//              SqlSchemaGenerator.buildForeignKeyNames(dxdRelation).getSecond()),
//          Dot.PortAlignment.EAST,
//          String.format("node_%s",
//              SqlSchemaGenerator.buildTableName(dxdRelation.getSecondClass())),
//          String.format("port_%s",
//              SqlSchemaGenerator.SQL_PRIMARY_KEY_NAME),
//          Dot.PortAlignment.WEST,
//          Dot.EdgeStyle.DOTTED));
//    }
//    return output.toString();
