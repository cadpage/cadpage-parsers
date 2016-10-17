package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Cuyahoga County, OH
 */
public class DispatchA39Parser extends FieldProgramParser {

  private static final String PROGRAM_STR = "DEMPTY+? CALL? ADDR/iS6! APT? INFO/N+";

  public DispatchA39Parser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState, PROGRAM_STR);
  }

  public DispatchA39Parser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState, PROGRAM_STR);
  }

  @Override
  protected boolean keepLeadBreak() {
    return true;
  }

  @Override
  public boolean parseUntrimmedMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch Message")) return false;
    if (!parseFields(body.split("\n",-1), 2, data)) return false;
    if (data.strCall.length() == 0 && data.strSupp.length() == 0) data.strCall = "ALERT";
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DEMPTY")) return new MyDoubleEmptyField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("APT")) return new BaseAptField();
    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }
  
  private class MyDoubleEmptyField extends SkipField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      return field.length() == 0 && getRelativeField(+1).length() == 0;
    }
  }
  
  private static final Pattern DIR_BOUND_PTN = Pattern.compile("\\b([NSEW])/B\\b");
  private class MyAddressField extends AddressField {
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() == 0) return false;
      field = DIR_BOUND_PTN.matcher(field).replaceAll("$1B");
      return super.checkParse(field, data);
    }
    
    @Override
    public void parse(String field, Data data) {
      field = DIR_BOUND_PTN.matcher(field).replaceAll("$1B");
      super.parse(field,  data);
    }
  }
  
  private class BaseAptField extends AptField {
    
    public BaseAptField() {
      super("APT *(\\S+)", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      if (data.strApt.equals(field)) return;
      super.parse(field, data);
    }
  }
  
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("(?:-- +)?\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d Disp +\\S+[- ]*|[- ]+");
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_JUNK_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      if (data.strCall.length() == 0 && data.strSupp.length() == 0 && field.length() <= 40) {
        data.strCall = field;
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CALL " + super.getFieldNames();
    }
  }
}
