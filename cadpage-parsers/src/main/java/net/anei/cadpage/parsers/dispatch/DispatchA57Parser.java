package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class DispatchA57Parser extends FieldProgramParser {
  
  private static final Pattern DELIM = Pattern.compile("\n|(?=Call Type:|Address:|Common Name:|Closest Intersection:|Additional Location Info:|Nature of Call:|Assigned Units:|Priority:|Quadrant:|Status:|District:|Beat:|Narrative)");
  
  public DispatchA57Parser(String defCity, String defState) {
    super(defCity, defState,
          "Call_Time:DATETIME? Call_Type:CALL! Address:ADDRCITY! Common_Name:PLACE! City:CITY Closest_Intersection:X! Additional_Location_Info:INFO? Nature_of_Call:INFO? Assigned_Units:UNIT! Priority:PRI? Status:SKIP? Quadrant:MAP District:MAP Beat:MAP Primary_Incident:ID Radio_Channel:CH Narrative:INFO+");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return super.parseFields(DELIM.split(body), data);
  };
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new BaseDateTimeField();
    if (name.equals("ADDRCITY")) return new BaseAddressCityField();
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("MAP")) return new BaseMapField();
    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class BaseDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
    }
  }
  
  private class BaseAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }
  
  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
  
  private class BaseMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      data.strMap = append(data.strMap, "/", field);
    }
  }
  
  private static final Pattern INFO_CH_PTN = Pattern.compile("TAC\\d+");
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_CH_PTN.matcher(field).matches()) {
        data.strChannel = append(data.strChannel, "/", field);
      } else {
        data.strSupp = append(data.strSupp, "\n", field);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CH INFO";
    }
  }
}
