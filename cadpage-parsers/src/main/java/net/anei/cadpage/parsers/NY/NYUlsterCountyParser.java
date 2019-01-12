package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Ulster County, NY
 */
public class NYUlsterCountyParser extends FieldProgramParser {

  public NYUlsterCountyParser() {
    super(CITY_LIST, "ULSTER COUNTY", "NY",
          "Inc:CALL! Loc:ADDR_CITY_X! Common:PLACE/SDS! Nature:CALL/SDS? Original_Call_Time:DATETIME");
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
    if (name.equals("ADDR_CITY_X")) return new MyAddressCityCrossField();
    if  (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private static final Pattern ADDR_DELIM_PTN = Pattern.compile(" *, +");
  private static final Pattern NAME_PHONE_PTN = Pattern.compile("(.*) (\\d{10})");
  private class MyAddressCityCrossField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      String[] parts = ADDR_DELIM_PTN.split(field);
      super.parse(parts[0], data);
      if (parts.length > 1) {
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, parts[1], data);
        String left = getLeft();
        if (left.length() > 0) {
          Matcher match = NAME_PHONE_PTN.matcher(left);
          if (match.matches()) {
            data.strName = match.group(1).trim();
            data.strPhone = match.group(2);
          } else if (left.contains("/")) {
            data.strCross = left;
          } else if (left.contains(",")) {
            data.strName = left;
          } else {
            data.strPlace = left;
          }
        }
      }
      for (int ndx = 2; ndx < parts.length; ndx++) {
        data.strCross = append(data.strCross, ", ", parts[ndx]);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY NAME PHONE PLACE X";
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
