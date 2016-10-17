package net.anei.cadpage.parsers.MO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchGlobalDispatchParser;



public class MOPolkCountyParser extends DispatchGlobalDispatchParser {
  
  private static final Pattern UNIT_PTN = Pattern.compile("[MRC]\\d+|MSHP\\d|\\d{2,4}(?:-\\d)?|SJDC|SJLL|[A-Z]{1,2}FD");
  private static final Pattern CALL_CODE_PTN = Pattern.compile("/ *(\\d{1,2}-[A-Z]-\\d{1,2})$");
  private static final Pattern CITY_PTN = Pattern.compile("[A-Z ]+", Pattern.CASE_INSENSITIVE);
  
  public MOPolkCountyParser() {
    super(CITY_TABLE, "POLK COUNTY", "MO", CALL_FOLLOWS_ADDR | TRAIL_SRC_UNIT_ADDR, null, UNIT_PTN);
  }
  
  @Override
  public String getFilter() {
    return "911paging@polkco911.com";
  }
  
  @Override
  public boolean parseUntrimmedMsg(String subject, String body, Data data) {
    if (!subject.equals("RT:")) return false;
    if (body.startsWith("\nMO ")) body = body.substring(1);
    body = body.replace("=20\n", " \n");
    body = body.replace("Disp:", "Dispatch:");
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.equals("POLK COUNTY")) data.strCity = "";
    Matcher match = CALL_CODE_PTN.matcher(data.strCall);
    if (match.find()) {
      data.strCode = match.group(1);
      data.strCall = data.strCall.substring(0,match.start()).trim();
    }
    if (CITY_PTN.matcher(data.strMap).matches()) {
      if (data.strCity.length() == 0) {
        data.strCity = convertCodes(data.strMap, CITY_ABBRV_TABLE);
      }
      data.strMap = "";
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace(" CALL ", " CALL CODE ").replace(" MAP ", " MAP CITY ");
  }
  
  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      return super.checkParse(cleanCity(field), data);
    }
    
    @Override
    public void parse(String field, Data data) {
      super.checkParse(cleanCity(field), data);
    }
    
    private String cleanCity(String city) {
      if (city.endsWith(" MO")) city = city.substring(0,city.length()-3).trim();
      return city;
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }
  
  private static final String[] CITY_TABLE = new String[]{
    "ALDRICH",
    "BOLIVAR",
    "BRIGHTON",
    "DUNNEGAN",
    "EUDORA",
    "FAIR PLAY",
    "FLEMINGTON",
    "GOODSON",
    "GOODNIGHT",
    "HALFWAY",
    "HUMANSVILLE",
    "MORRISVILLE",
    "PLEASANT HOPE",
    "POLK",
    "TIN TOWN",
    
    "POLK COUNTY"
  };
  
  private static final Properties CITY_ABBRV_TABLE = buildCodeTable(new String[]{
      "Pleasant H", "Pleasant Hope",
      "Humansvill", "Humansville",
      "Morrisvill", "Morrisville"
  });
}
