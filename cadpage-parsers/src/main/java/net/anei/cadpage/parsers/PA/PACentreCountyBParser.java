package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PACentreCountyBParser extends FieldProgramParser {
  
  public PACentreCountyBParser() {
    super("CENTRE COUNTY", "PA", "Box:BOX_CALL_ADDR! Name:NAME!");
    setupMultiWordStreets(MWORD_STREET_LIST);
    removeWords("TWP");
  }
  
  @Override
  public String getFilter() {
    return "alerts@centre.ealert911.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Centre County Alerts")) return false;
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("BOX_CALL_ADDR")) return new MyBoxCallAddressField();
    if (name.equals("NAME")) return new MyNameField();
    return super.getField(name);
  }
  
  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");
  private static final Pattern BOX_CALL_ADDR_PTN = Pattern.compile("(?:(\\d{3,}) )?(.*?) (?![A-Z]+-[AB]LS\\b)([^a-z,]*)(?:, *([^,]*))?");
  private class MyBoxCallAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = MBLANK_PTN.matcher(field).replaceAll(" ");
      Matcher match = BOX_CALL_ADDR_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strBox = getOptGroup(match.group(1));
      data.strCall = match.group(2).trim();
      parseAddress(StartType.START_PLACE, FLAG_ANCHOR_END, match.group(3).trim().replace('@',  '&'), data);
      if (data.strAddress.length() == 0) {
        data.strAddress = data.strPlace;
        data.strPlace = "";
      }
      data.strCity = getOptGroup(match.group(4));
    }
    
    @Override
    public String getFieldNames() {
      return "BOX CALL PLACE ADDR APT CITY";
    }
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      super.parse(field, data);
    }
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "BIG OAK",
    "DIX RUN",
    "EGYPT HOLLOW",
    "END OF LINE",
    "FLAT ROCK",
    "GOVERNORS PARK",
    "HALFMOON VALLEY",
    "JAMES HILL",
    "LITTLE MARSH CREEK",
    "MATCH FACTORY",
    "MOOSE RUN",
    "PURDUE MOUNTAIN",
    "RABBIT HILL",
    "ROLLING RIDGE",
    "SPORTS CAMP",
    "STEELE HOLLOW",
    "SUMMIT HILL",
    "SWAMP POODLE",
    "TOW HILL",
    "TURKEY EYE",
    "VALLEY VIEW",
    "WHITE PINE"
  };
}
