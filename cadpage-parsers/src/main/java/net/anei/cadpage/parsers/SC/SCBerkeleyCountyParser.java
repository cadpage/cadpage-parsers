package net.anei.cadpage.parsers.SC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class SCBerkeleyCountyParser extends DispatchB2Parser {

  public SCBerkeleyCountyParser() {
    super(CITY_LIST, "BERKELEY COUNTY", "SC", B2_SEPARATE_APT_FLD);
    setupCallList((CodeSet)null);
  }
  
  private static final Pattern EXTRA_CODE_PTN = Pattern.compile("^(([A-Z0-9]+)) ++(?!>)");
  private static final Pattern EXTRA_PHONE_PTN = Pattern.compile(" {4,}(\\d+)(?=\n)");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("TICKET:")) return false;
    
    if (body.startsWith("Return Phone:")) {
      int pt = body.indexOf('\n');
      if (pt < 0) return false;
      data.strPhone = body.substring(13, pt).trim();
      body = body.substring(pt+1).trim();
    }
    
    Matcher match = EXTRA_CODE_PTN.matcher(body);
    if (match.lookingAt()) {
      body = match.group(1)+'>' + body.substring(match.end());
      
      match = EXTRA_PHONE_PTN.matcher(body);
      if (match.find()) {
        body = body.substring(0,match.start()) +  body.substring(match.end());
        if (data.strPhone.length() == 0) {
          String phone = match.group(1);
          if (phone.length() == 7 || phone.length() == 10) data.strPhone = phone;
        }
      }
    }
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "PHONE? " + super.getProgram();
  }

  @Override
  public String adjustMapCity(String city) {
    String tmp = MAP_CITY_TABLE.getProperty(city.toUpperCase());
    if (tmp != null) city = tmp;
    return city;
  }

  @Override
  protected Result parseAddress(StartType sType, int flags, String address) {
    address = address.replace('@', '&');
    return super.parseAddress(sType, flags, address);
  }

  private static final String[] CITY_LIST = new String[]{

      //Cities
      "CHARLESTON",
      "GOOSE CREEK",
      "HANAHAN",
      "NORTH CHARLESTON",

      //Towns
      "BONNEAU",
      "JAMESTOWN",
      "MONCKS CORNER",
      "ST STEPHEN",
      "SUMMERVILLE",

      //Townships
      "CROSS",
      "GUMVILLE",
      "LADSON",
      "PINOPOLIS",
      
      // Unincorporated communities
      "CORDESVILLE",
      "HUGER",
      "PINEVILLE",
      "SANGAREE",
      "SANTEE CIRCLE",
      
      // Other neighborhoods
      "LAKE MOULTRIE",
      "LEBANON",
      "MACEDONIA",
      "SPRING LAKE VILLAGE",
      "VILLAGE GREEN",
      "WOODSIDE",
      
      // Charleston County
      "CAINHOY",
      "WANDO",
      
      // Dorchester County
      "RIDGEVILLE",
      
      // Orangeburg County
      "HOLLY HILL"
  };
  
  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "CAINHOY",                 "CHARLESTON",
      "LAKE MOULTRIE",           "BONNEAU",
      "MACEDONIA",               "BONNEAU",
      "SANTEE CIRCLE",           "BONNEAU",
      "SPRING LAKE VILLAGE",     "SUMMERVILLE",
      "VILLAGE GREEN",           "SUMMERVILLE",
      "WOODSIDE",                "LADSON"
  });
}
