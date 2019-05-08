package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/*
Humble, TX (B)
*/

public class TXHumbleBParser extends DispatchOSSIParser {
  public TXHumbleBParser() {
    super(CITY_CODE, "HUMBLE", "TX",
          "( CANCEL ADDR! CITY " +
          "| FYI? CALL ADDR! X/Z+? ID " + 
          ") INFO/N+");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{8}", true);
  
    return super.getField(name);
  }
  
  private static final Properties CITY_CODE = buildCodeTable(new String[] {
      "HARR", "HARRIS COUNTY",
      "HUMB", "HUMBLE",
  });
}
