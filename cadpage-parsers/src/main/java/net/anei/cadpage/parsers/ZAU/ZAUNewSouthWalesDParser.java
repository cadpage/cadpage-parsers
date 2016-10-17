package net.anei.cadpage.parsers.ZAU;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZAUNewSouthWalesDParser extends FieldProgramParser {
  
  public ZAUNewSouthWalesDParser() {
    super("", "NSW", CountryCode.AU, 
          "CALL:SKIP ID:ID ADDR:ADDR", FLDPROG_IGNORE_CASE);
  }
  
  @Override
  public String getFilter() {
    return "whyf@onesteel.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern MARKER = Pattern.compile("SiPass Event Task generated email message at (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M) *");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.length() == 0) return false;
    data.strCall = subject;
    
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    data.strDate = match.group(1);
    setTime(TIME_FMT, match.group(2), data);
    body = body.substring(match.end());
    
    int pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    body = stripFieldEnd(body,  ";");
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public String getProgram() {
    return "CALL DATE TIME " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private static final Pattern MAP_GPS_PTN = Pattern.compile("(.*), *([-+]?[.0-9]*, *[-+]?[.0-9]*)");
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = MAP_GPS_PTN.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(2).trim(), data);
        field = match.group(1).trim();
      }
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " GPS";
    }
  }

}
