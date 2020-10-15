package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class DispatchA57Parser extends FieldProgramParser {
  
  public DispatchA57Parser(String defCity, String defState) {
    super(defCity, defState,
          "( SELECT/1 Call_Time:DATETIME? Call_Type:CALL! Radio_Channel:CH? Address:ADDRCITY/S6! " + 
                "( Cross_Sts:X! Unit:UNIT! INFO/N+? DATETIME! GPS? " +
                "| City:CITY Common_Name:PLACE Custom_Layer:MAP? Map_Page:MAP? ( Latt:GPS1! Long:GPS2 | ) Closest_Intersection:X EMPTY+? Narrative:INFO Additional_Location_Info:INFO EMPTY+? Nature_of_Call:INFO EMPTY+? " + 
                      "( Assigned_Units:UNIT% | Dispatched_Units:UNIT% ) Priority:PRI? ( Narrative:INFO/N | Nar:INFO/N | ) INFO/N+ Status:SKIP? ( Fire_Box:BOX EMS_District:MAP | Quadrant:MAP District:MAP ) Beat:MAP " + 
                      "Lat_and_Long:GPS CFS_Number:ID1? Primary_Incident:ID2/L CFS_Number:ID1? Radio_Channel:CH? ( Nar:INFO | Narrative:INFO ) INFO/N+ " + 
                ") " +
          "| DATETIME CALL ADDRCITY PLACE CALL/SDS ID! UNIT% INFO/N+ )");
  }
  
  private static final Pattern DELIM1 = Pattern.compile("\n|(?<!\n)(?=Call Type:|Address:|Common Name:|City:|Custom Layer:|Map Page:|Latt:|(?<!Lat and )Long:|Closest Intersection:|Additional Location Info:|Nature of Call:|Assigned Units:|Priority:|Quadrant:|Status:|Fire Box:|(?:EMS )?District:|Beat:|Narr:|Narrative)");
  private static final Pattern DELIM2 = Pattern.compile("\\s*;\\s*");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("Call Time:") || body.startsWith("Call Type:")) {
      setSelectValue("1");
      return super.parseFields(DELIM1.split(body), data);
    }
    
    setSelectValue("2");
    return super.parseFields(DELIM2.split(body), data);
  };
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new BaseDateTimeField();
    if (name.equals("ADDRCITY")) return new BaseAddressCityField();
    if (name.equals("PLACE")) return new BasePlaceField();
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("UNIT")) return new BaseUnitField();
    if (name.equals("GPS")) return new BaseGPSField();
    if (name.equals("MAP")) return new BaseMapField();
    if (name.equals("ID1")) return new BaseId1Field();
    if (name.equals("ID2")) return new BaseId2Field();
    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class BaseDateTimeField extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final Pattern ADDR_PLACE_PTN = Pattern.compile("(.*)\\((.*)\\)");
  private static final Pattern ADDR_PLACE_PTN2 = Pattern.compile("(.*?)(?:(?<!LAT|LON):| - )(.*)");
  private class BaseAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_PLACE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strPlace = match.group(2).trim();
      } 
      else if ((match = ADDR_PLACE_PTN2.matcher(field)).matches()) { 
        field = match.group(1).trim();
        data.strPlace = match.group(2).trim();
      } else {
        field = stripFieldEnd(field, " -");
      }
      field = field.replace('@', '&');
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  private class BasePlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ":");
      super.parse(field, data);
    }
  }
  
  private class BaseUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ";");
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
  
  private static final Pattern GPS_PTN = Pattern.compile("([-+]?\\d{2,3}\\.\\d{6,})([-+]\\d{2,3}\\.\\d{6,})");
  private class BaseGPSField extends GPSField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) return false;
      setGPSLoc(match.group(1)+','+match.group(2), data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private class BaseId1Field extends IdField {
    @Override
    public void parse(String field, Data data) {
      if (!data.strCallId.isEmpty()) return;
      super.parse(field, data);
    }
  }
  
  private class BaseId2Field extends IdField {
    @Override
    public void parse(String field, Data data) {
      StringBuilder sb = new StringBuilder();
      for ( String id : field.split(",")) {
        id = id.trim();
        id = stripFieldStart(id, "[");
        id = stripFieldEnd(id, "]");
        if (id.startsWith("Incident not yet created")) continue;
        int pt = id.indexOf(' ');
        if (pt >= 0) id = id.substring(0,pt);
        if (sb.length() > 0) sb.append(',');
        sb.append(id);
      }
      data.strCallId = sb.toString();
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
