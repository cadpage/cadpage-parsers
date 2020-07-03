package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Ulster County, NY
 */
public class NYUlsterCountyParser extends FieldProgramParser {

  public NYUlsterCountyParser() {
    super(CITY_LIST, "ULSTER COUNTY", "NY",
          "Inc:CALL! Lvl:PRI? Loc:ADDRCITY! Additional:INFO! Int:X! Common:PLACE/SDS! Nature:CALL/SDS? Original_Call_Time:DATETIME END");
  }

  @Override
  public String getFilter() {
    return "dispatch@co.ulster.ny.us,777";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("Dispatch")) return false;

    body = body.replace("\n", "");
    if (!super.parseMsg(body, data)) return false;

    data.strCity = data.strCity.replaceAll(" +", " ");
    if (data.strCity.toUpperCase().startsWith("KING CITY")) data.strCity="KINGSTON";
    else if (data.strCity.equalsIgnoreCase("Out of Cty")) data.strCity = "";
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    if  (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "KINGSTON",

      // Towns
      "DENNING",
      "ESOPUS",
      "GARDINER",
      "HARDENBURGH",
      "HURLEY",
      "KINGSTON",
      "LLOYD",
      "MARBLETOWN",
      "MARLBOROUGH",
      "NEW PALTZ",
      "OLIVE",
      "PLATTEKILL",
      "ROCHESTER",
      "ROSENDALE",
      "SAUGERTIES",
      "SHANDAKEN",
      "SHAWANGUNK",
      "ULSTER",
      "WAWARSING",
      "WOODSTOCK",

      // Villages
      "ELLENVILLE",
      "NEW PALTZ",
      "SAUGERTIES",

      // Census-designated places
      "ACCORD",
      "CLINTONDALE",
      "CRAGSMOOR",
      "EAST KINGSTON",
      "GARDINER",
      "GLASCO",
      "HIGH FALLS",
      "HIGHLAND",
      "HILLSIDE",
      "HURLEY",
      "KERHONKSON",
      "LAKE KATRINE",
      "LINCOLN PARK",
      "MALDEN-ON-HUDSON",
      "MARLBORO",
      "MILTON",
      "NAPANOCH",
      "PHOENICIA",
      "PINE HILL",
      "PLATTEKILL",
      "PORT EWEN",
      "RIFTON",
      "ROSENDALE HAMLET",
      "SAUGERTIES SOUTH",
      "SHOKAN",
      "STONE RIDGE",
      "TILLSON",
      "WALKER VALLEY",
      "WALLKILL",
      "WATCHTOWER",
      "WEST HURLEY",
      "WOODSTOCK",
      "ZENA",

      // Hamlets
      "BEARSVILLE",
      "BIG INDIAN",
      "BOICEVILLE",
      "BROWNS STATION",
      "CENTERVILLE",
      "CHICHESTER",
  };
}
