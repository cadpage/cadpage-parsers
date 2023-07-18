package net.anei.cadpage.parsers.CT;


import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Middlesex County, CT
 */

public class CTMiddlesexCountyAParser extends FieldProgramParser {

  private static final Pattern SUBJECT_PTN = Pattern.compile("(?:(.+?) +CALL +)?CAD Page for CFS (\\d{6,}-\\d+)");

  public CTMiddlesexCountyAParser() {
    super(CITY_CODES, "MIDDLESEX COUNTY", "CT",
          "GPS? CALL! ADDR! Apt:APT! CITY! Cross_Streets:X? Caller:NAME Disp_Time:DATETIME% EMPTY+? GPS");
  }

  @Override
  public String getFilter() {
    return ".sbc.mail.gq1.yahoo.com,administrator@valleyshore911.org,cad@valleyshore911-lists.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strSource = getOptGroup(match.group(1));
    data.strCallId = match.group(2);
    if (parseFields(body.split("\n"), 5, data)) return true;
    String src = data.strSource;
    String callId = data.strCallId;
    data.parseGeneralAlert(this, body);
    data.strSource = src;
    data.strCallId = callId;
    return true;
  }

  @Override
  public String getProgram() {
    return "SRC ID " + super.getProgram() + " INFO";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private static final Pattern GPS_PATTERN = Pattern.compile("http://maps\\.google\\.com/maps\\?q=([-+]*\\d+\\.\\d+ +[-+]*\\d+\\.\\d+)");
  private class MyGPSField extends GPSField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      field = field.replace("%20", " ");
      Matcher match = GPS_PATTERN.matcher(field);
      if (!match.matches()) return false;
      String gps =  match.group(1);
      if (!gps.equals("+-1.00000 --1.00000")) super.parse(match.group(1), data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern MCVEAGH_PTN = Pattern.compile("\\bMC VEAGH\\b", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      while (field.startsWith("U:")) field = field.substring(2).trim();
      field = MCVEAGH_PTN.matcher(field).replaceAll("MCVEAGH");
      super.parse(field, data);
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field.toUpperCase(), data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" * ", " & ");
      super.parse(field, data);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AV", "AVON",
      "BA", "BETHANY",
      "BE", "BERLIN",
      "BR", "BRANFORD",
      "CH", "CHESTER",
      "CL", "CLINTON",
      "CO", "COLCHESTER",
      "CR", "CROMWELL",
      "CS", "CHESHIRE",
      "DH", "DURHAM",
      "DR", "DEEP RIVER",
      "EF", "EAST HARTFORD",
      "EH", "EAST HADDAM",
      "EL", "EAST LYME",
      "EM", "EAST HAMPTON",
      "EV", "EAST HAVEN",
      "GL", "GLASTONBURY",
      "GR", "GROTON",
      "GU", "GUILFORD",
      "HD", "HADDAM",
      "HE", "HEBRON",
      "HF", "HARTFORD",
      "HN", "HADDEN NECK",
      "KW", "KILLINGWORTH",
      "LI", "LONG ISLAND",
      "LY", "LYME",
      "MA", "MADISON",
      "MB", "MARLBOROUGH",
      "MD", "MIDDLETOWN",
      "ME", "MERIDEN",
      "MF", "MIDDLEFIELD",
      "MI", "MIDDLEBURY",
      "ML", "MILFORD",
      "MO", "BANFORD",   // ????
      "MV", "MONTVILLE",
      "NB", "NEW BRANFORD",
      "NE", "NEWINGTON",
      "NH", "NEW HAVEN",
      "NK", "NORWALK",
      "NL", "NEW LONDON",
      "NO", "NORWICH",
      "NV", "NORTH HAVEN",
      "NW", "NEW BRITAIN",
      "NY", "NEW YORK CITY",
      "OL", "OLD LYME",
      "OS", "OLD SAYBROOK",
      "OR", "ORANGE",
      "PL", "PORTLAND",
      "RH", "ROCKY HILL",
      "SA", "SALEM",
      "SO", "SOUTHINGTON",
      "ST", "STONINGTON",
      "SX", "ESSEX",
      "WA", "WALLINGFORD",
      "WB", "WESTBROOK",
      "WD", "WALLINGFORD",  // ???
      "WE", "WEATHERSFIELD",
      "WF", "WATERFORD",
      "WH", "WEST HARTFORD",
      "WO", "WOODBRIDGE",
      "WP", "WESTPORT",
      "WT", "WATERBURY",
      "WV", "WEST HAVEN",
  });
}
