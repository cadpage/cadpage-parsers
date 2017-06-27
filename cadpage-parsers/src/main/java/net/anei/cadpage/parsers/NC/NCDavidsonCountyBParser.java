package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;


public class NCDavidsonCountyBParser extends FieldProgramParser {
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("Dispatch Report Incident #(\\d+)");
  private static final Pattern MARKER = Pattern.compile("Communications\n+Dispatch\n+");;

  public NCDavidsonCountyBParser() {
    super(CITY_CODES, "DAVIDSON COUNTY", "NC",
          "Incident#:ID! Report#:SKIP! Date:DATE! Time_Out:TIME! Nature:CALL! MP:CODE! Business:PLACE! Address:ADDR! City:CITY! Addt_Address:PLACE2! Cross:X! X+ Subdivision:SKIP! Neighborhood:SKIP! Notes:INFO/N+");
  }
  
  private static final Pattern DELIM = Pattern.compile("\n| (?=Report#:|Time Out:|MP:|City:)");
    
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    
    match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    body = body.substring(match.end()).trim();
    body = body.replace("â€‘", "-");  // Dispatch did something wierd here
    body = body.replace((char)0x2011, '-');
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d\\d", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("PLACE2")) return new MyPlace2Field(); 
    return super.getField(name);
  }
  
  
  private static final CodeTable CALL_TABLE = new StandardCodeTable();
  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      String call = CALL_TABLE.getCodeDescription(field);
      if (call != null) data.strCall = call;
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private Pattern PLACE_APT_PTN = Pattern.compile("(?:APT|RM|ROOM|SUITE|LOT) +(\\S+) *(.*)");
  private Pattern PLACE_PHONE_PTN = Pattern.compile("(.*?) *\\b(\\d{10})");
  private class MyPlace2Field extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("(S)");
      if (pt >= 0) field = field.substring(0, pt).trim();
      
      pt = field.indexOf("DIST:");
      if (pt >= 0) field = field.substring(0,pt).trim();
      
      Matcher match = PLACE_APT_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", getOptGroup(match.group(1)));
        field = match.group(2);
      }
      
      match = PLACE_PHONE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strPhone = match.group(2);
      }
      
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "APT PLACE PHONE";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ARC",  "ARCHDALE",     // Guildford County
      "CL",   "CLEMMONS",     // FOrsyth County
      "DEN",  "DENTON",
      "GREE", "GREENSBORO",
      "HP",   "HIGH POINT",
      "KV",   "KERNERSVILLE", // Forsyth County
      "LEX",  "LEXINGTON", 
      "LIN",  "LINWOOD",
      "NL",   "NEW LONDON",   // Stanly County
      "RAN",  "RANDOLPH COUNTY",
      "RWC",  "ROWAN COUNTY",
      "THA",  "THOMASVILLE",
      "TROY", "TROY",         // Montgomery County
      "WELC", "WELCOME",
      "WS",   "WINSTON-SALEM" // Forsyth County
      // Missing MIDWAY and WALLBURG
  });
}
