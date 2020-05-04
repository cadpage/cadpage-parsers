package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA29Parser;

public class MOHowardCountyParser extends DispatchA29Parser {
  
  public MOHowardCountyParser() {
    super(CITY_LIST, "HOWARD COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "notification@howardcountye911.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strAddress = stripFieldStart(data.strAddress, "*");
    return true;
  }

  private static final String[] CITY_LIST = new String[]{
      "ARMSTRONG",
      "BOONESBORO",
      "FAYETTE",
      "ESTILL",
      "FRANKLIN",
      "GLASGOW",
      "NEW FRANKLIN",
      "ROANOKE",
      "SEBREE"
  };

}
