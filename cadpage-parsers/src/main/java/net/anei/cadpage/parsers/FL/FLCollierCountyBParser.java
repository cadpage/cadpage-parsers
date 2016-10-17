package net.anei.cadpage.parsers.FL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class FLCollierCountyBParser extends FieldProgramParser {
  
  public FLCollierCountyBParser() {
    super("COLLIER COUNTY", "FL", 
          "DATETIME CALL CALL/SDS UNIT ADDR! Crosstreet:X! INFO+? GPS1 GPS2 END");
  }
  
  @Override 
  public String getFilter() {
    return "emssql@colliergov.net";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Active911 Dispatch")) return false;
    
    int pt = body.indexOf("\n\nUnder Florida Law");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split(","), data);
  }
  
  private static final DateFormat DATETIME_FMT = new SimpleDateFormat("MMM dd yyyy hh:mmaa");
  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}");
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField(DATETIME_FMT, true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("GPS1")) return new GPSField(1, GPS_PTN, true);
    if (name.equals("GPS2")) return new GPSField(2, GPS_PTN, true);
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
  
  private static final Pattern MBLANK_PTN = Pattern.compile("\n{2,}");
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = MBLANK_PTN.matcher(field).replaceAll("\n");
      data.strSupp = append(data.strSupp, ", ", field);
    }
  }
}
