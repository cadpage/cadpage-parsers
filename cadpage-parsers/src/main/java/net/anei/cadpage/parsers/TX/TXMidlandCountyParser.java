package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXMidlandCountyParser extends FieldProgramParser {
  
  public TXMidlandCountyParser() {
    super("MIDLAND COUNTY", "TX", 
          "DATETIME CALL ADDR PLACE INFO! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "MidlandCommunications@midlandtexas.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Text Message")) return false;
    return parseFields(body.split(";", -1), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("CALL")) return new CallField("[A-Z]+", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern CR_PTN = Pattern.compile("\\bC R\\b");
  private static final Pattern FM_PTN = Pattern.compile("\\bF M\\b");
  private class MyAddressField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      field = CR_PTN.matcher(field).replaceAll("CR");
      field = FM_PTN.matcher(field).replaceAll("FM");
      super.parse(field, data);
    }
  }
  
  private static final Pattern INFO_GPS_PTN = Pattern.compile("([-+]?\\d{2,3}\\.\\d{6}, *[-+]?\\d{2,3}\\.\\d{6})[- ]*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_GPS_PTN.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(1), data);
        field = field.substring(match.end());
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "GPS " + super.getFieldNames();
    }
  }
}
