package net.anei.cadpage.parsers.FL;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class FLLeeCountyBParser extends DispatchOSSIParser {

  public FLLeeCountyBParser() {
    super(CITY_CODES, "LEE COUNTY", "FL",
          "( CANCEL ADDR CITY! " +
          "| FYI DATETIME ADDR CALL! " +
          ") INFO/N+");
  }

  @Override
  public String getFilter() {
    return "CAD@capecoral.net,CAD@capecoral.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Text Message")) return false;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CC", "CAPE CORAL"
  });

}
