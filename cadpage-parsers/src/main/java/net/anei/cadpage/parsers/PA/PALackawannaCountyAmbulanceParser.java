package net.anei.cadpage.parsers.PA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class PALackawannaCountyAmbulanceParser extends FieldProgramParser {
  
  public PALackawannaCountyAmbulanceParser() {
    super("LACKAWANNA COUNTY", "PA",
           "DATETIME INC#:ID! FROM:ADDRCITY! RM:APT? TYPE:CALL! to:INFO");
  }
  
  @Override
  public String getFilter() {
    return "lead@ladisp.us";
  }

  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("hh:mmaa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      setTime(DATE_TIME_FMT, p.get(' '), data);
      data.strDate = p.getLast(' ');
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
}
