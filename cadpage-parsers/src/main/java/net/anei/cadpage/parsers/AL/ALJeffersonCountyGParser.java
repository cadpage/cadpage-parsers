package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class ALJeffersonCountyGParser extends DispatchSouthernParser {
    
  public ALJeffersonCountyGParser() {
    super(ALJeffersonCountyParser.CITY_LIST, "JEFFERSON COUNTY", "AL",
          "ADDR/S X EMPTY EMPTY ( EMPTY UNIT | ) ID TIME CALL! INFO+");
    setupCities("UNINCORPORATED");
     
  }
  
  private static final Pattern OCA_PTN = Pattern.compile("\\bOCA: *[\\d-]+$");
      
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = OCA_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start()).trim();
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.equalsIgnoreCase("UNINCORPORATED")) data.strCity = "";
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
      if (data.strUnit.length() == 0) {
        Matcher match = UNIT_CALL_PTN.matcher(field);
        if (match.matches()) {
          data.strUnit = match.group(1);
          field = match.group(2);
        }
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "UNIT CALL";
    }
  }

}
