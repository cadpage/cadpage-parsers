package net.anei.cadpage.parsers.WV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;


public class WVPocahontasCountyParser extends DispatchEmergitechParser {
  
  private static final Pattern MARKER_PTN = Pattern.compile("^[A-Z0-9]+:");
  private static final Pattern GEN_ALERT_PTN = Pattern.compile("([A-Z0-9]+):\\[\\1\\][ -]*(.*)");
  
  public WVPocahontasCountyParser() {
    super(true, 60, CITY_LIST, "POCAHONTAS COUNTY", "WV");
    addSpecialWords("JERICO", "MOUNTAIN");
  }

  @Override
  public String getFilter() {
    return "@pocahontasemergency.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER_PTN.matcher(body);
    if (!match.find()) return false;
    if (super.parseMsg(body.substring(match.end()).trim(), data)) {
      if (data.strApt.length() > 5) {
        data.strCross = data.strApt;
        data.strApt = "";
      }
      if (data.strCity.equalsIgnoreCase("BUC")) data.strCity = "Buckeye";
      return true;
    }
    else {
      match = GEN_ALERT_PTN.matcher(body);
      if (!match.matches()) return false;
      data.initialize(this);
      data.strCall = "GENERAL ALERT";
      data.strUnit = match.group(1);
      data.strPlace = match.group(2);
      return true;
    }
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("APT", "APT X");
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR2")) return new MyAddressField();
    return super.getField(name);
  }
  
  private class MyAddressField extends BaseAddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(';', ' ').trim();
      super.parse(field, data);
    }
  }

  private static final String[] CITY_LIST = new String[]{

    // Towns
    "DURBIN",
    "HILLSBORO",
    "MARLINTON",
  "",
    // CDPs  
    "BARTOW",
    "CASS",
    "FRANK",
    "GREEN BANK",
    "HUNTERSVILLE",

    // Unincorporated communities   
    "ARBOVALE",
    "BEARD",
    "BEARD HEIGHTS",
    "BOYER",
    "BRAUCHER",
    "BROWNSBURG",
    "BUC",     // short for BUCKEYE
    "BUCKEYE",
    "BURNER",
    "BURNSIDES",
    "BURR",
    "CAMPBELLTOWN",
    "CLAWSON",
    "CLOVER LICK",
    "DEER CREEK",
    "DENMAR",
    "DILLEYS MILL",
    "DROOP",
    "DUNMORE",
    "EDRAY",
    "FROST",
    "GREENBANK",
    "HARTER",
    "HOSTERMAN",
    "IMPROVEMENT LICK",
    "JACOX",
    "KENNISON",
    "KNAPP",
    "LINWOOD",
    "LOBELIA",
    "LOCUST",
    "MACE",
    "MAY",
    "MILL POINT",
    "MINNEHAHA SPRINGS",
    "NIDA",
    "NOTTINGHAM",
    "OLIVE",
    "ONOTO",
    "RAINTOWN",
    "RAYWOOD",
    "RIMEL",
    "SEEBERT",
    "SITLINGTON",
    "SLATY FORK",
    "SNOWSHOE",
    "SPICE",
    "SPRUCE",
    "STILLWELL",
    "STONY BOTTOM",
    "THORNWOOD",
    "THORNY CREEK",
    "VIOLET",
    "WALNUT",
    "WANLESS",
    "WARWICK",
    "WATOGA",
    "WEST UNION",
    "WILDELL",
    "WOODROW",

    // Ghost towns   
    "GERTRUDE",
    "MILL RUN",
    "SUNSET"
  };
}
