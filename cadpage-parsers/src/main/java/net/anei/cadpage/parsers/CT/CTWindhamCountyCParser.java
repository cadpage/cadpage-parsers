package net.anei.cadpage.parsers.CT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;



public class CTWindhamCountyCParser extends FieldProgramParser {

  public CTWindhamCountyCParser() {
    super("WINDHAM COUNTY", "CT",
          "CITY ADDR PLACE APT CALL CALL+? X! TIME");
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
    };
  }


  @Override
  public String getFilter() {
    return "wpdpaging@gmail.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    String[] flds = body.split(" \\\\ ");
    return parseFields(flds, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private static final Pattern CROSS_PTN = Pattern.compile("\\(X-STS +(?:Map ([^ ]+) +)?(.*)\\)");
  private class MyCrossField extends CrossField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CROSS_PTN.matcher(field);
      if (!match.matches()) return false;
      String map = match.group(1);
      if (map != null && !map.equals("-1")) data.strMap = map;
      data.strCross = match.group(2).replace('\\', '/').replace(".", "").trim();
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "MAP X";
    }
  }
  
  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("WINDHAM CENTER")) city = "WINDHAM";
    return city;
  }
}
