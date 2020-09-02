package net.anei.cadpage.parsers.MO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOPolkCountyParser extends FieldProgramParser {
  
  public MOPolkCountyParser() {
    super(CITY_CODES, "POLK COUNTY", "MO", 
          "( UNDER_CONTROL ADDR CITY! INFO/N+ " +
          "| CALL PLACE ADDR CITY DATETIME! END )");
  }
  
  @Override
  public String getFilter() {
    return "911paging@polkco911.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNDER_CONTROL")) return new CallField("UNDER CONTROL", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private static final Pattern CALL_CODE_PTN = Pattern.compile("(.*?) +(\\d{1,2}-[A-Z]-\\d{1,2})");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_CODE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strCode = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CALL CODE";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALDR", "ALDRICH",
      "BOLI", "BOLIVAR",
      "BRIG", "BRIGHTON",
      "BUFF", "BUFFALO",
      "DUNN", "DUNNEGAN",
      "FAIR", "FAIR PLAY",
      "FLEM", "FLEMINGTON",
      "FRGO", "FAIR GROVE",
      "HALF", "HALFWAY",
      "HUMA", "HUMANSVILLE",
      "LOUI", "LOUISBURG",
      "MORR", "MORRISVILLE",
      "PLEA", "PLEASANT HOPE",
      "POLK", "POLK",
      "STOC", "STOCKTON",
      "URBA", "URBANA",
      "WALN", "WALNUTGROVE",
      "WILL", "WILLARD"
      
  });
}
