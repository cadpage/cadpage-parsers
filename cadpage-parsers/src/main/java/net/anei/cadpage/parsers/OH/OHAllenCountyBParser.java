package net.anei.cadpage.parsers.OH;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;



public class OHAllenCountyBParser extends DispatchEmergitechParser {
  
  public OHAllenCountyBParser() {
    super(CITY_LIST, "ALLEN COUNTY", "OH", TrailAddrType.PLACE);
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){};
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Units get incorrectly identified as the subject and have to be restored
    if (subject.length() > 0 && body.startsWith("-")) {
      body = '[' + subject + ']' + body;
    }
    return super.parseMsg(body, data);
  }
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "2277 W BREESE RD",                     "+40.686152,-84.146268"
  });

  private static final String[] CITY_LIST = new String[]{
    
      //Cities
      
      "DELPHOS",
      "LIMA",

      //Villages

      "BEAVERDAM",
      "BLUFFTON",
      "CAIRO",
      "ELIDA",
      "HARROD",
      "LAFAYETTE",
      "SPENCERVILLE",

      //Townships

      "AMANDA TWP",
      "AMERICAN TWP",
      "AUGLAIZE TWP",
      "BATH TWP",
      "JACKSON TWP",
      "MARION TWP",
      "MONROE TWP",
      "PERRY TWP",
      "RICHLAND TWP",
      "SHAWNEE TWP",
      "SPENCER TWP",
      "SUGAR CREEK TWP",

      //Unincorporated communities

      "ALLENTOWN",
      "AUGLAIZE",
      "CONANT",
      "FORT SHAWNEE",
      "GOMER",
      "HUME",
      "KEMP",
      "LANDECK",
      "NEEDMORE",
      "OAKVIEW",
      "ROCKPORT",
      "ROUSCULP",
      "SCOTTS CROSSING",
      "SLABTOWN",
      "SOUTH WARSAW",
      "SOUTHWORTH",
      "WEST NEWTON",
      "WESTMINSTER",
      "YODER"
  };
}
