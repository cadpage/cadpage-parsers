package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHCuyahogaCountyFParser extends FieldProgramParser {
  
  public OHCuyahogaCountyFParser() {
    super("CUYAHOGA COUNTY", "OH", 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR/S6! CITY:CITY! ID:ID! UNIT:UNIT! INFO:INFO/N+");
  }
  
  private static final Pattern ID_PTN = Pattern.compile(" *FireNo *(\\d*)$");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    String callId = "";
    Matcher match = ID_PTN.matcher(body);
    if (match.find()) {
      callId = match.group(1);
      body = body.substring(0,match.start());
    }
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.strApt.equals("0")) data.strApt = "";
    data.strCallId = append(data.strCallId, "/", callId);
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " ID";
  }

}
