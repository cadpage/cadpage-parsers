package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHLakeCountyCParser extends FieldProgramParser {
  
  public OHLakeCountyCParser() {
    super(CITY_LIST, "LAKE COUNTY", "OH", 
          "CALL ADDR ( CITY | PLACE CITY | ) INFO/CS+");
  }
  
  @Override
  public String getFilter() {
    return "info@sundance-sys.com";
  }

  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("From: WillowickPager")) return false;
    return parseFields(body.split(",", -1), 5, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\w+)-(.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCode = match.group(1);
      data.strCall = match.group(2).trim();
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String part : field.split(";")) {
        data.strSupp = append(data.strSupp, "\n", part);
      }
    }
  }
  
  private static final String[] CITY_LIST = new String[]{
    "WILLOWICK"
  };
}
