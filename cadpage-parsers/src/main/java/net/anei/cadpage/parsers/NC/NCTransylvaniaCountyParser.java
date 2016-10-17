package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCTransylvaniaCountyParser extends FieldProgramParser {
  
  public NCTransylvaniaCountyParser() {
    super(CITY_LIST, "TRANSYLVANIA COUNTY", "NC", 
          "ADDR/S X? NAME EMPTY+? CALL INFO/CS+");
    setupProtectedNames("SEE OFF MOUNTAIN");
  }
  
  @Override
  public String getFilter() {
    return "911CENTER@transylvaniacounty.org";
  }
  
  private static final Pattern OCA_ID_PTN = Pattern.compile("[, ]+OCA: *([-0-9]+)$");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // String OCA ID from end of text
    Matcher match = OCA_ID_PTN.matcher(body);
    if (match.find()) {
      body = body.substring(0,match.start());
      data.strCallId = match.group(1);
    }
    
    return (parseFields(body.split(","), data));
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " ID";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("NAME")) return new MyNameField();
    return super.getField(name);
  }
  
  private static final Pattern CROSS_PTN = Pattern.compile("(.*)\\bX\\b(.*)");
  private class MyCrossField extends CrossField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CROSS_PTN.matcher(field);
      if  (!match.matches()) return false;
      data.strCross = append(match.group(1).trim(), " / ", match.group(2).trim());
      return true;
    }
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      if (NUMERIC.matcher(field).matches()) {
        data.strApt = append(data.strApt, "-", field);
      } else {
        super.parse(field, data);
      }
    }
  }
  
  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }
  
  private static Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "CONNESTEE",                "BREVARD",
      "CONNESTEE FALLS",          "BREVARD",
      "NORTH TRANSYLVANIA",       "TRANSYLVANIA COUNTY",
      "PISGAH NATIONAL FOREST",   "PISGAH FOREST",
      "SHERWOOD FOREST",          "BREVARD"
  });
  
  private static final String[] CITY_LIST = new String[]{
    "CASHIERS",
    "CONNESTEE",
    "CONNESTEE FALLS",
    "BALSAM GROVE",
    "BREVARD",
    "BREVARD CITY",
    "ETOWAH",
    "LAKE TOXAWAY",
    "LITTLE RIVER",
    "MILLS RIVER",
    "NORTH TRANSYLVANIA",
    "PENROSE",
    "PISGAH FOREST",
    "PISGAH NATIONAL FOREST",
    "ROSMAN",
    "SAPPHIRE",
    "SHERWOOD FOREST",
    
    "JACKSON COUNTY"
  };

}
