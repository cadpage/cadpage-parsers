package net.anei.cadpage.parsers.UT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

public class UTWeberCountyAParser extends FieldProgramParser {
  public UTWeberCountyAParser() {
    super("WEBER COUNTY", "UT",
        "CALL_TIME! ADDR_MAP PLACE_X+? ( Ch:CH UNIT_ID! | UNIT_ID! ) Sent_by:SKIP!");
  }
  
  @Override
  public String getFilter() {
    return "FD3@slcgov.com";
  }
  @Override
  protected boolean parseMsg(String body, Data data) {
    String[] field = body.split("\n");
    return parseFields(field, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL_TIME")) return new MyCallTimeField();
    if (name.equals("ADDR_MAP")) return new MyAddrMapField();
    if (name.equals("PLACE_X")) return new MyPlaceCrossField();
    if (name.equals("CH")) return new MyChannelField("(?:\\d|FIRE TEST)", true);
    if (name.equals("UNIT_ID")) return new MyUnitIdField();
    return super.getField(name);
  }
  

  private static final Pattern CALL_TIME_PATTERN
    = Pattern.compile("(.*)\\b(\\d\\d:\\d\\d:\\d\\d)");
  private class MyCallTimeField extends CallField {
    
    @Override
    public void parse(String field, Data data) {
      Matcher m = CALL_TIME_PATTERN.matcher(field);
      if (!m.matches()) abort();
      field = m.group(1).trim();
      data.strTime = m.group(2);
      
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" TIME";
    }
  }
  
  private static final Pattern ADDR_MAP_PATTERN
    = Pattern.compile("(.*)Dt:(.*)Zn:(.*)Gd:(.*)");
  private class MyAddrMapField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      Matcher m = ADDR_MAP_PATTERN.matcher(field);
      if (!m.matches()) abort();
      field = m.group(1).trim();
      data.strMap = append(m.group(2).trim(), "-", append(m.group(3).trim(), "-", m.group(4).trim()));

      int ndx = field.indexOf('-');
      if (ndx > -1) {
        data.strApt = field.substring(0, ndx).trim();
        field = field.substring(ndx+1).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" APT MAP";
    }
  }
  
  private class MyPlaceCrossField extends Field {

    @Override
    public void parse(String field, Data data) {
      if (data.strCross.length() == 0) {
        data.strCross = field;
      } 
      else if (data.strPlace.length() == 0) {
        data.strPlace = data.strCross;
        data.strCross = field;
      }
      else {
        data.strCross = append(data.strCross, " & ", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE X";
    }
  }
  
  private class MyChannelField extends ChannelField {
    MyChannelField(String p, boolean h) {
      super(p, h);
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.equals("0")) field = "";
      super.parse(field, data);
    }
  }
  
  private static final Pattern UNIT_ID_PATTERN
    = Pattern.compile("(.*)\\b([A-Z]{2}) #(\\d+)");
  private class MyUnitIdField extends UnitField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher m = UNIT_ID_PATTERN.matcher(field);
      if (!m.matches()) return false;
      data.strUnit = m.group(1).trim();
      data.strSource = m.group(2);
      data.strCallId = m.group(3);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" SRC ID";
    }
  }
}
