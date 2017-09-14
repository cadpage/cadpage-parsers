package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILMacoupinCountyParser extends FieldProgramParser {
  public ILMacoupinCountyParser() {
    super("MACOUPIN COUNTY", "IL",
      "( ADDR_SRC_INFO | ADDRCITY PLACE? SRC! INFO CC_Text:CALL Problem:INFO | DISPATCH:EVERYTHING! )");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (! parseFields(body.split("\n+"), data)) return false;
    if (data.strCall.length() == 0) {
      data.strCall = data.strSupp;
      data.strSupp = "";
      if (data.strCall.length() == 0) data.strCall = "ALERT";
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " CALL"; 
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR_SRC_INFO")) return new MyAddressSrcInfoField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("SRC")) return new SourceField("[A-Z]{2,5}:.+", true);
    if (name.equals("EVERYTHING")) return new EverythingField();
    return super.getField(name);
  }
  
  private static Pattern ADDR_SRC_INFO_PTN = Pattern.compile("(.*?)(?:,([ A-Z]+))?//([_A-Z]{2,6}:[_A-Z0-9 :,]+)//(.*)");
  private class MyAddressSrcInfoField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = ADDR_SRC_INFO_PTN.matcher(field);
      if (!match.matches()) return false;
      parseAddress(match.group(1).trim(), data);
      data.strCity = getOptGroup(match.group(2));
      data.strSource = match.group(3).trim();
      data.strSupp = match.group(4).trim();
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY SRC INFO";
    }
    
  }
  
  static private final Pattern ADDRESS_PATTERN
    = Pattern.compile("(\\d+(?: +BLK)?),(.*)");
  private String fixAddress(String a) {
    Matcher m=ADDRESS_PATTERN.matcher(a);
    if (m.matches()) a=append(m.group(1), " ", m.group(2).trim());
    return a;
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(fixAddress(field), data);
    }
  }
  
  static private final Pattern EVERYTHING_PATTERN
    = Pattern.compile("([A-Z]{2,5}:.*?)- +(\\d{4})(.*)/(.*)");
  private class EverythingField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher m = EVERYTHING_PATTERN.matcher(field);
      if (!m.matches()) abort();
      data.strSource = m.group(1).trim();
      data.strCode = m.group(2);
      data.strCall = m.group(3).trim();
      super.parse(fixAddress(m.group(4).trim()), data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" SRC CODE CALL INFO";
    }
  }
  
  static private final Pattern PAMA_PATTERN  = Pattern.compile("(.*)-.*SECTOR");
  
  @Override
  public String adjustMapAddress(String a) {
    Matcher m = PAMA_PATTERN.matcher(a);
    if (m.matches()) a = m.group(1).trim();
    return a;
  }
}
