package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHLakeCountyCParser extends FieldProgramParser {
  
  public OHLakeCountyCParser() {
    super("LAKE COUNTY", "OH", 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR/S6! CITY:CITY! ID:ID! UNIT:UNIT! INFO:INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "info@sundance-sys.com";
  }
  
  private static final Pattern TRAIL_ID_PTN = Pattern.compile("\\s*FireNo *\\d*$");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("From: WillowickCAD")) return false;
    Matcher match = TRAIL_ID_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start());
    return parseFields(body.split("\n"), data);
  }
}
