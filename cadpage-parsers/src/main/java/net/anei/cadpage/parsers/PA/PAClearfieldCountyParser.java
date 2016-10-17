package net.anei.cadpage.parsers.PA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Clearfield County, PA
 */
public class PAClearfieldCountyParser extends FieldProgramParser {
  
  public PAClearfieldCountyParser() {
    super("CLEARFIELD COUNTY", "PA",
           "Inc:CALL! Add:ADDR! City:CITY! Units:UNIT SRC DATETIME END");
  }
  
  @Override
  public String getFilter() {
    return "alerts@clearfieldalerts.com,Clearfield Alerts";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Clearfield Alert")) return false;
    if (!parseFields(body.split("\n"), 3, data)) return false;
    if (data.strCity.equals("FALLS_JEF")) data.strCity = "FALLS CREEK";
    if (data.strCity.equals("WASH_JEFF")) data.strCity = "WASHINGTON TWP";
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField(DATE_FMT);
    return super.getField(name);
  }
  private static final DateFormat DATE_FMT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
}
