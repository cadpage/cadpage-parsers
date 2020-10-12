package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class PABedfordCountyParser extends DispatchH05Parser {
  
  public PABedfordCountyParser() {
    super("BEDFORD COUNTY", "PA", 
          "BEDFORD_COUNTY%EMPTY MASH1 MASH2 ( GPS1 GPS2 | ) INFO_BLK+? TIMES+? GMAP!");
    setAccumulateUnits(true);
    setupProtectedNames("W & W");
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "CADnoreply@bedfordcountypa.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("MASH1")) return new MyMash1Field();
    if (name.equals("MASH2")) return new MyMash2Field();
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("GMAP")) return new SkipField("https://www.google.com/maps.*", true);
    return super.getField(name);
  }
  
  private static final Pattern MASH1_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d) (?:(.*) )?CFS: \\d+ +(.*)");
  private static final Pattern NAME_PHONE_PTN = Pattern.compile("(.*?) *(\\(\\d{3}\\) ?\\d{3}-\\d{4})");
  private class MyMash1Field extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = MASH1_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      String name = getOptGroup(match.group(3));
      data.strCall = match.group(4);
      
      match = NAME_PHONE_PTN.matcher(name);
      if (match.matches()) {
        name = match.group(1);
        data.strPhone = match.group(2);
      }
      data.strName = name;
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME NAME PHONE CALL";
    }
  }
  
  private static final Pattern MASH2_PTN = Pattern.compile("(.*?)(\\[.*\\])(.*)");
  private class MyMash2Field extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = MASH2_PTN.matcher(field);
      if (!match.matches()) abort();
      super.parse(match.group(1).trim(), data);
      data.strCallId = cleanIdField(match.group(2));
      parseAddress(StartType.START_PLACE, FLAG_ONLY_CROSS, match.group(3).trim(), data);
    }
    
    private String cleanIdField(String field) {
      field = field.replace("[", "").replace("]",  "");
      StringBuilder sb = new StringBuilder();
      for (String id : field.split(",")) {
        id = id.trim();
        if (id.startsWith("Incident not yet created")) continue;
        int pt = id.indexOf(' ');
        if (pt >= 0) id = id.substring(0,pt);
        if (sb.length() > 0) sb.append(", ");
        sb.append(id);
      }
      return sb.toString();
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ID PLACE X";
    }
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2}\\.\\d{6,}");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type, GPS_PTN, true);
    }
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "BARNETTS RUN",
      "BLACK BEAR",
      "BLACK OAK",
      "BUCK VALLEY",
      "CAMP GROUND",
      "COON HOLLOW",
      "FOX SQUIRREL",
      "FRANKLIN MILLS",
      "GEM BRIDGE",
      "GREAT COVE",
      "GREEN LANE",
      "HONEY ROCK",
      "LAUREL RIDGE",
      "LOCUST GROVE",
      "MCKEES GAP",
      "MILL HILL",
      "PIGEON COVE",
      "PLEASANT GROVE",
      "PLEASANT RIDGE",
      "QUARRY HILL",
      "SHADED ACRES",
      "SIPES MILL",
      "SPOTTED FAWN",
      "TALL SPRUCE",
      "TIMBER RIDGE",
      "WENDING WAY",
      "WOLF HOLLOW"
  };
}
