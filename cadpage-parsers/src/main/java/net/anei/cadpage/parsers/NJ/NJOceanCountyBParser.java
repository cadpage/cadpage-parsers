package net.anei.cadpage.parsers.NJ;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class NJOceanCountyBParser extends DispatchA19Parser {

  public NJOceanCountyBParser() {
    super(CITY_CODES, "OCEAN COUNTY", "NJ");
  }

  @Override
  public String getFilter() {
    return "@trpolice.org,@alert.active911.com,ripnrun@lakewoodpolicenj.com,FlexRapidNotification@dccnotify.com,Fire_records@co.ocean.nj.us";
  }

  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.startsWith("LAKEWOOD DISPATCH")) {
      subject = subject.substring(17).trim();
      subject = MBLANK_PTN.matcher(subject).replaceAll("\n");
      body = append(subject, " ", body);
    }
    return super.parseMsg(subject, body, data);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "LKH", "LAKEHURST",
      "MAN", "MANCHESTER TWP"
  });

}
