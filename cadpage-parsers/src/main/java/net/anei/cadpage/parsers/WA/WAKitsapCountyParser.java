package net.anei.cadpage.parsers.WA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WAKitsapCountyParser extends FieldProgramParser {

  public WAKitsapCountyParser() {
    super(CITY_CODES, "KITSAP COUNTY", "WA", 
          "Location:ADDR/S! EID:ID! TYPE_CODE:CODE! TIME:TIME! Comments:INFO? TY:CALL! ALR:PRI! Beat:BOX! C:SKIP? Disp:UNIT! END");
  }
  
  @Override
  public String getFilter() {
    return "cencomca@co.kitsap.wa.us";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    //check for CENCOM, remove from body
    if (!subject.equals("CENCOM")) {
      if (!body.startsWith("CENCOM")) return false;
      body = body.substring(6).trim();
    }
    
    // Is this the new format
    if (body.startsWith("Location:")) {
      return super.parseMsg(body, data);
    }
    
    // Old logic, not used since 7/14/2016
    return parseOldMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(':');
      if (pt >= 0) {
        String place = field.substring(pt+1).trim();
        place = stripFieldStart(place, "@");
        data.strPlace = place;
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.length() == 0) {
        data.strCall = field;
      }
      else if (!field.equals(data.strCall)) {
        data.strCall = append(data.strCall, " - ", field);
      }
    }
  }
  
  private static final Pattern INFO_GPS_PTN = Pattern.compile("([-+]\\d{2,3}\\.\\d{3,} [-+]\\d{2,3}\\.\\d{3,})(?: \\d+ WPH\\d)?\\b *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_GPS_PTN.matcher(field);
      if (match.lookingAt()) {
        setGPSLoc(match.group(1), data);
        field = field.substring(match.end());
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "GPS " + super.getFieldNames();
    }
  }
  
  public static Pattern HEAD = Pattern.compile("(?:Loc: +(.*?)(?:: *(.*?))? +)?TY: +(.*?) +ALR: *(.*?) +RP:? +(.*)");
  public static Pattern MID = Pattern.compile("(?:(.*?)  +(.*?)(?: +- +[NSEW]{1,2} .*?| +CALLBK=.*?)?  +([A-Z]?\\(?\\d{3}\\)?[ \\-]?\\d{3}[\\- ]\\d{4})|(.*?)) *(?:T:(\\d{2}:\\d{2}:\\d{2})(?: Beat: *(\\d+))?(?: C:(?:([+\\-]\\d+(?:\\.\\d+)?) ?([+\\-]\\d+(?:\\.\\d+)?)(?: \\d+ WPH\\d)?)? *(.*))?)?");
  public static Pattern TAIL = Pattern.compile("(?:(.*?) *\\*\\* *)?(?:Case number|PACIFIC SURGERY) ([A-Z]+(?:\\d *){8})(?:has been assigned for .*?  +\\*\\* >>>> by: +.*? +on terminal: *?.*?(?:  +(.*))?|.*)?");
  
  private boolean parseOldMsg(String body, Data data) {
    
    //HEAD - ADDR (PLACE|APT)? CODE? PRI?
    Matcher mat = HEAD.matcher(body);
    if (!mat.matches()) return false;
    setFieldList("ADDR CITY APT PLACE CITY CALL PRI NAME PHONE TIME GPS INFO ID UNIT SRC");
    String loc = mat.group(1);
    if (loc != null) {
      int pt = loc.indexOf(';');
      if (pt >= 0) {
        data.strPlace = loc.substring(pt+1).trim();
        loc = loc.substring(0,pt);
      }
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, loc.trim(), data);
    }
    //if loc is null, we use RP's address in MID
    data.strPlace = append(data.strPlace, " - ", getOptGroup(mat.group(2)));
    data.strCall = getOptGroup(mat.group(3));
    data.strPriority = getOptGroup(mat.group(4));
    body = mat.group(5);
    
    
    //MID - NAME backup_ADDR PHONE TIME GPS?
    mat = MID.matcher(body);
    if (!mat.matches()) return false;
    String unformatted_rp_field = mat.group(4);
    if (unformatted_rp_field != null) {
      data.strName = unformatted_rp_field;
    } else {  
      data.strName = cleanWirelessCarrier(mat.group(1));
      //if there was no "LOC:" field in head, parse RP's ADDR, discarding what's left (callback number)
      if (loc == null) parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, getOptGroup(mat.group(2)), data);
      data.strPhone = mat.group(3);   
    }
    data.strTime = getOptGroup(mat.group(5));
    data.strUnit = getOptGroup(mat.group(6));
    setGPSLoc(mat.group(7) + ',' + mat.group(8), data);
    body = mat.group(9);
    if (body == null) return true;


    //TAIL - INFO ID INFO?
    mat = TAIL.matcher(body);
    if (mat.matches()) {
      data.strSupp = append(data.strSupp, " / ", getOptGroup(mat.group(1)));
      data.strCallId = mat.group(2).replace(" ", "");
      data.strSupp = append(data.strSupp, " / ", getOptGroup(mat.group(3)));
    } else data.strSupp = append(data.strSupp, " / ", body);
    return true;
  }
  
  public static Properties CITY_CODES = buildCodeTable(new String[]{
      "BI", "BAINBRIDGE ISLAND",
      "BR", "BREMERTON",
      "HA", "HANSVILLE",
      "IN", "INDIANOLA",
      "KI", "KINGSTON",
      "KY", "KEYPORT",
      "OL", "OLALLA",
      "PB", "POULSBO",
      "PG", "PORT GAMBLE",
      "PO", "PORT ORCHARD",
      "SE", "SEABACK",
      "SI", "SILVERDALE",
      "SU", "SUQUAMISH"
  });
}
