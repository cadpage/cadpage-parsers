package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class KSMarionCountyAParser extends DispatchB2Parser {
  
  public KSMarionCountyAParser() {
    super("MARION911:", CITY_LIST, "MARION COUNTY", "KS", B2_OPT_CALL_CODE);
  }
  
  @Override
  public String getFilter() {
    return "MARION911@marioncoks.net";
  }
    
  private static final String[] CITY_LIST = new String[]{
    "ANTELOPE",
    "AULNE",
    "BURNS",
    "CANADA",
    "DURHAM",
    "EASTSHORE",
    "FLORENCE",
    "GOESSEL",
    "HILLSBORO",
    "LEHIGH",
    "LINCOLNVILLE",
    "LOST SPRINGS",
    "MARION",
    "MARION COUNTY LAKE",
    "NEWTON",
    "PEABODY",
    "PILSEN",
    "RAMONA",
    "TAMPA"
    };


}
