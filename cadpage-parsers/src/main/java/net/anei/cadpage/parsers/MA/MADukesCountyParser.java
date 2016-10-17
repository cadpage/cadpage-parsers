package net.anei.cadpage.parsers.MA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MADukesCountyParser extends FieldProgramParser {
  public MADukesCountyParser() {
    super("DUKES COUNTY", "MA", 
          "DISP! CALL! ADDR1 X INFO INFO ADDR2 END");
  }
  
  @Override
  public String getFilter() {
    return "@mvpsis.com";
  }
  
  private static final Pattern PAMA_PATTERN
    = Pattern.compile("(.*)FL(?: +[A-Z]+ +SECTOR)?");
  @Override
  public String postAdjustMapAddress(String field) {
    Matcher m = PAMA_PATTERN.matcher(field);
    if (m.matches()) field = m.group(1).trim();
    return field.replace("EDG RD", "EDGARTOWN RD");
  }
  
  private static final Pattern DELIM = Pattern.compile("\\|");
  private static final Pattern SUBJECT_PATTERN
    = Pattern.compile("\\d{2}");
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    //check subject
    Matcher m = SUBJECT_PATTERN.matcher(subject);
    if (!m.matches()) return false;
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DISP")) return new SkipField("[a-z]+:\\d{2}", true);
    if (name.equals("ADDR1")) return new MyAddressField();
    if (name.equals("ADDR2")) return new SkipField();
    return super.getField(name);
  }

  private static final Pattern NAME_PATTERN
    = Pattern.compile("([A-Z]+(?:-[A-Z]+)?, +[A-Z]+(?: +[A-Z]+)?(?:\\ *& *[A-Z]+(?: +[A-Z])?)?)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int ndx = field.lastIndexOf(" / ");
      String aField;
      if (ndx > -1) {
        aField = field.substring(ndx+2).trim();
        field = field.substring(0, ndx);
      }
      else {
        aField = field;
        field = "";
      }
      aField = stripCity(aField, data);
      super.parse(aField, data);
      parsePlace(field, data);
    }
    
    private String stripCity(String field, Data data) {
      for (int i=0; i<CITY_LIST.length; i++) {
        int ndx = field.length() - CITY_LIST[i].length();
        if (ndx > 0)
          if (field.substring(ndx).equals(CITY_LIST[i])) {
            data.strCity = CITY_LIST[i];
            return field.substring(0, ndx).trim();
          }
      }
      return field;
    }
    
    private void parsePlace(String field, Data data) {
      Matcher m;
      int ndx = field.lastIndexOf('-');
      if (ndx > -1) {
        String nField = field.substring(0, ndx).trim();
        m = NAME_PATTERN.matcher(nField);
        if (m.matches()) {
          data.strName = nField;
          if (ndx < field.length()-1)
            data.strPlace = field.substring(ndx+1).trim();
        }
        else
          data.strPlace = field;
      }
      else {
        m = NAME_PATTERN.matcher(field);
        if (m.matches())
          data.strName = field;
        else
          data.strPlace = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" NAME PLACE CITY APT";
    }
  }
  
  private static final String[] CITY_LIST = {
    "WEST TISBURY",
    "EDGARTOWN",
    "OAK BLUFFS",
    "TISBURY",
  };
}
