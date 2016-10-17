package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;



public class OHAllenCountyBParser extends DispatchEmergitechParser {
  
  public OHAllenCountyBParser() {
    super(CITY_LIST, "ALLEN COUNTY", "OH", TrailAddrType.PLACE);
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){};
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Units get incorrectly identified as the subject and have to be restoredd
    if (subject.length() > 0 && body.startsWith("-")) {
      body = '[' + subject + ']' + body;
    }
    return super.parseMsg(body, data);
  }

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
