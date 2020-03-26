package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Craven County, NC
 */
public class NCCravenCountyDParser extends FieldProgramParser {
  
  public NCCravenCountyDParser() {
    super("CRAVEN COUNTY", "NC", 
          "( Location:ADDRCITY/S! X GPS! OCA:ID! CALL3 PLACE! Dispatched_Units:CH! " +
          "| CALL CALL2! Location:ADDRCITY/S! X GPS! OCA:ID! PLACE " + 
          ") MAP MAP/L UNIT! Narrative:INFO/N+");
    setupMultiWordStreets(MWORD_STREET_LIST);
    removeWords("COUNTY");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@newbernnc.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("[DISP]")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String  name) {
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("CALL3")) return new MyCall3Field();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }
  
  private class MyCall2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strCall)) return;
      data.strCall = append(data.strCall, " / ", field);
    }
  }
  
  private class MyCall3Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = field;
      int pt = data.strCallId.lastIndexOf(field);
      if (pt >= 0) {
        data.strCallId = data.strCallId.substring(pt + field.length()).trim();
      }
    }
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@',  '&');
      super.parse(field, data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Failed ")) return;
      super.parse(field, data);
    }
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("(-?\\d{2}\\.\\d{4,}) ?(-?\\d{2}\\.\\d{4,})");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (field.equals("-361 -361")) return;
      if (field.equals("-361-361")) return;
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) abort();
      super.parse(match.group(1)+','+match.group(2), data);
    }
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      
      "ASHLEY PARK",
      "BEND COLLEGE",
      "BETTYE GRESHAM",
      "CAMDEN SQUARE",
      "CLUB HOUSE",
      "COUNTRY CLUB",
      "COUNTY LINE",
      "EVANS MILL",
      "FAIRWAYS W",
      "MEDICAL PARK",
      "M L KING JR",
      "RED ROBIN",
      "WALT BELLAMY",
      "WEATHERSTONE PARK"

  };
}
