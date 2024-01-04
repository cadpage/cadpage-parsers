package net.anei.cadpage.parsers.AK;

import net.anei.cadpage.parsers.dispatch.DispatchA90Parser;

public class AKAnchorageParser extends DispatchA90Parser {

  public AKAnchorageParser() {
    super("ANCHORAGE", "AK");
  }

  @Override
  public String getFilter() {
    return "jberfire907@gmail.com,monacoenterprises2014@gmail.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
