package net.anei.cadpage.parsers.WA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;



public class WAStevensCountyParser extends DispatchA19Parser {

  public WAStevensCountyParser() {
    super("STEVENS COUNTY", "WA");
  }

  @Override
  public String getFilter() {
    return "spillmancad.gov,@lifeflight.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern MSPACE_PTN = Pattern.compile(" {3,}");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    int pt = subject.indexOf("Incident #:");
    if (pt >= 0) {
      body = MSPACE_PTN.matcher(subject.substring(pt)).replaceAll("\n") + '\n' + body;
      subject = subject.substring(0,pt).trim();
    }

    return super.parseMsg(subject, body, data);
  }
}
