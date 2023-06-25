package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


/**
 * Ontario County, NY
 */
public class NYOntarioCountyAParser extends FieldProgramParser {

  public NYOntarioCountyAParser() {
    super(CITY_FIX_TABLE, "ONTARIO COUNTY", "NY",
          "INFO? CALL TIME ADDR/S6 X UNIT! INFO/N ID");
    setupCities(CITY_LIST);
    setupProtectedNames("5 AND 20");
  }

  @Override
  public String getFilter() {
    return "E911page@co.ontario.ny.us";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (data.strSupp.startsWith("Cancel Reason:") ||
          data.strSupp.contains("CANCEL") ||
          data.strSupp.contains("STAND DOWN")) {
        field = "CANCEL - " + field;
      } else if (data.strSupp.equals("RE-ALERT!")) {
        field = "RE-ALERT - " + field;
        data.strSupp = "";
      } else if (data.strSupp.equalsIgnoreCase("NO RESPONSE")) {
        field = "NO RESPONSE - " + field;
        data.strSupp = "";
      }
      super.parse(field, data);
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "***");
      int pt = field.indexOf(',');
      if (pt >= 0) {
        String part1 = field.substring(0,pt).trim();
        String part2 = field.substring(pt+1).trim();
        if (isCity(part2)) {
          field = part1;
          data.strCity = part2.replace(".", "");
        } else {
          data.strPlace = part1;
          field = field.substring(pt+1).trim();
        }
      }
      super.parse(field.replace(" - ", " & "), data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replaceAll(" - ", " & ");
      if (field.endsWith("-")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = PK_PTN.matcher(addr).replaceAll("PARK");
    addr = RT_5_20_PTN.matcher(addr).replaceAll("5");
    addr = RT_5_21_PTN.matcher(addr).replaceAll("$1");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern PK_PTN = Pattern.compile("\\bPK\\b");
  private static final Pattern RT_5_20_PTN = Pattern.compile("\\b5 (?:AND|&) 20\\b");
  private static final Pattern RT_5_21_PTN = Pattern.compile("(?:RT |ROUTE |\\b)(?:5|20)(?: *& *)((?:RT|ROUTE) *21)\\b");

  private static final Properties CITY_FIX_TABLE = buildCodeTable(new String[]{
      "ATLANTA T-COHOCTON",   "ATLANTA",
      "TOWN OF PERINTON",     "PERINTON",
      "T-PERINTON",           "PERINTON"
  });

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "CANANDAIGUA",
      "GENEVA",

      // Towns
      "BRISTOL",
      "CANADICE",
      "CANANDAIGUA",
      "E BLOOMFIELD",
      "EAST BLOOMFIELD",
      "FARMINGTON",
      "GENEVA",
      "GORHAM",
      "HOPEWELL",
      "MANCHESTER",
      "NAPLES",
      "PHELPS",
      "RICHMOND",
      "SENECA",
      "SOUTH BRISTOL",
      "VICTOR",
      "WEST BLOOMFIELD",

      // Villages
      "BLOOMFIELD",
      "CLIFTON SPRINGS",
      "MANCHESTER",
      "NAPLES",
      "PHELPS",
      "RUSHVILLE",
      "SHORTSVILLE",
      "VICTOR",

      // Census-designated places
      "CRYSTAL BEACH",
      "GORHAM",
      "HALL",
      "HONEOYE",
      "PORT GIBSON",

      // Hamlets
      "CENTERFIELD",
      "CHESHIRE",
      "FISHERS",
      "HALL",
      "HOPEWELL CENTER",
      "STANLEY",

      // Livingston County
      "LIVONIA",

      // Monroe County
      "PERINTON",

      // Steuben County
      "ATLANTA"
  };
}


