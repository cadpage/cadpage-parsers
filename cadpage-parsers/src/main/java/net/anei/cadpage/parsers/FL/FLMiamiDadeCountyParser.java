package net.anei.cadpage.parsers.FL;

import java.util.regex.*;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class FLMiamiDadeCountyParser extends FieldProgramParser {
  
  public FLMiamiDadeCountyParser() {
    super("MIAMIDADE COUNTY", "FL",
          "SRC_UNIT! SIG:CALL! ADDR:ADDR! RECV:DATETIME! UNIT:SKIP! UNITS:SKIP! REMARKS:INFO!");
  }
  
  @Override
  public String getFilter() {
    return "mdfrstaffing@miamidade.gov";
  }

  @Override
  public String getLocName() {
    return "Miami-Dade County, FL";
  }
  
  private static final String SUBJECT_STRING = "TEST ACTIVE911";
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals(SUBJECT_STRING)) return false;
    
    return super.parseMsg(body.replace("UNIT:", " UNIT:"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC_UNIT")) return new SourceUnitField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
  
  private static final Pattern SOURCE_UNIT_PATTERN
    = Pattern.compile("(?:(.*?) +\\- +)?([A-Z]+) +\\- +([A-Z]\\d{2})");
  private class SourceUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      Matcher m = SOURCE_UNIT_PATTERN.matcher(field);
      if (!m.matches()) abort();
      data.strCall = getOptGroup(m.group(1));
      data.strSource = m.group(2);
      data.strUnit = m.group(3);
    }
    
    @Override
    public String getFieldNames() {
      return "SRC UNIT";
    }
  }

  private static final Pattern ADDRESS_PATTERN
    = Pattern.compile("(.*?)\\s*ALR +(\\d{6})");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher m = ADDRESS_PATTERN.matcher(field);
      if (m.matches()) {
        data.strCallId = m.group(2);
        super.parse(m.group(1), data);
      }
      else
        abort();
    }
    
    @Override
    public String getFieldNames() {
      return append(super.getFieldNames(), " ", "ID");
    }
  }
  
  private static final Pattern INFO_PATTERN
    = Pattern.compile("(.*?)\\:\\s*(.*)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String fragment : field.split(";")) {
        parseFragment(fragment.trim(), data);
      }
    }
    
    private void parseFragment(String frag, Data data) {
      Matcher m = INFO_PATTERN.matcher(frag);
      if (m.matches()) {
        if (m.group(1).equals("Incident Initiated By")) return;
        if (m.group(1).equals("Original Location ")) {
          data.strPlace = m.group(2);
          return;
        }
      }
      data.strSupp = append(data.strSupp, ";", frag);
    }
    
    @Override
    public String getFieldNames() {
      return append(super.getFieldNames(), " ", "PLACE");
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(field, " - ", data.strCall);
    }
  }
}
