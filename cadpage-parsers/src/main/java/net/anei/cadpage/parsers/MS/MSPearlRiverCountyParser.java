package net.anei.cadpage.parsers.MS;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MSPearlRiverCountyParser extends FieldProgramParser {

  public MSPearlRiverCountyParser() {
    super(CITY_LIST, "PEARL RIVER COUNTY", "MS",
          "CALL ADDRCITY! INFO/N+");
    setupCities(MAP_CITY_TABLE);
  }

  @Override
  public String getFilter() {
    return "Dispatch@PearlRiverMSe911.info";
  }

  private static final Pattern PREFIX = Pattern.compile("CAD #(\\d{8}-\\d{5}):\n");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD DISPATCH")) return false;
    Matcher match = PREFIX.matcher(body);
    if (!match.lookingAt()) return false;
    data.strCallId = match.group(1);
    body = body.substring(match.end());
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private static final Pattern ADDRESS_PTN = Pattern.compile("(.*?)(?: *\\[(.*?)\\])?(?: *\\((.*)\\))?");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "INTERSECTION:");
      Matcher match = ADDRESS_PTN.matcher(field);
      if (!match.matches()) abort(); //  Can't happen
      field = match.group(1);
      data.strBox = getOptGroup(match.group(2));
      String gps = match.group(3);
      if (gps != null) setGPSLoc(gps.trim(), data);

      String[] parts = field.split(" *, *");
      switch (parts.length) {
      case 1:
        field = stripFieldEnd(field, " MS");
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
        break;

      case 2:
        if (!isCity(parts[1]) && isValidAddress(parts[1])) {
          data.strPlace = parts[0];
          parseAddress(parts[1], data);
        } else {
          parseAddress(parts[0], data);
          data.strCity = parts[1];
        }
        break;

      case 3:
        data.strPlace = parts[0];
        parseAddress(parts[1], data);
        data.strCity = parts[2];
        break;

      default:
        abort();
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY BOX GPS";
    }
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "DERBY",          "POPLARVILLE",
      "HILLSDALE",      "LUMBERTON",
      "LEETOWN",        "LUMBERTON",
      "MILL CREEK",     "CARRIERE",
      "NICHOLSON",      "PICAYUNE",
      "PROGRESS",       "POPLARVILLE",
      "RESTERTOWN",     "POPLARVILLE",
      "SALEM",          "PICAYUNE",
      "SONES CHAPEL",   "POPLARVILLE",
      "SYCAMORE",       "CARRIERE",
  });

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "LUMBERTON",
      "PICAYUNE",
      "POPLARVILLE",

      // Census-designated places
      "HIDE-A-WAY LAKE",
      "NICHOLSON",

      // Other unincorporated communities
      "CAESAR",
      "CARRIERE",
      "CROSSROADS",
      "HENLEYFIELD",
      "MCNEILL",
      "OZONA"



  };
}
