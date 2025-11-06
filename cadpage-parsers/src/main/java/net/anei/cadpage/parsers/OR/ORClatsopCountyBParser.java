package net.anei.cadpage.parsers.OR;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ORClatsopCountyBParser extends FieldProgramParser {
  
  public ORClatsopCountyBParser() {
    super("CLATSOP COUNTY", "OR", 
          "DATETIME CODE CALL ADDRCITYST EMPTY NAME PHONE UNIT! INFO! TIMES EMPTY END");
    setupGpsLookupTable(ORClatsopCountyParser.GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "no-reply@astoria.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("FIRE / EMS CALL")) return false;
    return parseFields(body.split("~", -1), data);
  }
  
  @Override
  protected boolean parseFields(String[] flds, Data data) {
    for (int j = 0; j<flds.length; j++) {
      if (flds[j].equals("None")) flds[j] = "";
    }
    return super.parseFields(flds, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("TIMES")) return new MyTimesField();
    return super.getField(name);
  }
  
  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }
  
  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", "\n");
      super.parse(field, data);
    }
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equals("CAPE FALCON")) city = "ARCH CAPE";
    return city;
  }

  @Override
  protected String adjustGpsLookupAddress(String address) {
    return ORClatsopCountyParser.doAdjustGpsLookupAddress(address);
  }
}
