package net.anei.cadpage.parsers.KY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class KYFortKnoxParser extends FieldProgramParser {

  public KYFortKnoxParser() {
    super("FORT KNOX", "KY", "ID:ID! CLASS:CALL! CALL:CALL/SDS! PRI:PRI! PLACE:PLACE ADDR:ADDR! GPS:GPS! END");
  }
  
  @Override
  public String getFilter() {
    return "monacoenterprises2014@gmail.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("TEST Incident Alert")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("([-+]?\\d{2,3}\\.\\d{3}),([-+]?\\d{2,3}\\.\\d{3})");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(1)+"000" + "," + match.group(2) + "000", data);
      }
    }
  }
}
