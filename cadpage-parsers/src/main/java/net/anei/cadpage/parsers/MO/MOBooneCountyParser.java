package net.anei.cadpage.parsers.MO;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Boone County, MO
 */
public class MOBooneCountyParser extends DispatchOSSIParser {
  
  public MOBooneCountyParser() {
    super(CITY_CODES, "BOONE COUNTY", "MO",
          "( CANCEL ADDR CITY! INFO/N+ " +
          "| FYI? DATETIME ID ADDR ( CODE | PLACE CODE | CITY PLACE X X CODE ) CALL SRC! UNIT PHONE INFO/N+ )");
  }
  
  @Override
  public String getFilter() {
    return "CAD@boonecountymo.org";
  }
  
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseMsg("CAD:"+body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("CODE"))  return new CodeField("\\d{1,3}[A-EO]\\d{1,2}[A-Z]?|[A-Z]{2,3}|TEST", true);
    if (name.equals("SRC")) return new SourceField("[A-Z]{2,5}", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("UNIT")) return new UnitField("[A-Z0-9,]+", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, data.strCode);
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : field.split("\n")) {
        line = line.trim();
        if (line.startsWith("Radio Channel:")) {
          data.strChannel = field.substring(14).trim();
        } else {
          data.strSupp = append(data.strSupp, "\n", line);
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CH";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AS",   "ASHLAND",
      "CO",   "COLUMBIA",
      "HB",   "HARTSBURG",
      "RO",   "ROCHEPORT",
      
      "BC",   ""
     });
}
