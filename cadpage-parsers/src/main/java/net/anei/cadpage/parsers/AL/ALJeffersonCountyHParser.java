package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class ALJeffersonCountyHParser extends DispatchSouthernParser {
    
  public ALJeffersonCountyHParser() {
    super(ALJeffersonCountyParser.CITY_LIST, "JEFFERSON COUNTY", "AL",
          "ADDR/S APT EMPTY EMPTY ID TIME CALL! INFO+");
  }
  
  private static final Pattern OCA_PTN = Pattern.compile("\\bOCA: *[\\d-]+$");
  private static final Pattern MM_PTN = Pattern.compile(" MM, *");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = OCA_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start()).trim();
    body = MM_PTN.matcher(body).replaceAll(" MM ");
    if (!super.parseMsg(body, data)) return false;
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{6}-\\d{6}");
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
  
  private static final Pattern UNIT_CALL_PTN = Pattern.compile("(\\d\\d) +(.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = UNIT_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strUnit = match.group(1);
        field = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "UNIT CALL";
    }
  }
}
