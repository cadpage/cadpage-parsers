package net.anei.cadpage.parsers.ZAU;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class ZAUNewSouthWalesAParser extends FieldProgramParser {
  
  private static final Pattern CALLBACK_PTN = Pattern.compile("\\.* *CALL \\d{10}(?: (\\d\\d/\\d\\d) (\\d\\d:\\d\\d:\\d\\d))?$");

  public ZAUNewSouthWalesAParser() {
    super(ZAUNewSouthWalesParser.CITY_LIST, "", "NSW", CountryCode.AU,
           "UNIT ID NAME ( ADDR/Z CITY | ADDR/S ) PHONE CALL! CALL+");
  }

  @Override
  public String getLocName() {
    return "New South Wales, AU";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = CALLBACK_PTN.matcher(body);
    if (!match.find()) return false;
    String date = match.group(1);
    if (date != null) {
      data.strDate = date.substring(3,5) + '/' + date.substring(0,2);
      data.strTime = match.group(2);
    }
    body = body.substring(0,match.start()).trim();
    return parseFields(body.split(","), 6, data);
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " DATE TIME";
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = append(data.strSupp, ", ", field);
    }
  }
  
  @Override
  protected Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
}
