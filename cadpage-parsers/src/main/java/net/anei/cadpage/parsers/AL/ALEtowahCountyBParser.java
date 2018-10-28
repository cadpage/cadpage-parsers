package net.anei.cadpage.parsers.AL;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class ALEtowahCountyBParser extends DispatchA65Parser {
  
  public ALEtowahCountyBParser() {
    super(CITY_LIST, "ETOWAH COUNTY", "AL");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@911comm2.info,dispatch@etowah911.info";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBreakIns() { return true; }
    };
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    String city = MISSPELLED_CITIES.getProperty(data.strCity.toUpperCase());
    if (city != null) data.strCity = city;
    return true;
  }

  private static final Properties MISSPELLED_CITIES = buildCodeTable(new String[]{
      "GLECNCOE",     "GLENCOE",
      "GLENCO",       "GLENCOE",
      "GLENOCE",      "GLENCOE"
  });
  
  private static final String[] CITY_LIST = new String[]{
    
      // Cities
      "ATTALLA",
      "BOAZ",
      "EAST GADSDEN",
      "GADSDEN",
      "GLENCO",  // Misspelled
      "GLENCOE",
      "GLENOCE", // Misspelled
      "GLECNCOE",  // Also mispelled
      "HOKES BLUFF",
      "RAINBOW CITY",
      "SOUTHSIDE",

      // Towns
      "ALTOONA",
      "REECE CITY",
      "RIDGEVILLE",
      "SARDIS CITY",
      "WALNUT GROVE",

      // Census-designated places
      "BALLPLAY",
      "BRISTOW COVE",
      "CARLISLE-ROCKLEDGE",
      "COATS BEND",
      "EGYPT",
      "GALLANT",
      "IVALEE",
      "LOOKOUT MOUNTAIN",
      "NEW UNION",
      "TIDMORE BEND",
      "WHITESBORO",

      // Unincorporated communities
      "ANDERSON",
      "LIBERTY HILL",
      "MOUNTAINBORO",
      "BOAZ",
      "PILGRIMS REST",
      "BAIRDVILLE",
      
      // Calhoun County
      "PIEDMONT",
      
      // Cherokee County
      "CENTRE",
      
      // Dekalb County
      "COLLINSVILLE"

  };
}
