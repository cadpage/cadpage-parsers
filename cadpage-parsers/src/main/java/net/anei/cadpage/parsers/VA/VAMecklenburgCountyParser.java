package net.anei.cadpage.parsers.VA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VAMecklenburgCountyParser extends FieldProgramParser {
  
  public VAMecklenburgCountyParser() {
    super("MECKLENBURG COUNTY", "VA", 
          "DATETIME CALL ADDRCITY PLACE X CALL/SDS! UNIT ID  EMPTY+ END");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.endsWith(":"))  body = body + '\n';
    if (!parseFields(body.split(":?\n"), data)) return false;
    if (data.strCity.equals("MECK CO")) data.strCity = "MECKLENBURG COUNTY";
    else if (data.strCity.equals("BRUN CO")) data.strCity = "BRUNSWICK COUNTY";
    else if (data.strCity.equals("LACROSSE")) data.strCity = "LA CROSSE";
    else if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
    
    if (data.strCity.endsWith(" APT") || data.strCity.endsWith(" APTS")) {
      data.strPlace = append(data.strPlace, " - ", data.strCity);
      data.strCity = convertCodes(data.strCity, CITY_MAP_TABLE);
    }
    
    // Only dispatch center we know that spells out highway numbers :(
    data.strAddress = fixHwyNumbers(data.strAddress);
    data.strCross = fixHwyNumbers(data.strCross);
    return true;
  }
  
  private String fixHwyNumbers(String address) {
    address = HWY_1_PTN.matcher(address).replaceAll("1");
    address = HWY_15_PTN.matcher(address).replaceAll("15");
    address = HWY_47_PTN.matcher(address).replaceAll("47");
    address = HWY_49_PTN.matcher(address).replaceAll("49");
    address = HWY_58_PTN.matcher(address).replaceAll("58");
    address = HWY_85_PTN.matcher(address).replaceAll("85");
    address = HWY_92_PTN.matcher(address).replaceAll("92");
    
    Matcher match = MULT_DIGIT_PTN.matcher(address);
    if (match.find()) {
      StringBuffer sb = new StringBuffer();
      do {
        String replace = match.group();
        try {
          StringBuilder sb2 = new StringBuilder();
          for (String digit : replace.split("-")) {
            int dig = DIGITS.valueOf(digit).ordinal();
            sb2.append((char)(dig + '0'));
          }
          replace = sb2.toString();
        } catch (IllegalArgumentException ex) {}
        match.appendReplacement(sb, replace);
      } while (match.find());
      match.appendTail(sb);
      address = sb.toString();
    }
    return address;
  }
  private static final Pattern HWY_1_PTN = Pattern.compile("\\bONE\\b");
  private static final Pattern HWY_15_PTN = Pattern.compile("\\bFIFTEEN\\b");
  private static final Pattern HWY_47_PTN = Pattern.compile("\\bFORTY-SEVEN\\b"); 
  private static final Pattern HWY_49_PTN = Pattern.compile("\\bFORTY-NINE\\b"); 
  private static final Pattern HWY_58_PTN = Pattern.compile("\\bFIFTY-EIGHT\\b"); 
  private static final Pattern HWY_85_PTN = Pattern.compile("\\bEIGHTY-FIVE\\b"); 
  private static final Pattern HWY_92_PTN = Pattern.compile("\\bNINETY-TWO\\b"); 
  private static final Pattern MULT_DIGIT_PTN = Pattern.compile("\\b[A-Z]+(?:-[A-Z]+){2,3}");
  
  private enum DIGITS {O, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE};
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field,  data);
    }
  }
  
  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, CITY_MAP_TABLE);
  }
  
  private static final Properties CITY_MAP_TABLE = buildCodeTable(new String[]{
      "NEWTONS M H PK",     "BUFFALO JUNCTION",
      
      "AMERICAMPS",           "BRACEY",
      "CHAMPION FOREST",      "BRACEY",
      "HOLLY GROVE SUB",      "BRACEY",
      "JOYCEVILLE SUBD",      "BRACEY",
      "RIVER RIDGE SUB",      "BRACEY",
      "TANGLEWOOD SHORES ASSOCIATION", "BRACEY",
      "TANGLEWOOD SUB",       "BRACEY",
      
      "HILLCREST MHP",        "LA CROSSE",
      "GREAT CREEK SUB",      "LA CROSSE",
      "PINE CREEK APTS",      "LA CROSSE",
      
      "MORRISTOWN SUBD",      "LITTLETON",
      
      "PLANTERWOOD APT",      "SOUTH HILL"
  });
}
