package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class COMesaCountyParser extends FieldProgramParser {
  
  public COMesaCountyParser() {
    super("MESA COUNTY", "CO",
          "ID Call_Type:CALL! Location:ADDR/SXx! Apt/Unit:APT? Lat/Long:GPS? Closest_Intersection:X? Call_Time:DATETIME! Narrative:INFO");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@ci.grandjct.co.us";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{8}", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      field = field.replace("UNKNOWN", "").trim();
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d)\\b *(.*)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      data.strSupp = match.group(3);
    }
    
    @Override
    public String getFieldNames() {
      return "DATE TIME INFO";
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = addr.replace("HWY 6 & 50", "HWY 50");
    return super.adjustMapAddress(addr);
  }
  
}
