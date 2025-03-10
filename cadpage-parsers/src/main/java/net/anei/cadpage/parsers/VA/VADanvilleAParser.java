package net.anei.cadpage.parsers.VA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class VADanvilleAParser extends DispatchA48Parser {

  public VADanvilleAParser() {
    super(CITY_LIST, "DANVILLE", "VA", FieldType.GPS_PLACE_X);
  }

  @Override
  public String getFilter() {
    return "DAN911ACTIVE911EMS@danvilleva.gov";
  }

  private static final Pattern BAD_FIELD_PTN = Pattern.compile("[A-Z]{3}|\\d{10}");

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "Text Message / ");
    if (!super.parseMsg(body, data)) return false;

    // We want to throw in something here that rejects VAPittsylvaniaCounty alerts that
    // have a very similar format
    if (data.strCall.equals("CANCEL") && data.strCity.length() == 0) return false;
    if (data.strCall.startsWith("20")) return false;
    if (data.strCallId.startsWith("20") && data.strCallId.compareTo("2019") >= 0) return false;
    if (BAD_FIELD_PTN.matcher(data.strCross).matches()) return false;
    if (BAD_FIELD_PTN.matcher(data.strAddress).matches()) return false;
    return true;
  }

  private static final String[] CITY_LIST = new String[] {
      "DANVILLE"
  };
}
