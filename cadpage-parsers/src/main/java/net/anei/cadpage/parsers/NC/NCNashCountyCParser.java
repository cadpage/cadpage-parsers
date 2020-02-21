package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCNashCountyCParser extends FieldProgramParser {
  
  public NCNashCountyCParser() {
    super(NCNashCountyParser.CITY_LIST, "NASH COUNTY", "NC", 
          "CALL ADDRCITY PLACE GPS1 GPS2 NAME UNIT/C+? ID DATETIME! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "donotreply@nashcountync.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private String tmpSubject;
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) return false;
    tmpSubject = subject;
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("ID"))  return new IdField("CFS\\d\\d-\\d{6}", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d?:\\d\\d", true);
    return super.getField(name);
  }
  
  private static final Pattern ADDR_CH_PTN = Pattern.compile("(.*?) (TAC[- ]*\\d+)\\b *(.*)");
  private static final Pattern ADDR_ST_ZIP_PTN = Pattern.compile("(.*?), *([A-Z]{2})\\b(?: +(\\d{5}))?(?: (.*?))?");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.length() == 0) data.strCall = tmpSubject;
      
      String callExt2 = "";
      Matcher match = ADDR_CH_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strChannel = match.group(2);
        callExt2 = match.group(3);
      }

      String callExt;
      int pt = field.indexOf(',');
      if (pt >= 0) {
        parseAddress(field.substring(0,pt).trim(), data);
        field = field.substring(pt+1).trim();
        
        match = ADDR_ST_ZIP_PTN.matcher(field);
        if (match.matches()) {
          data.strCity = match.group(1).trim();
          data.strState = match.group(2);
          callExt = getOptGroup(match.group(4));
        } else {
          parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, field, data);
          callExt = getLeft();
        }
      }
      
      else {
        parseAddress(StartType.START_ADDR, field, data);
        callExt = getLeft();
      }
      
      data.strCity = convertCodes(data.strCity, NCNashCountyParser.CITY_FIXES);
      if (!callExt.equals("None")) data.strCall = append(data.strCall, " - ", callExt);
      if (!callExt2.equals("None")) data.strCall = append(data.strCall, " - ", callExt2);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST CH";
    }
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }
}
