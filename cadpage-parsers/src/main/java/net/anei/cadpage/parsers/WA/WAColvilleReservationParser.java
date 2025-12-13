package net.anei.cadpage.parsers.WA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA94Parser;

public class WAColvilleReservationParser extends DispatchA94Parser {

  public WAColvilleReservationParser() {
    super("COLVILLE RESERVATION", "WA");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern CR_PTN = Pattern.compile("\\bCR\\b");
  @Override
  public String adjustMapAddress(String addr) {
    addr = CR_PTN.matcher(addr).replaceAll("CREEK");
    return addr;
  }

}
