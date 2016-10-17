package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class KYMonroeCountyParser extends DispatchA65Parser {
  
  public KYMonroeCountyParser() {
    super(CITY_LIST, "MONROE COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "monroecoky@911email.net";
  }
  
  private static final String[] CITY_LIST = new String[]{

      "FOUNTAIN RUN",
      "GAMALIEL",
      "TOMPKINSVILLE",
      "BUGTUSSLE",
      "MOUNT HERMON"

  };
}
