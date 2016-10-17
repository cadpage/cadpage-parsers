package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYNassauCountyIParser extends FieldProgramParser {
  
  private static final Pattern DELIM_PTN = Pattern.compile("(?<!\\*)\\*\\*(?!\\*)|(?=ADTNL:|MAP \\d)");
  
  public NYNassauCountyIParser() {
    super("NASSAU COUNTY", "NY",
          "TIMEDATE CALL CODE_MAP ID_ADDR! CS:X? MAP? ADTNL:INFO");
          removeWords("COVE");
          setupMultiWordStreets(
              "GLEN COVE",
              "MOTTS COVE"); 
  }
  
  @Override
  public String getFilter() {
    return "gfcoalarm@gmail.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    return parseFields(DELIM_PTN.split(body), data);
  }
  
  private static Pattern CODE_MAP_PTN = Pattern.compile(" *\\((.*)\\)$");
  private class CodeMapField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_MAP_PTN.matcher(field);
      if (match.find()) {
        data.strMap = match.group(1).trim();
        field = field.substring(0,match.start());
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE MAP";
    }
  }
  
  private static Pattern ID_ADDR_PTN = Pattern.compile("(\\d{4}-\\d{6}) *\\.? *(.*)");
  private class IdAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('.', ' ').replaceAll("  +", " ");
      Matcher match = ID_ADDR_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCallId = match.group(1);
      String addr = match.group(2);
      parseAddress(StartType.START_PLACE, FLAG_START_FLD_NO_DELIM | FLAG_ANCHOR_END, addr, data);
      if (data.strAddress.length() == 0) {
        parseAddress(data.strPlace, data);
        data.strPlace = "";
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ID PLACE ADDR APT";
    }
  }
  
  private static final Pattern MAP_PTN = Pattern.compile("MAP +(\\d+)(.*)");
  private class MyMapField extends MapField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override 
    public boolean checkParse(String field, Data data) {
      Matcher match = MAP_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strMap = append(data.strMap, " / ", match.group(1).trim());
      data.strSupp = match.group(2).trim();
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "MAP INFO";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIMEDATE"))  return new TimeDateField("\\d\\d:\\d\\d \\d\\d/\\d\\d/\\d\\d", true);
    if (name.equals("CODE_MAP")) return new CodeMapField();
    if (name.equals("ID_ADDR"))  return new IdAddressField();
    if (name.equals("MAP")) return new MyMapField();
    return super.getField(name);
  }
}
