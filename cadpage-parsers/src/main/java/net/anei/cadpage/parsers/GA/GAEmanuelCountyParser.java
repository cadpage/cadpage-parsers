package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class GAEmanuelCountyParser extends DispatchB2Parser {

  public GAEmanuelCountyParser() {
    super("EMANUEL COUNTY E911:", CITY_LIST, "EMANUEL COUNTY", "GA");
    setupCallList(CALL_LIST);
  }
  
  @Override
  public String getFilter() {
    return "tehanners@gmail.com";
  }
  private static final CodeSet CALL_LIST = new CodeSet(
      
      "ABDOMINAL PAIN",
      "ALTERED MENTAL STATUS",
      "CHEST PAINS",
      "DIFFICULTY BREATHING",
      "DOMESTIC IN PROGRESS",
      "FIGHT",
      "FIRE",
      "GENERAL WEAKNESS",
      "LIFT ASSISTANCE",
      "MEDICAL CALL",
      "MISC",
      "MVA",
      "REPORT",     
      "SEIZURE"
      
      );
  
  private static final String[] CITY_LIST = new String[]{

      //cities
      
      "ADRIAN",
      "GARFIELD",
      "NUNEZ",
      "OAK PARK",
      "STILLMORE",
      "SUMMERTOWN",
      "SWAINSBORO",
      "TWIN CITY",

      //other

      "CANOOCHEE",
      "NORRISTOWN"

  };
}
