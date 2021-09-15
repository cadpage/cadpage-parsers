package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class PABedfordCountyParser extends DispatchH05Parser {

  public PABedfordCountyParser() {
    super("BEDFORD COUNTY", "PA",
          "( SELECT/NEW CALL ADDRCITY NAME X DATETIME ID UNIT! INFO/N+ " +
          "| SKIP+? BEDFORD_COUNTY MASH1 MASH2 ( GPS1 GPS2 | ) INFO_BLK+? TIMES+? GMAP! )");
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
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (subject.equals("911 Dispatch")) {
      setSelectValue("NEW");
      return parseFields(body.split(" // "), data);
    }

    else {
      setSelectValue("OLD");
      return super.parseHtmlMsg(subject, body, data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("BEDFORD_COUNTY")) return new SkipField("BEDFORD COUNTY|Bedford County", true);
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

      String cross = match.group(3).trim();
      String extra = null;
      int pt = cross.indexOf(",");
      if (pt >= 0) {
        extra = cross.substring(pt+1).trim();
        cross = cross.substring(0, pt).trim();
      }
      parseAddress(StartType.START_PLACE, FLAG_ONLY_CROSS | FLAG_ANCHOR_END, cross, data);
      if (extra != null) data.strCross = append(data.strCross, ", ", extra);
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
      "ADAMS RUN",
      "BACK RUN",
      "BARNETTS RUN",
      "BETHEL CHURCH",
      "BLACK BEAR",
      "BLACK OAK",
      "BLAIRS HILL",
      "BREEZY POINT",
      "BUCK VALLEY",
      "CAMP GROUND",
      "CIDER MILL",
      "CLEAR RIDGE",
      "COON HOLLOW",
      "COVE RUN",
      "COVE VIEW",
      "DENEENS GAP",
      "DIEHL FAMILY",
      "DUNNINGS CREEK",
      "EBENEZER CHURCH",
      "FOX SQUIRREL",
      "FRANKLIN MILLS",
      "GEM BRIDGE",
      "GRANGE HILL",
      "GREAT COVE",
      "GREEN LANE",
      "HEAVENLY ACRES RIDGE",
      "HICKORY GROVE",
      "HIDEAWAY ACRES",
      "HOMINY HILL",
      "HONEY ROCK",
      "INDIAN GRAVE",
      "JIM WEST",
      "LANE METAL",
      "LAUREL RIDGE",
      "LOCUST GROVE",
      "MCKEES GAP",
      "MEADOW MOUNTAIN",
      "MEDICAL CAMPUS",
      "MILL HILL",
      "MOUNTAIN VIEW",
      "PATTERSON RUN",
      "PEACH ORCHARD",
      "PIGEON COVE",
      "PLEASANT GROVE",
      "PLEASANT HOLLOW",
      "PLEASANT RIDGE",
      "POSSUM HOLLOW",
      "QUAKER VALLEY",
      "QUARRY HILL",
      "SHADED ACRES",
      "SIPES MILL",
      "SPOTTED FAWN",
      "TALL SPRUCE",
      "TIMBER RIDGE",
      "WENDING WAY",
      "WINDY HILL",
      "WOLF HOLLOW"
  };
}
