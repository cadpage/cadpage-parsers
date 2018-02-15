package net.anei.cadpage.parsers.OK;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

public class OKTulsaCParser extends FieldProgramParser {
  
  public OKTulsaCParser() {
    super("TULSA", "OK", 
          "CALL ADDR PLACE ID MAP! END");
  }
  
  @Override
  public String getFilter() {
    return "pagealert@cityoftulsa.org";
  }

  private static final Pattern DELIM = Pattern.compile(" [.,]");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("MAP")) return new MapField("MP +(.*)", true);
    return super.getField(name);
  }
}
