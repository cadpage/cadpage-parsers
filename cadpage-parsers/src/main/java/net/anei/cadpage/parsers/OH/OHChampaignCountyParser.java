package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class OHChampaignCountyParser extends FieldProgramParser {

  private static final Pattern SUBJECT_PTN = Pattern.compile("CAD Page(?: (\\d\\d-\\d{6}))?");
  public OHChampaignCountyParser() {
    super("CHAMPAIGN COUNTY", "OH",
           "DATETIME ADDR! X1:X! X2:X! CALL UNIT");
  }
  
  @Override
  public String getFilter() {
    return "champaign911@co.champaign.oh.us";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = getOptGroup(match.group(1));
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("(\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d),", true);
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      parseAddress(p.get(','), data);
      data.strCity = p.get(',');
      data.strSupp = p.get();
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY INFO";
    }
  }
}
