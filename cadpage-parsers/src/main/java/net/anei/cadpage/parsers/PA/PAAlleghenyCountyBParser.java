package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAAlleghenyCountyBParser extends FieldProgramParser {
  
  public PAAlleghenyCountyBParser() {
    super("ALLEGHENY COUNTY", "PA", 
          "Address:ADDR! xSts:X! Code:CODE! Code_Detail:CALL! Fire_Zone:MAP! Description:INFO! TOC:DATETIME! Inc_Num:ID! Responding:SKIP! Units:UNIT! Disp_Grp:CH! Call_Taker:SKIP");
    setupGpsLookupTable(PAAlleghenyCountyParser.GPS_TABLE_LOOKUP);
    setupPlaceGpsLookupTable(PAAlleghenyCountyParser.PLACE_GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "alert@ecm2.us";
  }
  
  
  private static final Pattern UPDATE_PTN = Pattern.compile("Update to Inc# (F\\d{9}): *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.endsWith(" - 911 Call")) return false;
    
    Matcher match = UPDATE_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strCallId = match.group(1);
      body = body.substring(match.end());
    }
    
    body = body.replace(" Unit:", "Units:");
    return parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("MAP")) return new MapField("\\d{6}", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (city.equals("PA")) {
        data.strState = city;
        city = p.getLastOptional(',');
      }
      data.strCity = city;
      super.parse(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }

  private static final Pattern INFO_ID_PTN = Pattern.compile("\\b[A-Z]{4}\\d{9}\\b");
  private static final Pattern INFO_PHONE_PTN = Pattern.compile(" */+ *|(?=\\d)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      
      Matcher match = INFO_ID_PTN.matcher(field);
      if (!match.find()) {
        super.parse(field, data);
        return;
      }
      super.parse(field.substring(match.end()).trim(), data);
      
      field = field.substring(0, match.start()).trim();
      if (field.length() > 0) {
        field = stripFieldStart(field, "Phone:");
        match = INFO_PHONE_PTN.matcher(field);
        if (match.find()) {
          data.strName = field.substring(0,match.start()).trim();
          data.strPhone = field.substring(match.end()).trim();
        } else {
          data.strName = field;
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "NAME PHONE INFO";
    }
  }
}
