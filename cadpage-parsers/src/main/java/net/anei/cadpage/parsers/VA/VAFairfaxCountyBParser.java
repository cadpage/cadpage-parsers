package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VAFairfaxCountyBParser extends FieldProgramParser {

  public VAFairfaxCountyBParser() {
    super(CITY_CODES, "FAIRFAX COUNTY", "VA",
          "LOCATION:ADDR/S? EVENT_TYPE:CALL! EVENT_#:ID! FIRE_BOX:BOX! TALKGROUP:CH% Disp:UNIT");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Information")) return false;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      while (true) {
        int pt = field.lastIndexOf(':');
        if (pt < 0) pt = field.lastIndexOf(',');
        if (pt < 0) break;
        char del = field.charAt(pt);
        String tmp = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        if (del == ',') {
          apt = append(tmp, "-", apt);
        } else {
          tmp = stripFieldStart(tmp, "@");
          if (tmp.startsWith("btwn")) {
            data.strCross = append(tmp.substring(4).trim(), " / ", data.strCross);
          } else {
            data.strPlace = append(tmp, " - ", data.strPlace);
          }
        }
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCode = field;
      data.strCall = convertCodes(field, CALL_CODES);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "FBLDG",  "BUILDING FIRE",
      "FHOU",   "HOUSE FIRE",
      "FHIRIS", "HIGHRISE FIRE",
      "FGAPT",  "GARDEN APARTMENT FIRE",
      "FTH",    "TOWNHOUSE FIRE",
      "FHOSP",  "HOSPITAL FIRE",
      "HAZMAT", "HAZARDOUS MATERIAL INCIDENT",
      "CAVEIN", "STRUCTURAL COLLAPSE",
      "EXPLOF", "EXPLOSION",
      "METROF", "FIRE ON METRO SYSTEM",
      "FTANKF", "TANKER FIRE",
      "FJAILF", "JAIL FIRE",
      "RESCUE", "TECHNICAL RESCUE",
      "ACCIMF", "ACCIDENT WITH VEHICLE ON FIRE",
      "FLSLG",  "LARGE SPILL",
      "FNH",    "FIRE IN NURSING HOME",
      "RSWIFT", "SWIFT WATER RESCUE (MOVING WATER)",
      "RIVERF", "FLAT WATER RESCUE"
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALEX",          "ALEXANDRIA",
      "ALEX CITY",     "ALEXANDER",
      "ANDL",          "ANNANDALE",
      "ARCO",          "ARLINGTON COUNTY",
      "ARLN",          "ARLINGTON",
      "BRKE",          "BURKE",
      "CENT",          "CENTREVILLE",
      "CHAN",          "CHANTILLY",
      "CLFT",          "CLIFTON",
      "DLLS",          "DULLES",
      "DUNN",          "DUNN LORING",
      "FLCH",          "FALLS CHURCH",
      "FRFX CITY",     "FAIRFAX CITY",
      "FRFX",          "FAIRFAX",
      "FTBV",          "FORT BELVOIR",
      "FXST",          "FAIRFAX STATION",
      "GTFL",          "GREAT FALLS",
      "HRND",          "HERNDON",
      "LOCO",          "LOUDOUN",
      "LRTN",          "LORTON",
      "MCLN",          "MCLEAN",
      "MOCO",          "MONTGOMERY COUNTY",
      "OKTN",          "OAKTON",
      "PGCO",          "PRINCE GEORGE COUNTY",
      "PWC",          "PRINCE WILLIAM COUNTY",
      "RSTN",          "RESTON",
      "SFLD",          "SPRINGFIELD",
      "VNNA",          "VIENNA",
      "VNNA TOWN",     "VIENNA"
  });
}
