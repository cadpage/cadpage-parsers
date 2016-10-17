package net.anei.cadpage.parsers.AK;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class AKMatanuskaSusitnaBoroughParser extends FieldProgramParser {
  
  public AKMatanuskaSusitnaBoroughParser() {
    super("MATANUSKA-SUSITNA BOROUGH", "AK", 
          "CALL MAP ADDR UNIT INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "response@ak-matanuska-5-1.fastalerting.com";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("(.*) ALERT!! \\((\\d{4}-\\d{8})\\)");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strSource = match.group(1).trim();
    data.strCallId = match.group(2).trim();
    
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "SRC ID " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("MAP")) return new MapField("\\d{4}|UKN", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strPlace = p.getOptional('@');
      String city = p.getLastOptional(',');
      if (city.equals("AK")) city = p.getLastOptional(',');
      data.strCity = city;
      parseAddress(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY";
    }
  }
  
  private static final Pattern INFO_GPS_PTN = Pattern.compile("([-+]\\d{2} \\d+\\.\\d{2,}) ([-+]\\d{2,3} \\d+\\.\\d{2,})\\b[- ]*(.*)");
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_GPS_PTN.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(1)+','+match.group(2), data);
        field = match.group(3);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "GPS " + super.getFieldNames();
    }
  }

}
