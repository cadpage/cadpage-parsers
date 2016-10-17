package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOLawrenceCountyParser extends FieldProgramParser {

  public MOLawrenceCountyParser() {
    super(CITY_LIST, "LAWRENCE COUNTY", "MO",
      "Date:DATE! Addr:ADDR1/S! Inc_Type:CALL! Remarks:INFO! ADDR2/S? INFO+");    
  }
  
  @Override
  public String getProgram() {
    return append(super.getProgram(), " ", "PLACE");
  }

  @Override
  public String getFilter() {
    return "cad@e9.com";
  }

  private static final Pattern HIGHWAY_PATTERN
    = Pattern.compile("(.*?) +HIGHWAY +(?:HWY +)?\\b(\\d{1,3}|([A-Z])\\3?)\\b(?: +HY)?");
  private static final Pattern LAWRENCE_HIGHWAY_PATTERN
    = Pattern.compile("(.*? +LAWRENCE +)HWY +(.*)");
  @Override
  public String postAdjustMapAddress(String field) {
    Matcher m = HIGHWAY_PATTERN.matcher(field);
    if (m.matches()) return m.group(1)+" STATE HWY "+m.group(2);
    m = LAWRENCE_HIGHWAY_PATTERN.matcher(field);
    if (m.matches()) return m.group(1)+m.group(2);
    return field;
  }
  
  @Override
  public boolean parseMsg (String body, Data data) {
    if (body.contains("CAD Num:")) {
      data.strCall = "RUN REPORT";
      data.strPlace = body;
      return true;
    }
    return super.parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new MyDateField("(.*?)\\s*Num\\:.*", false);
    if (name.equals("ADDR1")) return new MyAddressField();
    if (name.equals("ADDR2")) return new AddressField(">AC<(.*)", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PATTERN
  = Pattern.compile("(\\d{2}\\/\\d{2}\\/\\d{2})\\s*Time\\:(\\d{2}\\:\\d{2}).*");
  private class MyDateField extends DateField {
    MyDateField(String p, boolean h) {
      super(p, h);
    }
    
    @Override
    public void parse(String field, Data data) {
      Matcher m = DATE_TIME_PATTERN.matcher(field);
      if (m.matches()) {
        data.strDate = m.group(1);
        data.strTime = m.group(2);
      }
    }
    
    @Override
    public String getFieldNames() {
      return append(super.getFieldNames(), " ", "TIME");
    }
  }

  private static final Pattern UNKNOWN_PATTERN
    = Pattern.compile("UNKNOWN.*");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (UNKNOWN_PATTERN.matcher(field).matches()) return;
      super.parse(field, data);
    }
  }
  
  private static final Pattern TRAILING_CODE_PATTERN
    = Pattern.compile("(.*)\\b(\\d{6})\\s*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher m = TRAILING_CODE_PATTERN.matcher(field);
      if (m.matches()) {
        field = m.group(1).trim();
        data.strCallId = m.group(2);
      }
      field = field.replace("S>", "").replace(">IC<", "");
      data.strSupp = append(data.strSupp, " ", field);
    }

    @Override
    public String getFieldNames() {
      return append(super.getFieldNames(), " ", "ID");
    }
  }
  
  private static final String[] CITY_LIST = {
    "ALBATROSS",
    "ASH GROVE",
    "AURORA",
    "BATTLEFIELD",
    "BOIS D'ARC",
    "CHESAPEAKE",
    "EBENEZER",
    "FAIR GROVE",
    "FREISTATT",
    "GREENE COUNTY",
    "HALL TOWN",
    "HALLTOWN",
    "HEATONVILLE",
    "HOBERG",
    "LOGAN",
    "MARIONVILLE",
    "MCKINLEY",
    "MILLER",
    "MINDEN",
    "MISSION HILLS",
    "MONETT",
    "MOUNT VERNON",
    "OAK GROVE HEIGHTS",
    "PARIS SPRINGS JUNCTION",
    "PHELPS",
    "PIERCE CITY",
    "PLANO",
    "PLEW",
    "REPUBLIC",
    "RESCUE",
    "ROGERSVILLE",
    "SPENCER",
    "SPRINGFIELD",
    "STOTTS CITY",
    "STRAFFORD",
    "TURNERS",
    "VERONA",
    "WALNUT GROVE",
    "WILLARD"
  };
}
