package net.anei.cadpage.parsers.ZSE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZSESwedenParser extends FieldProgramParser {

  public ZSESwedenParser() {
    super("", "", CountryCode.SE,
    "( ID CALL CALL CALL! Händelsebeskrivning:INFO? INFO2? ADDR CITY! SRC Larmkategori_namn:PRI? PositionWGS84:GPS! Stationskod:SKIP? Larmkategori_namn:PRI? Händelsebeskrivning:INFO INFO+ " + 
    "| CALL CALL CALL ADDR CITY ( GPS/Z END | INFO+? SRC UNIT! UNIT/S+? GPS ) ) END");
  }
  
  @Override
  public String getFilter() {
    return "3315,@zenit.sosalarm.se";
  }

  @Override
  public String getLocName() {
    return "Sweden";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!parseFields(body.split("\n"), 6, data)) return false;
    if (data.strCity.equals("-")) data.strCity = "";
    if (data.strPlace.equals(data.strCity)) data.strPlace = "";
    return true;
  };
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d+");
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("INFO2")) return new ExtraInfoField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("SRC")) return new SourceField("\\d{3}-\\d{4}");
    if (name.equals("UNIT")) return new UnitField("\\d{3}-\\d{4}|MRSYDLB|MRSYDIB");
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }
  
  // Sometimes the info field is split by a newline leaving a fragment that we have to
  // identify and merge back into the original info field. Detecting this situation is
  // a bit tricky.  Best bet is to look ahead to see if the source field is what it should
  // be if this is an extra field
  private static final Pattern INFO_SRC_PTN = Pattern.compile("D\\d+");
  private class ExtraInfoField extends MyInfoField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strSupp.length() == 0) return false;
      String src = getRelativeField(3);
      if (!INFO_SRC_PTN.matcher(src).matches()) return false;
      parse(field, data);
      return true;
    }
  }
  
  private static final Pattern INFO_CHANNEL_PTN = Pattern.compile("\\bRAPS\\b", Pattern.CASE_INSENSITIVE);
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_CHANNEL_PTN.matcher(field).find()) {
        data.strChannel = append(data.strChannel, " / ", field);
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CH INFO";
    }
  }
  
  private static final Pattern APT_MARKER = Pattern.compile("(?: Nr(?= |\\d)|,) *(\\d+[A-Z]?\\b)? *(.*)$");
  private static final Pattern PROPERTY_MARKER = Pattern.compile("\\w+:.*");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_MARKER.matcher(field);
      if (match.find()) {
        field = field.substring(0,match.start()).trim();
        data.strApt = getOptGroup(match.group(1));
        String place = match.group(2);
        if (place.startsWith(";")) place = place.substring(1).trim();
        if (PROPERTY_MARKER.matcher(place).matches()) {
          data.strSupp = place;
        } else {
          data.strPlace = place;
        }
      }
      data.strAddress = field;
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE INFO";
    }
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("La = (\\d+)(?:[^\\p{ASCII}]+| grader) ([\\d\\.,]+)'([NS]) +Lo = (\\d+)(?:[^\\p{ASCII}]+| grader) ([\\d\\.,]+)'([EW])");
  private class MyGPSField extends GPSField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      
      if (field.length() == 0) return true;
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) return false;
      
      String gpsLoc = (match.group(3).charAt(0) == 'S' ? "-" : "+") + match.group(1) + ' ' + match.group(2) + ' ' +
                      (match.group(6).charAt(0) == 'W' ? "-" : "+") + match.group(4) + ' ' + match.group(5);
      gpsLoc = gpsLoc.replace(',', '.');
      setGPSLoc(gpsLoc, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
