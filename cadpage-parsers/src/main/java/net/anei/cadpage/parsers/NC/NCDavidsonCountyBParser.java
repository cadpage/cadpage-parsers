package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NCDavidsonCountyBParser extends FieldProgramParser {
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("(Clear|Dispatch) Report Incident #(\\d+)");
  private static final String MARKER = "Dispatch\nCommunications\n";

  public NCDavidsonCountyBParser() {
    super(CITY_CODES, "DAVIDSON COUNTY", "NC",
           "Address:EMPTY! Cross:EMPTY! ADDR! X X Nature:EMPTY! CALL! MP:EMPTY? MAP Date:EMPTY! DATE! Time_Out:EMPTY! TIME! City:EMPTY! CITY!");
  }
    
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(2);
    if (match.group(1).equals("Clear")) {
      data.strCall = "RUN REPORT";
      data.strPlace = body;
      return true;
    }
    
    if (!body.startsWith(MARKER)) return false;
    body = body.substring(MARKER.length()).trim();
    body = body.replace("â€‘", "-");  // Dispatch did something wierd here
    body = body.replace((char)0x2011, '-');
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram() + " PLACE";
  }
  
  private static final Pattern CODE_CALL_PRI_PTN = Pattern.compile("([A-Z0-9]+)-(.*?)(?:-([A-Z]+))?");
  private class MyCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PRI_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCode = match.group(1);
      data.strCall = match.group(2).trim();
      data.strPriority = getOptGroup(match.group(3));
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL PRI";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
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
