package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class GABibbCountyParser extends DispatchProQAParser {

  public GABibbCountyParser() {
    super("BIBB COUNTY", "GA",
          "ID! CALL! CALL/L+? PRI! PLACE/L+? ADDR/Z EMPTY APT CITY SKIP DEST_ADDR DEST_CITY! DEST_APT? INFO/L+", true);
  }

  @Override
  public String getFilter() {
    return "noreply@ZollHosted.com";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new PriorityField("Priority (\\d+) .*", true);
    return super.getField(name);
  }
}
