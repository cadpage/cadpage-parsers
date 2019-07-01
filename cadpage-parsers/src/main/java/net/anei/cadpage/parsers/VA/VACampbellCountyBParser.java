package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VACampbellCountyBParser extends FieldProgramParser {
  
  public VACampbellCountyBParser() {
    super("CAMPBELL COUNTY", "VA", "UNIT ID DATETIME ADDRCITY PLACE X INFO! END");
  }
  
  @Override
  public String getFilter() {
    return "administrator@co.campbell.va.us";
  }
  
  private static final Pattern DELIM = Pattern.compile(" *। *");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    data.strCall = subject;
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(';', ',').replaceAll(" ", "");
      super.parse(field, data);
    }
  }
  
  private static final Pattern ADDR_STATE_ZIP_PTN = Pattern.compile("(.*), *([A-Z]{2})(?: +(\\d{5}))?");
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String zip = null;
      Matcher match = ADDR_STATE_ZIP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strState = match.group(2);
        zip = match.group(3);
      }
      super.parse(field, data);
      if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }

  private static final Pattern INFO_PFX_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - ");
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      
      for (String line : field.split(";")) {
        line = line.trim();
        Matcher match = INFO_PFX_PTN.matcher(line);
        if (match.lookingAt()) line =  line.substring(match.end()).trim();
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
  }
}
