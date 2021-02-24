package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class TXGalvestonCountyBParser extends DispatchOSSIParser {

  public TXGalvestonCountyBParser() {
    super(TXGalvestonCountyAParser.CITY_CODES, "GALVESTON COUNTY", "TX",
          "( CANCEL ADDR CITY! " +
          "| FYI ID SRC CALL ADDR CITY UNIT! UNIT/C+? ) INFO/N+");
  }

  private static final Pattern TX_LA_PORTE_MARKER = Pattern.compile(";\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d;");

  @Override
  protected boolean parseMsg(String body, Data data) {

    // Reject TXLaPorte alerts
    if (TX_LA_PORTE_MARKER.matcher(body).find()) return false;

    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    if (name.equals("SRC")) return new SourceField("[A-Z]{4}", true);
    if (name.equals("UNIT")) return new UnitField("[A-Z]+\\d+", true);
    return super.getField(name);
  }
}
