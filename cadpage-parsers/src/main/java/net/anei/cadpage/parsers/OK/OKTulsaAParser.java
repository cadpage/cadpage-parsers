package net.anei.cadpage.parsers.OK;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class OKTulsaAParser extends FieldProgramParser {
  
  public OKTulsaAParser() {
    super("TULSA", "OK",
          "( ADDR/Z PLACE CALL MAP! PLACE ID " +
          "| CALL ADDR PLACE ID MAP! ) INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "pagealert@cityoftulsa.org,@berryhillfire.com";
  }
  
  private static final Pattern DELIM = Pattern.compile(" [.,]|,(?=\\[\\d{1,2}\\])");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseFields(DELIM.split(body, -1), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("[A-Z]{3,5}\\d{4}-?\\d+", true);
    if (name.equals("MAP")) return new MyMapField();
    return super.getField(name);
  }
  
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("NOT FOUND")) return;
      field = stripFieldStart(field, "MP ");
      super.parse(field, data);
    }
  }
}
