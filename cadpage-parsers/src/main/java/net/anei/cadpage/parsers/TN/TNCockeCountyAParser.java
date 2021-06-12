package net.anei.cadpage.parsers.TN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TNCockeCountyAParser extends FieldProgramParser {
  
  public TNCockeCountyAParser() {
    super("COCKE COUNTY", "TN", 
          "ID ADDR INFO! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "no_reply@traumasoft.com,paramedic1242@gmail.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    data.strCall = "ALERT";
    return parseFields(body.split("/ :", -1), data);
  }
  
  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d+", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern CITY_PTN = Pattern.compile("[A-Z ]+");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String[] parts = field.split(",");
      if (parts.length < 2 && parts.length > 3) abort();
      data.strPlace = parts[0].trim();
      parseAddress(parts[1].trim(), data);
      if (parts.length == 3) {
        String apt = parts[2].trim();
        if (apt.startsWith("#")) {
          apt = apt.substring(1).trim();
        } else if (CITY_PTN.matcher(apt).matches()) {
          data.strCity = apt;
          return;
        }
        data.strApt = append(data.strApt, "-", apt);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      super.parse(field, data);
    }
  }
}
