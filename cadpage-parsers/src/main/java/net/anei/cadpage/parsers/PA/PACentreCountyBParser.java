package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PACentreCountyBParser extends FieldProgramParser {
  
  public PACentreCountyBParser() {
    super("CENTRE COUNTY", "PA", 
          "( SELECT/1 Box:BOX_CALL_ADDR! Due:UNIT? Name:NAME " + 
          "| Box:BOX! ( CALL_ADDR/Z! END | CALL PLACE? ADDRCITY! Name:NAME? MAP END ) )");
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
    int pt = body.indexOf(" http://");
    if (pt >= 0) body = body.substring(0, pt).trim();
    body = stripFieldEnd(body, ".");
    if (body.contains("\n")) {
      setSelectValue("2");
      return parseFields(body.split("\n"), data);
    }
    
    else {
      setSelectValue("1");
      return super.parseMsg(body, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("BOX_CALL_ADDR")) return new MyBoxCallAddressField();
    if (name.equals("CALL_ADDR")) return new MyCallAddressField();
    if (name.equals("NAME")) return new MyNameField();
    return super.getField(name);
  }
  
  private static final Pattern BOX_PTN = Pattern.compile("(\\d{3,}) +");
  private class MyBoxCallAddressField extends MyCallAddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = BOX_PTN.matcher(field);
      if (match.lookingAt()) {
        data.strBox = match.group(1);
        field = field.substring(match.end());
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "BOX " + super.getFieldNames();
    }
  }
  
  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");
  private static final Pattern ADDR_MAP_PTN = Pattern.compile("(.*)([NSEW][NSEW]? SECTOR)");
  private static final Pattern CALL_ADDR_PTN1 = Pattern.compile("(.*- ?[AB]LS(?: Urgent)?) *(.*)");
  private static final Pattern CALL_ADDR_PTN2 = Pattern.compile("(.*?) ([^a-z,]*)");
  private static final Pattern STATION_PTN = Pattern.compile("(STATION \\d{1,2}) +(.*)");
  private class MyCallAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = MBLANK_PTN.matcher(field).replaceAll(" ");
      Matcher match = ADDR_MAP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strMap = match.group(2);
      }
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        data.strCity = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      match = CALL_ADDR_PTN1.matcher(field);
      if (!match.matches()) {
        match = CALL_ADDR_PTN2.matcher(field);
        if (!match.matches()) abort();
      }
      data.strCall = match.group(1).trim();
      String addr = match.group(2).trim();
      String prefix = "";
      match = STATION_PTN.matcher(addr);
      if (match.lookingAt()) {
        prefix = match.group(1);
        addr = match.group(2);
      }
      parseAddress(StartType.START_PLACE, FLAG_ANCHOR_END, addr.replace('@',  '&'), data);
      data.strPlace = append(prefix, " ", data.strPlace);
      if (data.strAddress.length() == 0) {
        data.strAddress = data.strPlace;
        data.strPlace = "";
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CALL PLACE ADDR APT CITY MAP";
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
    "BALD EAGLE",
    "BELL HOLLOW",
    "BIG OAK",
    "BRUSH VALLEY",
    "BUFFALO RUN",
    "BULLIT RUN",
    "BUSH HOLLOW",
    "CENTENNIAL HILLS",
    "CURTIN NARROWS",
    "DIX RUN",
    "EAGLE VALLEY",
    "EGYPT HOLLOW",
    "END OF LINE",
    "FLAT ROCK",
    "FOWLER HOLLOW",
    "GOVERNORS PARK",
    "HALFMOON VALLEY",
    "HECKMAN CEMETERY",
    "JACK STRAW",
    "JAMES HILL",
    "LIONS HILL",
    "LITTLE MARSH CREEK",
    "MATCH FACTORY",
    "MEDICAL PARK",
    "MOOSE RUN",
    "PENNS VALLEY",
    "PHILIPSBURG BIGLER",
    "PINE CREEK",
    "PORT MATILDA",
    "PURDUE MOUNTAIN",
    "RABBIT HILL",
    "REESE HOLLOW",
    "RISHEL HILL",
    "ROLLING RIDGE",
    "ROSS HILL",
    "SKYTOP MOUNTAIN",
    "SNO FOUNTAIN",
    "SPORTS CAMP",
    "STEELE HOLLOW",
    "SUMMIT HILL",
    "SUNNYSIDE HOLLOW",
    "SWAMP POODLE",
    "TOW HILL",
    "TURKEY EYE",
    "VALLEY VIEW",
    "VIRGINIA PINE",
    "WEAVER HILL",
    "WHITE PINE"
  };
}
