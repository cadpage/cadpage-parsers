package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class TXMansfieldParser extends DispatchOSSIParser {

  public TXMansfieldParser() {
    super(CITY_CODES, "MANSFIELD", "TX",
          "( CANCEL ADDR CITY " +
          "| FYI? CALL ADDR CITY? ( PLACE X/Z X/Z SRC! | SRC! | ( X | PLACE ) X/Z? SRC! ) PLACENAME? EMPTY? CH? ) INFO/N+");
  }

  @Override
  public String getFilter() {
    return "CAD@mansfieldtexas.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Text Message")) return false;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z]{1,3}(?:EMS|FD|PD)|KENN|MANS", true);
    if (name.equals("PLACENAME")) return new PlaceNameField("[ ,A-Z]{1,20}", true);
    if (name.equals("CH")) return new ChannelField("(CH(?:AN)? *\\d+)", true);
    return super.getField(name);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "KENN", "KENNEDALE",
      "MANS", "MANSFIELD",
      "TRCO", "TARRANT COUNTY"
  });

}
