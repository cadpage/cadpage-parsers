package net.anei.cadpage.parsers.OH;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHLorainCountyCParser extends FieldProgramParser {
  
  public OHLorainCountyCParser() {
    super("LORAIN COUNTY", "OH", 
          "( Call_Time:DATETIME! Call_Type:CALL! Address:ADDRCITY! Common_Name:PLACE! Closest_Intersection:X! " +
            "Additional_Location_Info:INFO! Nature_of_Call:CALL/SDS! Assigned_Units:UNIT! Priority:PRI! Status:SKIP! " + 
            "Quadrant:MAP! District:MAP/D! Beat:MAP/D! CFS_Number:SKIP! Primary_Incident:ID! Radio_Channel:CH! Narrative:INFO! " +
          "| DATETIME CALL ADDRCITY! X PLACE/SDS " + 
          ") INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "noreply@loraincounty911.com";
  }
  
  private static final Pattern DELIM = Pattern.compile("\n|(?<!\n)(?=Call Type:|Address:|Common Name:|Clossest Intersection:|Additional Location Info:|Nature of Call:|Assigned Units:|Priority:|Status:|Quadrant:|District:|Beat:|CFS Number:|Primary Incident:|Radio Channel:|Narrative:)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("1??(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa"); 
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
    }
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("  ");
      if (pt >= 0) {
        data.strPlace = field.substring(pt+3).trim();
        field = field.substring(0,pt);
      }
      field = field.replace('@', '&');
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE?";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found") || field.equals("Searching Cross Streets...")) return;
      super.parse(field, data);
    }
  }
}
