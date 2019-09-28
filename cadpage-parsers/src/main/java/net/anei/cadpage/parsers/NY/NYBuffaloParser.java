package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;


public class NYBuffaloParser extends DispatchProQAParser {
  
  public NYBuffaloParser() {
    super(CITY_LIST, "BUFFALO", "NY",
          "( CALL CALL/L TIME ADDR APT CITY ZIP CALL/L! " +
          "| JOB? PRI? CODE_CALL CALL2/L+? ADDR APT APT/L+? CITY ALT_ID INFO/N+? TIME! " + 
          ") INFO/N+", true);
  }
  
  @Override
  public String getFilter() {
    return "2002000004,777,tcaems.com";
  }
  
  private static final Pattern PREFIX_PTN = Pattern.compile("([A-Z0-9]+): +(?=RC:)");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = PREFIX_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strSource = match.group(1);
      body = body.substring(match.end());
    }
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new PriorityField("(\\d) .*", true);
    if (name.equals("JOB")) return new SkipField("\\d{4}-[A-Z]");
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("CALL2")) return new CallField("(?!.* (?:Institute|Park|Center|Room|Area)$).*[a-z].*|GSW|", true);
    if (name.equals("ALT_ID")) return new SkipField("\\d+", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("ZIP")) return new SkipField("\\d{5}", true);
    return super.getField(name);
  }
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d\\d?[A-Z]\\d\\d?[A-Z]?) +(.*)");
  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private static final String[] CITY_LIST = new String[]{
      "AMHERST",
      "BUFFALO",
      "CHEEKTOWAGA",
      "DEPEW",
      "EAST AURORA",
      "ELMA",
      "ELMA",
      "EVANS",
      "HAMBURG",
      "KENMORE",
      "LACKAWANNA",
      "NIAGARA FALLS",
      "NORTH COLLINS",
      "SLOAN",
      "WEST SENECA"
  };
}
