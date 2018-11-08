package net.anei.cadpage.parsers.MO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class MONewtonCountyBParser extends DispatchOSSIParser {
  
  public MONewtonCountyBParser() {
    super(CITY_CODES, "NEWTON COUNTY", "MO", 
          "( CANCEL ADDR CITY! " +
          "| FYI CALL ADDR CITY APT! INFO/N+ )");
    
  }
  
  @Override
  public String getFilter() {
    return "CAD@nc-cdc.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Text Message")) return false;
    body = "CAD:" + body;
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("APT")) return new MyAptField();
    return super.getField(name);
  }
  
  private static final Pattern APT_PTN = Pattern.compile("(?:RM|ROOM|APT|LOT) *(.*)");
  private class MyAptField extends AptField {
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "GOOD", "GOODMAN",
      "JOPL", "JOPLIN",
      "NEOS", "NEOSHO",
      "NEWT", "NEWTON",
      "REDI", "REDINGS MILL",
      "SENE", "SENECA"
  });
}
