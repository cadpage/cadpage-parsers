package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Craven County, NC
 */
public class NCCravenCountyCParser extends FieldProgramParser {

  public NCCravenCountyCParser() {
    super(CITY_LIST, "CRAVEN COUNTY", "NC",
          "Location:ADDR/S! OCA:CALL! Narrative:INFO!");
  }

  @Override
  public String getFilter() {
    return "dispatch@cravencountync.gov,dispatch1@cravencountync.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CC911")) return false;
    if (super.parseMsg(body, data)) return true;
    setFieldList("INFO");
    data.parseGeneralAlert(this, body);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }

  private static final Pattern ADDR_GPS_PTN = Pattern.compile("\\s*(?:(\\b3[45]\\.\\d{4,} *-7[67]\\.\\d{4,})|-361 +-361)$");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_GPS_PTN.matcher(field);
      if (match.find()) {
        String gps = match.group(1);
        if (gps != null) setGPSLoc(gps, data);
        field = field.substring(0,match.start());
      }
      super.parse(field,  data);
      if (data.strCity.equalsIgnoreCase("Jones")) data.strCity = "Jones County";
      if (data.strCity.equalsIgnoreCase("Pamlico")) data.strCity = "Pamlico County";
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " GPS";
    }
  }

  private static final Pattern CALL_ID_PTN = Pattern.compile("(.*?) +(\\d{4}-\\d{8}\\b.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_ID_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strCallId = match.group(2);

        int pt = field.indexOf(' ');
        if (pt >= 0) {
          int pt2 = field.indexOf(' ' + field.substring(0,pt), pt);
          if (pt2 >= 0) field = field.substring(0,pt2).trim();
        }
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CALL ID";
    }
  }


  private static final String[] CITY_LIST = new String[]{
    "BRICES CREEK",
    "BRIDGETON",
    "COVE CITY",
    "DOVER",
    "ERNUL",
    "FAIRFIELD HARBOUR",
    "HAVELOCK",
    "JAMES CITY",
    "NEUSE FOREST",
    "NEW BERN",
    "RIVER BEND",
    "TRENT WOODS",
    "VANCEBORO",
    "EMUL",
    "HARLOWE",
    "CHERRY POINT",
    "CHERRY BRANCH",
    "ADAMS CREEK",
    "FORT BARNWELL",

    "JONES",   // Jones County
    "PAMLICO", // Pamlico County

    // Carteret County
    "CARTERET",

    // Lenoir County
    "GRIFTON"
  };

}
