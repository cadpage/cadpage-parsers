package net.anei.cadpage.parsers.PA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 *  Elk County, PA (also dispatches Cameron County and apparently Clearfield County) 
 */

public class PAElkCountyParser extends FieldProgramParser {
  
  private static final Properties CITY_TABLE = buildCodeTable(new String[]{
      "JOHNSBURG", "JOHNSONBURG",
      "RIDGWAY_B", "RIDGWAY",
      "ST_MARYS", "ST MARYS"
  }); 
  
  public  PAElkCountyParser() {
    super(CITY_TABLE, "ELK COUNTY", "PA",
          "Inc_Code:CALL! Address:ADDR! City:CITY! Cross_Streets:X? Agency:SRC! INFO+? DATETIME");
  }
  
  @Override
  public String getFilter() {
    return "alerts@elkcounty911.ealertgov.com";
  }
  
  @Override
  public String adjustMapAddress(String address) {
    return WATER_STREET_EXT.matcher(address).replaceAll("$1 EXD");
  }
  private static final Pattern WATER_STREET_EXT = Pattern.compile("\\b(WATER ST(?:REET)?) EXT?\\b");

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    body = body.replace("Inc:", "Inc Code:").replace(" Add:", "\nAddress:").replace("\nXSt:", "\nCross Streets:");
    return parseFields(body.split("\n"), 5, data);
  }
  
  private final static DateFormat DATE_TIME_FMT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField(DATE_TIME_FMT, true);
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("*", "/");
      super.parse(field, data);
    }
  }
}
