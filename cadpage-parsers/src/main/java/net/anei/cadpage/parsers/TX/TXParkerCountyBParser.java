package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;
/**
 * Parker County, TX (B)
 */
public class TXParkerCountyBParser extends DispatchOSSIParser {

  public TXParkerCountyBParser() {
    super(CITY_CODES, "PARKER COUNTY", "TX",
          "( SELECT/STATUS UNIT CALL ADDR CITY/Y CALL2 " +
          "| CALL ADDR CITY/Y! X+? INFO/N+? ( CH SRC UNIT! | SRC UNIT! | UNIT! | END ) MAP? Radio_Channel:CH " +
          ") INFO/N+");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "Text Message");
    if (body.contains(",Enroute,")) {
      setSelectValue("STATUS");
      return parseFields(body.split(","), data);
    } else {
      if (!body.startsWith("CAD:"))  body = "CAD:" + body;
      setSelectValue("");
      return super.parseMsg(body, data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("CH")) return new ChannelField("TAC\\d*|WP?FD", true);
    if (name.equals("SRC")) return new SourceField("ST\\d+|TC\\d+", true);
    if (name.equals("UNIT")) return new UnitField("(?:\\b[A-Z]+\\d+\\b,?)+", true);
    return super.getField(name);
  }

  private class MyCall2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, " for ", field);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALD", "ALEDO",
      "AZL", "AZLE",
      "CRE", "CRESSON",
      "FTW", "FORT WORTH",
      "LIP", "LIPAN",
      "MIL", "MILLSAP",
      "POL", "POOLVILLE",
      "SPT", "SPRINGTOWN",
      "WFD", "WEATHERFORD"
  });
}
