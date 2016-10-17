package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class OHMedinaCountyParser extends FieldProgramParser {
  
  private static final Pattern SUBJECT_PTN = Pattern.compile(".CAD\\|!");
  public OHMedinaCountyParser() {
    super(CITY_LIST, "MEDINA COUNTY", "OH",
           "CALL! ADDR/S! INFO! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "@c-msg.net";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!isPositiveId() && !SUBJECT_PTN.matcher(subject).matches()) return false;
    return parseFields(body.split("//"), 3, data);
  }
  
  private static final Pattern BETWEEN_PTN = Pattern.compile("B *ETWEEN");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = BETWEEN_PTN.matcher(field);
      if (match.find()) {
        data.strCross = field.substring(match.end()).trim();
        field = field.substring(0,match.start()).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " X";
    }
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private static final String[] CITY_LIST = new String[]{
    "BRUNSWICK",
    "MEDINA",
    "RITTMAN",
    "WADSWORTH",
    
    "CHIPPEWA LAKE",
    "CRESTON",
    "GLORIA GLENS PARK",
    "LODI",
    "SEVILLE",
    "SPENCER",
    "WESTFIELD CENTER",
    
    "BRUNSWICK HILLS TWP",
    "CHATHAM TWP",
    "GRANGER TWP",
    "GUILFORD TWP",
    "HARRISVILLE TWP",
    "HINCKLEY TWP",
    "HOMER TWP",
    "LAFAYETTE TWP",
    "LITCHFIELD TWP",
    "LIVERPOOL TWP",
    "MEDINA TWP",
    "MONTVILLE TWP",
    "SHARON TWP",
    "SPENCER TWP",
    "WADSWORTH TWP",
    "WESTFIELD TWP",
    "YORK TWP",
    
    "BEEBETOWN",
    "HOMERVILLE",
    "LITCHFIELD",
    "VALLEY CITY",
  };
}
