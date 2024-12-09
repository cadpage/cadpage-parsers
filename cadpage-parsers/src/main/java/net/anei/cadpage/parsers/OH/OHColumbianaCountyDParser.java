package net.anei.cadpage.parsers.OH;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHColumbianaCountyDParser extends FieldProgramParser {

  public OHColumbianaCountyDParser() {
    super(CITY_LIST, "COLUMBIANA COUNTY", "OH",
          "CODE_CALL ADDR PLACE? CITY/Y! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "sunsrv@sundance-sys.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("From: ELPD_Map")) return false;
    if (!parseFields(body.split(","), data)) return false;
    data.strCity = convertCodes(data.strCity, MISSPELLED_CITIES);
    if (WV_CITIES.contains(data.strCity)) data.strState = "WV";
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    return super.getField(name);
  }

  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf('-');
      if (pt < 0) abort();
      data.strCode = field.substring(0, pt).trim();
      data.strCall = field.substring(pt+1).trim();
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "COLUMBIANA",
      "EAST LIVERPOOL",
      "EAST LIVEPOOL",     // Misspelled
      "SALEM",
      "VILLAGES",
      "EAST PALESTINE",
      "HANOVERTON",
      "LEETONIA",
      "LISBON",
      "MINERVA",
      "NEW WATERFORD",
      "ROGERS",
      "SALINEVILLE",
      "SUMMITVILLE",
      "WASHINGTONVILLE",
      "WELLSVILLE",

      // Townships
      "BUTLER",
      "CENTER",
      "ELKRUN",
      "FAIRFIELD",
      "FRANKLIN",
      "HANOVER",
      "KNOX",
      "LIVERPOOL",
      "MADISON",
      "MIDDLETON",
      "PERRY",
      "SALEM",
      "ST CLAIR",
      "UNITY",
      "WASHINGTON",
      "WAYNE",
      "WEST",
      "YELLOW CREEK",

      // Census-designated places
      "CALCUTTA",
      "DAMASCUS",
      "EAST ROCHESTER",
      "GLENMOOR",
      "HOMEWORTH",
      "LA CROFT",
      "LAKE TOMAHAWK",
      "NEGLEY",
      "SALEM HEIGHTS",

      // Unincorporated communities
      "ACHOR",
      "BAYARD",
      "CANNONS MILL",
      "CHAMBERSBURG",
      "CLARKSON",
      "DUNGANNON",
      "EAST CARMEL",
      "EAST FAIRFIELD",
      "ELKTON",
      "FRANKLIN SQUARE",
      "FREDERICKTOWN",
      "GAVERS",
      "GLASGOW",
      "GUILFORD",
      "HIGHLANDTOWN",
      "KENSINGTON",
      "LYNCHBURG",
      "MIDDLETON",
      "MILL ROCK",
      "MILLPORT",
      "MOULTRIE",
      "NEW ALEXANDER",
      "NEW GARDEN",
      "NEW MIDDLETON",
      "NEW SALISBURY",
      "NORTH GEORGETOWN",
      "READING",
      "SIGNAL",
      "TEEGARDEN",
      "UNIONVILLE",
      "UNITY",
      "VALLEY",
      "WEST POINT",
      "WILLIAMSPORT",
      "WINONA",

      // Jefferson County
      "TORONTO",

      // Hancock County, WV
      "NEW CUMBERLAND",

      ""
  };

  private static final Properties MISSPELLED_CITIES = buildCodeTable(new String[] {
      "LIVEPOOL",     "LIVERPOOL"
  });

  private static final Set<String> WV_CITIES = new HashSet<>(Arrays.asList("NEW CUMBERLAND"));
}
