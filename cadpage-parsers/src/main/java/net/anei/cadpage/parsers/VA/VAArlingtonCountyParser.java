package net.anei.cadpage.parsers.VA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VAArlingtonCountyParser extends FieldProgramParser {

  public VAArlingtonCountyParser() {
    super(CITY_CODES, "ARLINGTON COUNTY", "VA", 
          "CALL BOX ADDR CITY Units:UNIT/S+");
  }
  
  @Override
  public String getFilter() {
    return "rwaller@arlingtonva.us";
  }
  
  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Email Copy Message From Hiplink")) return false;
    if (!body.startsWith("DISPATCH:")) return false;
    body = body.substring(9).trim();
    
    int pt = body.indexOf('\n');
    if (pt >= 0) {
      String tail = body.substring(pt+1).trim();
      body = body.substring(0,pt).trim();
      pt = tail.lastIndexOf("  ");
      if (pt >= 0) tail = tail.substring(pt+2).trim();
      setDateTime(DATE_TIME_FMT, tail, data);
    }
    body = body.replace(" Unit:", " Units");
    return parseFields(body.split(", "), data);
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " DATE TIME";
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AC", "ARLINGTON COUNTY",
      "AX", "ALEXANDRIA"
  });
}
