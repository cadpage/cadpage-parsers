package net.anei.cadpage.parsers.IN;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Adams County, IN
 */
public class INAdamsCountyParser extends DispatchOSSIParser {
  
  public INAdamsCountyParser() {
    super(CITY_LIST, "ADAMS COUNTY", "IN",
           "CALL ADDR/S! CITY? INFO+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@co.adams.in.us";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strAddress = fixAddress(data.strAddress);
    if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
    if (OHIO_CITY_LIST.contains(data.strCity)) data.strState = "OH";
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }
  
  private static final Pattern DIR_RT_PTN = Pattern.compile("\\b([NSEW]) (\\d+)");
  private String fixAddress(String addr) {
    Matcher match = DIR_RT_PTN.matcher(addr);
    if (match.find()) {
      StringBuffer sb = new StringBuffer();
      do {
        String dir = match.group(1);
        String num = match.group(2);
        String type;
        switch (Integer.parseInt(num)) {
        case 27:
        case 33:
        case 224:
          type = "US";
          break;
          
        case 101:
        case 115:
        case 124:
        case 218:
          type = "IN";
          break;
        
        default:
          type = null;
        }
        if (type != null) match.appendReplacement(sb, dir + ' ' + type + ' ' + num);
        
      } while (match.find());
      match.appendTail(sb);
      addr = sb.toString();
    }
    return addr;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }
  
  private class MyCityField extends CityField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strCity.length() > 0) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      data.strCity = convertCodes(field, CITY_CODES);
    }
  }

  // In county use city codes
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BER", "BERNE",
      "DEC", "DECATUR",
      "MON", "MONROE",
      "GEN", "GENEVA",
      "LIN", "LIN GROVE"      
  });
  
  // out of county use full city/county names
  private static final String[] CITY_LIST = new String[]{
    "WELLS COUNTY",
    "WELLS CO",
    "BLUFFTON",
    "OSSIAN",
    "UNIONDALE",
    "CRAIGVILLE",
    "PONETO",
    "TOCSIN",
    "MARKLE",
    "KEYSTONE",

    "ALLEN COUNTY",
    "ALLEN CO",
    "POE",
    "HOAGLAND",
    "ZANESVILLE",
    "MONROEVILLE",
    "YODER",

    "JAY COUNTY",
    "JAY CO",
    "BRYANT",
    "PORTLAND",
    "PENNVILLE",

    "VAN WERT COUNTY",
    "VAN WERT CO",
    "WILLSHIRE",
    "CONVOY",
    "VAN WERT",

    "MERCER COUNTY",
    "MERCER CO",
    "ROCKFORD"
  };
  
  // Some of whhich are in Ohio
  private Set<String> OHIO_CITY_LIST = new HashSet<String>(Arrays.asList(
    "VAN WERT COUNTY",
    "WILLSHIRE",
    "CONVOY",
    "VAN WERT",

    "MERCER COUNTY",
    "ROCKFORD"
  ));
}
