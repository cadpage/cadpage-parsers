package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class MOHenryCountyParser extends DispatchA48Parser {
  
  public MOHenryCountyParser() {
    super(CITY_LIST, "HENRY COUNTY", "MO", FieldType.PLACE, A48_ONE_WORD_CODE);
    setupCallList(CALL_LIST);
  }
  
  private static final CodeSet CALL_LIST = new  CodeSet(
      
      "ASSIST",
      "DEATH",
      "MVA",
      "MED",
      "TRANSPAT",
      "SUICIDE"
    
  );

  private static final String[] CITY_LIST = new String[]{

      "BLAIRSTOWN",
      "BROWNINGTON",
      "CALHOUN",
      "CLINTON",
      "DEEPWATER",
      "HARTWELL",
      "LA DUE",
      "MONTROSE",
      "TIGHTWAD",
      "URICH",
      "WINDSOR"
      
  };
}
