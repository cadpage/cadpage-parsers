package net.anei.cadpage.parsers.ZAU;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZAUWhyallaParser extends FieldProgramParser {
  
  public ZAUWhyallaParser() {
    super("WHYALLA", "SA", CountryCode.AU,
          "CALL:CALL! ID:UNIT! ADDR:ADDR");
  }
  
  @Override
  public String getFilter() {
    return "usgwhy@gmail.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern MARKER = Pattern.compile("[A-Za-z ]*\\b(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M) (?=CALL:)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    int pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    data.strDate = match.group(1);
    setTime(TIME_FMT, match.group(2), data);
    body = body.substring(match.end());
    
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public String getProgram() {
    return "DATE TIME " + super.getProgram();
  }
}
