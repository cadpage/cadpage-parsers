package net.anei.cadpage.parsers.NC;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Burke County, NC
 */
public class NCBurkeCountyParser extends DispatchOSSIParser {

  public NCBurkeCountyParser() {
    super(CITY_CODES, "BURKE COUNTY", "NC",
          "( CANCEL ADDR CITY? | FYI? SRC CALL CODE? ADDR! X? X? ) INFO+");
  }

  @Override
  public String getFilter() {
    return "CAD@bceoc.org,CAD@burke.local";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    if (body.startsWith("|")) body = body.substring(1).trim();
    return super.parseMsg(body, data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("\\S+", true);
    if (name.equals("CANCEL")) return new CallField("CANCEL|UNDER CONTROL", true);
    if (name.equals("CODE")) return new CodeField("\\d\\d[A-Z]\\d\\d[A-Za-z]?", true);
    return super.getField(name);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "HICK", "HICKORY",
      "LONG", "LONG VIEW",
      "NEBO", "NEBO",
      "NEWL", "JONAS RIDGE"  // ???
  });
}
