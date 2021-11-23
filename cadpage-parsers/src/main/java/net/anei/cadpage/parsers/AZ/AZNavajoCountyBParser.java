package net.anei.cadpage.parsers.AZ;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

/**
 * Navajo County, AZ (B)
 */
public class AZNavajoCountyBParser extends DispatchA20Parser {

  public AZNavajoCountyBParser() {
    super("NAVAJO COUNTY", "AZ");
  }

  @Override
  public String getFilter() {
    return "@ci.show-low.ca.us,police@stpd.org";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("\\([A-Z ]+\\)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (SUBJECT_PTN.matcher(subject).matches()) subject = "Dispatched Call " + subject;
    if (! super.parseMsg(subject, body, data)) return false;
    data.strUnit = "";
    return true;
  }
}
