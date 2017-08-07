package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class GAEmanuelCountyParser extends DispatchB2Parser {

  public GAEmanuelCountyParser() {
    super("EMANUEL COUNTY E911:", CITY_LIST, "EMANUEL COUNTY", "GA");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "tehanners@gmail.com";
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "CALL N MAIN",
      "DELLWOOD MAIN",
      "GRIFFIN FERRY",
      "KING CIRCLE",
      "LAMBS BRIDGE",
      "LONG BAY",
      "MARTIN LUTHER KING",
      "MARY ANN",
      "MCLEOD BRIDGE",
      "MEDICAL CENTER",
      "MT OLIVET CHURCH",
      "OAK PARK",
      "PARK CIRCLE",
      "ROUNTREE FARM",
      "TONY BAKER",
      "UNKNOWN TYPE CARVER",
      "WADLEY COLEMAN LAKE"

  };
  
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
      "MISC CALL",
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
