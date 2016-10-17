package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCSumterCountyBParser extends FieldProgramParser {
  
  public SCSumterCountyBParser() {
    super("SUMTER COUNTY", "SC", 
          "CALL? UNIT_NAME PLACE ( DX:CALL/SDS! | CALL/SDS ) CALL/SDS+", 
          FLDPROG_IGNORE_CASE);
  }
  
  @Override
  public String getFilter() {
    return "dispatch@midlandsmedtech.com";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("\\d+(?: *& *\\d+)?\\b[- ]*(.*)");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCall = match.group(1);
    return parseFields(body.split("\\n+"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT_NAME")) return new MyUnitNameField();
    return super.getField(name);
  }
  
  private static final Pattern UNIT_NAME_PTN = Pattern.compile("((?:[A-Z]+-\\d+ )+) *(.*)", Pattern.CASE_INSENSITIVE);
  private class MyUnitNameField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = UNIT_NAME_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strUnit = match.group(1).trim().toUpperCase();
      data.strName = match.group(2);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "UNIT NAME";
    }
  }
}
