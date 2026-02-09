package net.anei.cadpage.parsers.DC;


import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Proteanhub, DC
 */

public class DCProteanhubParser extends FieldProgramParser {

  public DCProteanhubParser() {
    super("DC", "",
          "CAD:ID? CALL:CALL! Sending_Facility:FACILITY? LOCATION:PLACE? " +
                "( ADDR:ADDR! CITY:CITY! ST:ST? CNTY:CITY? Call_Back_Number:PHONE? GPS:GPS? PRI:PRI! UNIT:UNIT! " +
//                "| PRI:PRI? UNIT:UNIT? " +
                ") INFO/N+", FLDPROG_IGNORE_CASE);
  }

  @Override
  public String getFilter() {
    return "donotreply@proteanhub.com,FlightVectorSvc@childrensnational.org";
  }

  @Override
  public String getLocName() {
    return "Protean Hub, DC";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern MISSING_NL_PTN = Pattern.compile(" (?=ADDR:|CITY:|ST:|CNTY:|GPS:)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("Alert") &&
        !subject.startsWith("Z - Active 911 Page") &&
        !subject.equals("FlightVector")) return false;
    int pt = body.indexOf("\n\nConfidentiality Notice:");
    if (pt >= 0) body = body.substring(0,pt).trim();
    body = body.replace('\t', ' ');
    body = MISSING_NL_PTN.matcher(body).replaceAll("\n");
    return parseFields(body.split("\n+"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("FACILITY")) return new MyFacilityField();
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }

  private class MyFacilityField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = "Sending Facility: " + field;
      super.parse(field, data);
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field, data);
    }
  }
}


