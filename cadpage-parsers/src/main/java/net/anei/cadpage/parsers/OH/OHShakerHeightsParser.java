package net.anei.cadpage.parsers.OH;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Shaker Heights, OH
 */
public class OHShakerHeightsParser extends FieldProgramParser {

  public OHShakerHeightsParser() {
    super("SHAKER HEIGHTS", "OH", 
          "ID:ID! CALL:CALL! LATITUDE:GPS1! LONGITUDE:GPS2! ADDRESS:ADDR! CITY:CITY! ZIP:SKIP! NOTES:INFO+");
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{4,8}", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern INFO_DATE_TIME_PTN = Pattern.compile(" *Date: (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M) User: [\\S]+ Narrative: *");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa"); 
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Narrative Imported From NWS CAD system....");
      field = stripFieldEnd(field, "End of Narrative Imported From NWS CAD system....");
      if (field.length() == 0) return;
      
      Matcher match = INFO_DATE_TIME_PTN.matcher(field);
      int lastPt = 0;
      while (match.find()) {
        data.strSupp = append(data.strSupp, "\n", field.substring(lastPt, match.start()).trim());
        lastPt = match.end();
        if (data.strTime.length() == 0) {
          data.strDate = match.group(1);
          setTime(TIME_FMT, match.group(2), data);
        }
      }
      data.strSupp = append(data.strSupp, "\n", field.substring(lastPt).trim());
    }
    
    @Override
    public String getFieldNames() {
      return "DATE TIME INFO";
    }
  }
}
  