package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MIBarryCountyBParser extends FieldProgramParser {
  
  public MIBarryCountyBParser() {
    super(CITY_LIST, "BARRY COUNTY", "MI", 
          "Location:ADDR! Cross_Streets:X! Use_Caution:CAUTION! Location_Alerts:ALERT/SDS! Zone:MAP? Call_Details:INFO! #:ID!");
  }
  
  @Override
  public String getFilter() {
    return "no-reply@zuercherportal.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Code:")) return false;
    data.strCall = subject.substring(5).trim();
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CAUTION")) return new MyCautionField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern CITY_ST_ZIP_PTN = Pattern.compile("([A-Z ]*), *([A-Z]{2})(?: +(\\d{5}))?\\b *");
  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?\\b *");
  private static final Pattern APT_PLACE_PTN1 = Pattern.compile("(?:APT|LOT|ROOM|RM) *(\\S+) *(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern APT_PLACE_PTN2 = Pattern.compile("(\\d+)\\b[ /]*(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      int pt = field.indexOf(',');
      if (pt >= 0) {
        parseAddress(field.substring(0,pt).trim(), data);
        field = field.substring(pt+1).trim();
        Matcher match = CITY_ST_ZIP_PTN.matcher(field);
        if (match.lookingAt()) {
          data.strCity = match.group(1).trim();
          data.strState = match.group(2);
          if (data.strCity.length() == 0) data.strCity = getOptGroup(match.group(3));
          field = field.substring(match.end()).trim();
        } else {
          parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, field, data);
          field = getLeft();
          match = ST_ZIP_PTN.matcher(field);
          if (match.lookingAt()) {
            data.strState = match.group(1);
            data.strCity = match.group(2);
            field = field.substring(match.end());
          }
        }
      } 
      else {
        parseAddress(StartType.START_ADDR, field, data);
        field = getLeft();
      }
      
      field = stripFieldStart(field, "None");
      field = stripFieldEnd(field, "None");
      Matcher match = APT_PLACE_PTN1.matcher(field);
      boolean found = match.matches();
      if (!found) {
        match = APT_PLACE_PTN2.matcher(field);
        found = match.matches();
      }
      if (found) {
        data.strApt = append(data.strApt, "-", match.group(1));
        data.strPlace = match.group(2);
      } else {
        data.strPlace = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR CITY ST APT PLACE";
    }
  }
  
  private class MyCautionField extends AlertField {
    @Override
    public void parse(String field, Data data) {
      if (field.toUpperCase().startsWith("Y")) {
        data.strAlert = "CAUTION";
      }
    }
  }
  
  private static final Pattern DATE_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String part : DATE_BRK_PTN.split(field)) {
        part = stripFieldStart(field, "None");
        data.strSupp = append(data.strSupp, "\n", part);
      }
    }
  }
  
  private static final String[] CITY_LIST = new String[]{
      
      // City
      "HASTINGS",

      // Villages
      "FREEPORT",
      "MIDDLEVILLE",
      "NASHVILLE",
      "WOODLAND",

      // Census-Designated Places
      "DELTON",
      "DOWLING",
      "HICKORY CORNERS",

      // Unincorporated communities
      "ASSYRIA",
      "BANFIELD",
      "CLOVERDALE",
      "COATS GROVE",
      "LACEY",
      "MAPLE GROVE",
      "PRAIRIEVILLE",
      "QUIMBY",
      "SCHULTZ",

      // Townships
      "ASSYRIA TWP",
      "BALTIMORE TWP",
      "BARRY TWP",
      "CARLTON TWP",
      "CASTLETON TWP",
      "HASTINGS CHARTER TWP",
      "HOPE TWP",
      "IRVING TWP",
      "JOHNSTOWN TWP",
      "MAPLE GROVE TWP",
      "ORANGEVILLE TWP",
      "PRAIRIEVILLE TWP",
      "RUTLAND CHARTER TWP",
      "THORNAPPLE TWP",
      "WOODLAND TWP",
      "YANKEE SPRINGS TWP"
  };
}
