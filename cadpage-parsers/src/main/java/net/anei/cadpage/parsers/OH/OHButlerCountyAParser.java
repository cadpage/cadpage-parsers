package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;


public class OHButlerCountyAParser extends DispatchA3Parser {
  
  private static final Pattern SPECIAL_COMMENT_PTN = Pattern.compile(" Special Comment:+ *\\*([\\w ]+)\\*");
  
  public OHButlerCountyAParser() {
    super(0, "BCSO:", "BUTLER COUNTY", "OH", FA3_NBH_PLACE_OFF);
    setupProtectedNames("SURFACE LOT");
  }
  
  protected boolean parseMsg(String body, Data data) {
    body = SPECIAL_COMMENT_PTN.matcher(body).replaceAll(" $1");
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getFilter() {
    return "BCSO@butlersheriff.org";
  }
}
