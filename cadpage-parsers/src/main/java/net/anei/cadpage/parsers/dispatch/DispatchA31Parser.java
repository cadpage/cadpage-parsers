package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class DispatchA31Parser extends FieldProgramParser {
  
  private String prefix;
  private String prefixPlusSlash;
  
  public DispatchA31Parser(String prefix, String defCity, String defState) {
    this(prefix, null, defCity, defState);
  }
  
  public DispatchA31Parser(String prefix, Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState, 
          "TIME SRC SKIP CALL ADDR! CITY");
    this.prefix = prefix;
    this.prefixPlusSlash = prefix + " / ";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    do {
      if (subject.equals(prefix)) break;

      if (body.startsWith(prefixPlusSlash)) {
        body = body.substring(prefixPlusSlash.length()).trim();
        break;
      }
      return false;
    } while (false);
    
    return parseFields(body.split("\n"), 5, data);
  }

  private class MySourceField extends SourceField {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("NEW EVENT ")) abort();
      super.parse(field.substring(10).trim(), data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField(TIME_FMT);
    if (name.equals("SRC")) return new MySourceField();
    return super.getField(name);
  }
  
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");
}
