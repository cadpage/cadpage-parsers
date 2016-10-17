package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Lee County, AL
 */
public class ALLeeCountyParser extends FieldProgramParser {  
  public ALLeeCountyParser() {
    super(CITY_LIST, "LEE COUNTY", "AL",
          "ADDR/S ID CALL! geo:GPS?");
  }
  
  private  static final Pattern MARKER = Pattern.compile("[A-Za-z]+:");
  private static final String ID_PATTERN_STRING = "\\d{4}-\\d{4}";
  private static final Pattern ID_PATTERN = Pattern.compile(ID_PATTERN_STRING);
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher m = MARKER.matcher(body);
    if (!m.lookingAt()) return false;
    body = body.substring(m.end()).trim();
    String[] field = body.split(";");
    if (ID_PATTERN.matcher(field[0]).matches()) {
      data.strCall = "RUN REPORT";
      data.strCallId = field[0];
      data.strPlace = body;
      return true;
    }
    return parseFields(field, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID")) return new IdField(ID_PATTERN_STRING, true);
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }

  private static final Pattern ADDRESS_PATTERN
    = Pattern.compile("(.*?LEE RD +\\d+ +)(\\d+)(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("\\", "&");
      Matcher m = ADDRESS_PATTERN.matcher(field);
      if (m.matches()) {
        field = m.group(1)+m.group(3);
        data.strApt = m.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" APT";
    }
  }
  
  private static final Pattern CALL_PATTERN
    = Pattern.compile("(\\d{2}-\\d{2})(.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher m = CALL_PATTERN.matcher(field);
      if (m.matches()) {
        data.strCode = m.group(1);
        data.strCall = m.group(2).trim();
      }
      else
        data.strCall = field;
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" CODE";
    }
  }
  private static final String[] CITY_LIST = {
    // Adjacent Counties
    "CHAMBERS COUNTY",
    "HARRIS COUNTY",
    "MUSCOGEE COUNTY",
    "RUSSELL COUNTY",
    "MACON COUNTY",
    "TALLAPOOSA COUNTY",

    // Cities
    "AUBURN",
    "OPELIKA",
    "PHENIX CITY",
    "SMITHS STATION",
    
    // Towns
    "LOACHAPOKA",
    "NOTASULGA",
    "WAVERLY",
    
    // Unincorporated Communities
    "BEAUREGARD",
    "BEE HIVE",
    "BEULAH",
    "CHEWACLA",
    "GOLD HILL",
    "HOPEWELL",
    "MARVYN",
    "ROXANA",
    "SALEM",
    "THE BOTTLE",
  };
}
