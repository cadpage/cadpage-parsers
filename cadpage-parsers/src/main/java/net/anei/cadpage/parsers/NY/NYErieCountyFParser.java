package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYErieCountyFParser extends FieldProgramParser {
  
  public NYErieCountyFParser() {
    super(CITY_CODES, "ERIE COUNTY", "NY",
          "PRI CALL ADDR/S! CITY? INFO");
    setupCities(CITY_LIST);
    setupProtectedNames("EAST AND WEST");
  }
  
  @Override
  public String getFilter() {
    return "@alert.erie.gov,messaging@iamresponding.com";
  }
  
  private static final Pattern SUBJECT_SRC_PTN = Pattern.compile("[A-Z]{3,5}");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (SUBJECT_SRC_PTN.matcher(subject).matches()) data.strSource = subject;
    if (!body.startsWith("*")) return false;
    body = body.substring(1).trim();
    body = body.replace("EAST & WEST", "EAST AND WEST");
    return parseFields(body.split("~",-1), 4, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new PriorityField("\\d", true);
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "WSE", "WEST SENECA",
      
      "LANCASTER VILL", "LANCASTER"
  });
  
  private static final String[] CITY_LIST = new String[]{
    "DEPEW",
    "LANCASTER",
    "LANCASTER TOWN",
    "WEST SENECA"
  };
}
