package net.anei.cadpage.parsers;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles alerts from IHS Status Panel device
 */
public class StatusPanelDeviceParser extends MsgParser {

  public StatusPanelDeviceParser() {
    super("", "", CountryCode.SE);
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{8,9}");
  @Override
  protected boolean parseMsg(String body, Data data) {

    do {
      String[] flds = body.split("\u001b");
      if (flds.length != 6) break;
      if (!GPS_PTN.matcher(flds[3]).matches()) break;
      if (!GPS_PTN.matcher(flds[4]).matches()) break;
      data.strCall = flds[5].trim();
      data.strGPSLoc = cvtGps(flds[4])+','+cvtGps(flds[3]);
      return true;

    } while (false);

    data.msgType = MsgInfo.MsgType.GEN_ALERT;
    data.strSupp = body.replace('\u001b', '\b');
    return true;
  }

  private String cvtGps(String field) {
    int pt = field.length()-6;
    return field.substring(0,pt)+'.'+field.substring(pt);
  }
}
