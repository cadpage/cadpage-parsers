package net.anei.cadpage.parsers.PA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

/**
 * Clarion County, PA (C)
 */
public class PAClarionCountyCParser extends DispatchA48Parser {
  
  private static final Pattern INT_PTN = Pattern.compile("\\bINT\\b", Pattern.CASE_INSENSITIVE);
  
  public PAClarionCountyCParser() {
    super(PAClarionCountyParser.CITY_LIST, "CLARION COUNTY", "PA", FieldType.X_NAME, A48_ONE_WORD_CODE, 
          Pattern.compile("\\d{3,4}(?:[A-Z]+\\d?)?|911\\d{3}|CH\\d?-\\d+|EBR|EML(?:-[A-Z0-9]+)?|LIFEFLIGHT\\d|SOU|SPC\\d"));
  }
  
  @Override
  public String getFilter() {
    return "PageGate@OES.CLARION.PA.US";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = INT_PTN.matcher(body).replaceAll("INTERSTATE");
    if (!super.parseMsg(subject, body, data)) return false;
    PAClarionCountyParser.fixCity(data);
    return true;
  }
  
  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_PREF_TRAILING_BOUND;
  }
}
