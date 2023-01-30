package net.anei.cadpage.parsers.ZCAAB;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZCAABCypressCountyParser extends FieldProgramParser {

  public ZCAABCypressCountyParser() {
    super("CYPRESS COUNTY", "AB",
          "LOC:ADDR! CALL:ID! TYPE:CALL! REM:INFO! INFO/N+ TIME:TIME! ZONE:MAP! LATLONG:GPS! BY:SKIP! END");
  }

  @Override
  public String getFilter() {
    return "cad-noreply@mh911.ca";
  }

  private static final Pattern DELIM = Pattern.compile(",?\n|, *(?=LATLONG:)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new MyCallIdField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d");
    return super.getField(name);
  }

  private class MyCallIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "#");
      super.parse(field, data);
    }
  }
}
